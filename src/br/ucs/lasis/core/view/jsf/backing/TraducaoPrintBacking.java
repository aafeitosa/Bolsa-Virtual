package br.ucs.lasis.core.view.jsf.backing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Traducao;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.session.TraducaoSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Named("traducaoPrintBack")
@ViewScoped
public class TraducaoPrintBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	@Inject
	private TraducaoSession traducaoSession;

	@Inject
	private IdiomaSession idiomaSession;

	private List<Traducao> traducoes;
	
	private Idioma idioma;
	private Boolean jaTraduzido;
	private String codigo;
	private Long idiomaId;
	
	public Boolean getJaTraduzido() {
		return jaTraduzido;
	}

	public void setJaTraduzido(Boolean jaTraduzido) {
		this.jaTraduzido = jaTraduzido;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public Long getIdiomaId() {
		return idiomaId;
	}

	public void setIdiomaId(Long idiomaId) {
		this.idiomaId = idiomaId;
	}

	public void init() {
		if(idiomaId!=null) {
			idioma = idiomaSession.buscarPorId(idiomaId);
		}
		this.pesquisar();
	}

	public List<Traducao> getTraducoes() {
		return traducoes;
	}

	public void pesquisar() {
		
		GridLazyLoaderDTO gridLazyLoaderDTO = new GridLazyLoaderDTO();

		Map<String, Object> filters = new HashMap<String, Object>();

		filters.put("codigo", codigo);
		filters.put("idioma", idioma);
		filters.put("jaTraduzido", jaTraduzido);
		
		gridLazyLoaderDTO.setFilters(filters);

		PagedResult<Traducao> pagedResult = traducaoSession
				.findTranslationsWith(gridLazyLoaderDTO);
		
		this.traducoes = pagedResult.getPage();
	}
}