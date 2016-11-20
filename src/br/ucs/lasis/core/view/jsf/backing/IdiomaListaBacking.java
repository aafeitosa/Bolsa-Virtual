package br.ucs.lasis.core.view.jsf.backing;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;

@Named("idiomaListaBack")
@ViewScoped
public class IdiomaListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private String nome;

	private GridLazyLoader<Idioma> gridLazyLoader;

	@Inject
	private IdiomaSession idiomaSession;
	
	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
	}

	private GridLazyLoader<Idioma> atualizaGrid() {

		return new GridLazyLoader<Idioma>(new IGridLazyLoader<Idioma>() {

			@Override
			public PagedResult<Idioma> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("nome", getNome());

				return idiomaSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	// @ExceptionInterceptor
	public GridLazyLoader<Idioma> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Idioma> getDataModel() {
		return this.gridLazyLoader;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String novo() {
		return "formIdiomas";
	}

	public String listagem() {
		return "listaIdiomas";
	}

	public void exclui(ActionEvent event) {
		try {
			idiomaSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}
}
