package br.ucs.lasis.lasis1.view.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Named("periodoListaBack")
@ViewScoped
public class PeriodoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private String nome;

	private GridLazyLoader<Periodo> gridLazyLoader;

	@EJB
	private PeriodoSession periodoSession;
	
	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
	}

	private GridLazyLoader<Periodo> atualizaGrid() {

		return new GridLazyLoader<Periodo>(new IGridLazyLoader<Periodo>() {

			@Override
			public PagedResult<Periodo> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("nome", getNome());

				return periodoSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	// @ExceptionInterceptor
	public GridLazyLoader<Periodo> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Periodo> getDataModel() {
		return this.gridLazyLoader;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String novo() {
		return "formPeriodos";
	}

	public String listagem() {
		return "listaPeriodos";
	}

	public void exclui(ActionEvent event) {
		try {
			periodoSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}
}
