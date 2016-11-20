package br.ucs.lasis.lasis1.view.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Investimento;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("investimentoListaBack")
@ViewScoped
public class InvestimentoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Grupo grupo;
	private List<Grupo> grupos;

	@Inject
	@EmpresaLogada
	private Empresa empresa;

	@Inject
	@PeriodoCorrente
	private Periodo periodo;

	@Inject
	@UsuarioLogado
	private Usuario usuario;
	
	@Inject
	@PerfilCoordenador
	private Perfil perfilCoordenador;
	
	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;

	private GridLazyLoader<Rodada> gridLazyLoader;

	@EJB
	private RodadaSession rodadaSession;

	@EJB
	private GrupoSession grupoSession;

	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
		this.grupos = grupoSession.buscaGruposDoUsuario();

		Long idGrupo = this.getParametroLong("grupo_id");
		if (idGrupo != null) {
			this.grupo = grupoSession.buscaPorId(idGrupo);
		} else {
			this.grupo = grupoSession.buscaPrimeiroGrupoDaEmpresa(periodo,
					empresa);
			if (this.grupo == null && !this.grupos.isEmpty()) {
				this.grupo = this.grupos.get(0);
			}
		}
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	private GridLazyLoader<Rodada> atualizaGrid() {

		return new GridLazyLoader<Rodada>(new IGridLazyLoader<Rodada>() {

			@Override
			public PagedResult<Rodada> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();
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

	public String novo() {
		return "formInvestimentos";
	}

	public String listagem() {
		return "listaInvestimentos";
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

	public boolean isMostrarGrupo() {
		return usuario.isAdministrador();
	}

	public boolean isTrocaGrupo() {
		return (usuario.isAdministrador() || usuario.getPerfis().contains(perfilCoordenador) || usuario.getPerfis().contains(perfilProfessor));
	}
	
	public void onGrupoSelect(AjaxBehaviorEvent event) {
//		System.out.println("trocou grupo");
		// this.empresas = new
		// ArrayList<Empresa>(grupoSession.buscaEmpresasDoGrupo(grupo));
		// investimentos = new ArrayList<Investimento>();
	}

}
