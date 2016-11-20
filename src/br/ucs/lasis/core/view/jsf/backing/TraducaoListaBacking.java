package br.ucs.lasis.core.view.jsf.backing;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Traducao;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.session.TraducaoSession;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.core.view.resourcebundle.TranslationsResourceBundle;

@Named("traducaoListaBack")
@ViewScoped
public class TraducaoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private Idioma idioma;
	private Boolean jaTraduzido = new Boolean(false);

	private GridLazyLoader<Traducao> gridLazyLoader;

	@Inject
	private TraducaoSession traducaoSession;

	@Inject
	private IdiomaSession idiomaSession;

	private List<Idioma> idiomas;

	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
		this.idiomas = idiomaSession.buscaTodos();
	}

	private GridLazyLoader<Traducao> atualizaGrid() {

		return new GridLazyLoader<Traducao>(new IGridLazyLoader<Traducao>() {

			@Override
			public PagedResult<Traducao> load(
					GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("codigo", getCodigo());
				filters.put("idioma", getIdioma());
				filters.put("jaTraduzido", getJaTraduzido());

				return traducaoSession.findTranslationsWith(gridLazyLoaderDTO);
			}
		});
	}

	public GridLazyLoader<Traducao> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Traducao> getDataModel() {
		return this.gridLazyLoader;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public Long getIdiomaId() {
		if (this.idioma != null) {
			return this.idioma.getId();
		}
		return null;
	}

	public void setIdiomaId(Long idiomaId) {
		if (this.idioma != null) {
			this.idioma.setId(idiomaId);
		}
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	public List<Idioma> getIdiomas() {
		return idiomas;
	}

	public Boolean getJaTraduzido() {
		return jaTraduzido;
	}

	public void setJaTraduzido(Boolean jaTraduzido) {
		if (jaTraduzido == null) {
			this.jaTraduzido = false;
		} else {
			this.jaTraduzido = jaTraduzido;
		}
	}

	public String novo() {
		return "formTraducoes";
	}

	public String listagem() {
		return "listaTraducoes";
	}

	public String print() {
		return "printTraducoes" + "?faces-redirect=true"
				+ "&includeViewParams=true" + "&output=pdf";
	}

	public void exclui(ActionEvent event) {
		try {
			traducaoSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}

	public void onRowEdit(RowEditEvent event) {

		Traducao traducao = (Traducao) event.getObject();

		try {
			traducaoSession.update(traducao);
			updateResourceBundle();
			this.showInfoMessage(this.getTranslation("alterado"),
					traducao.getCodigo());
		} catch (Exception e) {
			this.showConstraintError();
		}

	}

	public void onRowCancel(RowEditEvent event) {
		this.showInfoMessage(this.getTranslation("edicao_cancelada"), "");
	}

	private void updateResourceBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		TranslationsResourceBundle bundle = (TranslationsResourceBundle) context
				.getApplication().getResourceBundle(context, "msgs");
		if (bundle != null) {
			bundle.updateBundle();
		}
	}
}
