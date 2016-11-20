package br.ucs.lasis.lasis1.view.bean;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("rodadaListaBack")
@ViewScoped
public class RodadaListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Integer numero;
	private Grupo grupo;
	private List<Grupo> grupos;

	private GridLazyLoader<Rodada> gridLazyLoader;
	
	@EJB
	private RodadaSession rodadaSession;

	@EJB
	private GrupoSession grupoSession;

	
	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
		this.grupos = rodadaSession.buscaGruposDoUsuario();
		if(this.grupos!=null&&this.grupos.size()==1) {
			this.grupo = this.grupos.get(0);
		}
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	private GridLazyLoader<Rodada> atualizaGrid() {

		return new GridLazyLoader<Rodada>(new IGridLazyLoader<Rodada>() {

			@Override
			public PagedResult<Rodada> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("numero", getNumero());
				filters.put("grupo", getGrupo());

				return rodadaSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	// @ExceptionInterceptor
	public GridLazyLoader<Rodada> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<Rodada> getDataModel() {
		return this.gridLazyLoader;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String novo() {
		return "formRodadas";
	}

	public String listagem() {
		return "listaRodadas";
	}

	public String encerra(Rodada rodada) {
		
		rodada.setEncerrada(true);
		rodadaSession.salva(rodada);
		
		return "";
	}
	
	public void exclui(ActionEvent event) {
		try {
			rodadaSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}

	public void passarGrupo(ActionEvent event) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.put("grupo", grupo);
	}

}
