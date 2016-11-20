package br.ucs.lasis.core.producer;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import br.ucs.lasis.core.ejb.ParametrosSingletonBean;
import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.qualifier.EmpresaSistema;
import br.ucs.lasis.core.qualifier.PerfilAluno;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.PerfilSession;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Named("geralProducer")
@SessionScoped
public class UsuarioLogadoProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Principal principal;

	@Inject
	@DataRepository
	private transient EntityManagerExtended em;

	@Inject
	private ParametrosSingletonBean parametroSingletonBean;

	@EJB
	private PeriodoSession periodoSession;

	@Inject
	private PerfilSession perfilSession;

	private Usuario usuario;

	private Empresa empresaPrincipal;
	
	private Periodo periodoCorrente;

	private Perfil perfilAluno;
	private Perfil perfilProfessor;
	private Perfil perfilCoordenador;
	
	@Produces
	@UsuarioLogado
	public Usuario produzUsuarioLogado() {

		if (usuario == null) {
			if (principal == null || "anonymous".equals(principal.getName())) {
				return null;
			}
			this.usuario = this.buscaPorLogin(principal.getName());
		}

		return usuario;
	}

	@Produces
	@PeriodoCorrente
	public Periodo produzPeriodoCorrente() {
		
		return getPeriodoCorrente();
	}

	@Produces
	@EmpresaLogada
	public Empresa produzEmpresaLogada() {

		produzUsuarioLogado();

		if (this.usuario == null) {
			return null;
		}

		return this.usuario.getEmpresa();
	}

	@Produces
	@PerfilAluno
	public Perfil produzPerfilAluno() {

		if (this.perfilAluno == null) {
			this.perfilAluno = buscaPerfil(ParametrosEnum.ID_PERFIL_ALUNO);
		}
		return this.perfilAluno;
	}

	@Produces
	@PerfilProfessor
	public Perfil produzPerfilProfessor() {

		if (this.perfilProfessor == null) {
			this.perfilProfessor = buscaPerfil(ParametrosEnum.ID_PERFIL_PROFESSOR);
		}
		return this.perfilProfessor;
	}

	@Produces
	@PerfilCoordenador
	public Perfil produzPerfilCoordenador() {

		if (this.perfilCoordenador == null) {
			this.perfilCoordenador = buscaPerfil(ParametrosEnum.ID_PERFIL_COORDENADOR);
		}
		return this.perfilCoordenador;
	}

	private Perfil buscaPerfil(ParametrosEnum idPerfil) {

		Perfil perfil = null;

		Long id = new Long(
				parametroSingletonBean.buscarValorParametroAsInteger(idPerfil));

		if (id != null) {
			perfil = perfilSession.buscarPorId(id);
		}

		return perfil;
	}

	@Produces
	@EmpresaPrincipal
	public boolean produzEmpresaPrincipal() {

		if (empresaPrincipal == null) {
			empresaPrincipal = buscaEmpresaPrincipal();
		}

		Empresa empresaLogada = produzEmpresaLogada();

		if (empresaLogada == null) {
			return false;
		}

		return empresaLogada.equals(empresaPrincipal);
	}

	@Produces
	@EmpresaSistema
	public Empresa produzEmpresaSistema() {

		if (empresaPrincipal == null) {
			empresaPrincipal = buscaEmpresaPrincipal();
		}

		return this.empresaPrincipal;
	}

	@SuppressWarnings("unchecked")
	private Usuario buscaPorLogin(String login) throws BusinessException {

		try {

			Query query = em.createNamedQuery(Usuario.POR_LOGIN);
			query.setParameter("login", login);

			Usuario usuario = null;

			List<Usuario> usuarios = query.getResultList();
			if (!usuarios.isEmpty()) {
				usuario = usuarios.get(0);
			}

			Perfil perfilAdministrador = buscaPerfil(ParametrosEnum.ID_PERFIL_ADMINISTRADOR);
			if (perfilAdministrador != null) {
				if (usuario.getPerfis().contains(perfilAdministrador)) {
					usuario.setAdministrador(true);
				}
			}

			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(
					"Buscando todas as usuarios com login igual ao informado",
					e);
		}
	}

	private Empresa buscaEmpresaPrincipal() {

		Empresa empresa = null;

		Long id = parametroSingletonBean
				.buscarValorParametroAsId(ParametrosEnum.ID_EMPRESA_PRINCIPAL);

		if (id != null) {
			empresa = em.find(Empresa.class, id);
		}

		return empresa;
	}

	public Periodo getPeriodoCorrente() {
		if(periodoCorrente == null) {
			setPeriodoCorrente(periodoSession.buscaPorData(new Date()));
		}
		return periodoCorrente;
	}

	public void setPeriodoCorrente(Periodo periodoCorrente) {
		this.periodoCorrente = periodoCorrente;
	}

}
