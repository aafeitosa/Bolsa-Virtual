package br.ucs.lasis.lasis1.view.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.session.GrupoSession;

@Named("grupoListaBack")
@ViewScoped
public class GrupoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private String nome;

	private GridLazyLoader<Grupo> gridLazyLoader;

	@Inject
	private GrupoSession grupoSession;
	
	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
	}

	private GridLazyLoader<Grupo> atualizaGrid() {

		return new GridLazyLoader<Grupo>(new IGridLazyLoader<Grupo>() {

			@Override
			public PagedResult<Grupo> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("nome", getNome());

				return grupoSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	// @ExceptionInterceptor
	public GridLazyLoader<Grupo> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Grupo> getDataModel() {
		return this.gridLazyLoader;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String novo() {
		return "formGrupos";
	}

	public String listagem() {
		return "listaGrupos";
	}

	public void exclui(ActionEvent event) {
		try {
			grupoSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}
}
