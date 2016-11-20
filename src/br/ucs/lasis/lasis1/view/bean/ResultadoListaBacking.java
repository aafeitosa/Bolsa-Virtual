package br.ucs.lasis.lasis1.view.bean;

import java.util.List;

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
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("resultadoListaBack")
@ViewScoped
public class ResultadoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private GridLazyLoader<Grupo> gridLazyLoader;

	@Inject
	private GrupoSession grupoSession;

	@Inject
	private RodadaSession rodadaSession;
	
	private List<Grupo> grupos;
	
	@PostConstruct
	public void init() {
		this.grupos = rodadaSession.buscaGruposDoUsuario();
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
	}

	private GridLazyLoader<Grupo> atualizaGrid() {

		return new GridLazyLoader<Grupo>(new IGridLazyLoader<Grupo>() {

			@Override
			public PagedResult<Grupo> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				return grupoSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	public GridLazyLoader<Grupo> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Grupo> getDataModel() {
		return this.gridLazyLoader;
	}
	
	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public String visualizar() {
		return "formResultados";
	}

	public String listagem() {
		return "listaResultados";
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}
}
