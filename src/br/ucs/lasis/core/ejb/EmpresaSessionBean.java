package br.ucs.lasis.core.ejb;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import br.ucs.lasis.core.ejb.interceptors.PersistenceExceptionInterceptor;
import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.PerfilAluno;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.EmpresaSession;
import br.ucs.lasis.core.session.TraducaoSession;
import br.ucs.lasis.core.session.UsuarioSession;

@Stateless
@Interceptors(value = PersistenceExceptionInterceptor.class)
public class EmpresaSessionBean implements EmpresaSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;

	@Inject
	@PerfilAluno
	private Perfil perfilAluno;

	@Inject
	private TraducaoSession traducaoSession;

	@EJB
	private UsuarioSession usuarioSession;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	@Inject
	private ParametrosSingletonBean parametroSingletonBean;

	@Override
	public void salvar(Empresa empresa) throws BusinessException {
		this.salvar(empresa, false);
	}

	@Override
	public void salvar(Empresa empresa, boolean criarUsuario)
			throws BusinessException {

		if (empresa != null) {
			if (empresa.getIdioma() == null) {
				empresa.setIdioma(traducaoSession.getIdiomaPadrao());
			}
		}

		try {
			if (empresa.getId() == null) {
				em.persist(empresa);
			} else {
				em.merge(empresa);
				em.getEntityManagerFactory().getCache()
						.evict(Empresa.class, empresa.getId());
			}

			if (criarUsuario) {
				Usuario usuario = new Usuario();
				usuario.setLogin(empresa.getAcronimo());
				usuario.setNome("user" + empresa.getAcronimo());
				usuario.setPassword(empresa.getAcronimo());
				usuario.setAtivo(true);
				usuario.setEmpresa(empresa);
				Set<Perfil> perfis = new HashSet<Perfil>();
				perfis.add(perfilAluno);
				usuario.setPerfis(perfis);
				usuarioSession.salvar(usuario);
			}

		} catch (PersistenceException e) {
			throw new BusinessException(this.getClass().getName(), e);
		}
	}

	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, Empresa.class);
	}

	@Override
	public Empresa buscarPorId(long id) throws BusinessException {
		try {
			Empresa empresa = em.find(Empresa.class, id);
			return empresa;
		} catch (Exception e) {
			throw new BusinessException("Buscando Empresa", e);
		}
	}

	@Override
	public List<Empresa> buscaTodos() throws BusinessException {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Empresa> buscaTodasAtivas() throws BusinessException {
		try {

			Query query = em.createNamedQuery(Empresa.TODAS_EMPRESAS_ATIVAS);
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todas as empresas ativas", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Empresa> buscaTodos(Integer start, Integer limit)
			throws BusinessException {

		try {

			Query query = em.createNamedQuery(Empresa.TODAS_EMPRESAS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todas as empresas", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Empresa> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) throws BusinessException {

		StringBuilder queryString = new StringBuilder(
				"SELECT e FROM Empresa as e");

		queryString.append(" WHERE 1 = 1");

//		System.out.println("-----------------------------------------------");
//		System.out.println(usuarioLogado.getNome() + " " + usuarioLogado.isAdministrador());
//		System.out.println("-----------------------------------------------");

		if (!usuarioLogado.isAdministrador()) {
			Long id = new Long(
					parametroSingletonBean
							.buscarValorParametroAsInteger(ParametrosEnum.ID_EMPRESA_PRINCIPAL));
			if (id != null) {
				queryString.append(" AND e.id != " + id);
			}
		}

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				queryString.append(" AND UPPER(e.nome) like UPPER(:nome)");
			}

			if (parametros.containsKey("ativo")) {
				queryString.append(" AND e.ativo = :ativo");
			}
		}

		queryString.append(" ORDER BY e.nome");

		Query query = em.createQuery(queryString.toString());

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				query.setParameter("nome", "%" + parametros.get("nome") + "%");
			}

			if (parametros.containsKey("ativo")) {
				query.setParameter("ativo", parametros.get("ativo"));
			}
		}

		if (start != null && limit != null) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}

		return query.getResultList();

	}

	@Override
	public int getQuantidadeTotal(Map<String, Object> parametros)
			throws BusinessException {

		StringBuilder queryString = new StringBuilder(
				"SELECT count(e) FROM Empresa as e");
		queryString.append(" WHERE 1 = 1");

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				queryString.append(" AND UPPER(e.nome) like UPPER(:nome)");
			}

			if (parametros.containsKey("ativo")) {
				queryString.append(" AND e.ativo = :ativo");
			}
		}

		Query query = em.createQuery(queryString.toString());

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				query.setParameter("nome", "%" + parametros.get("nome") + "%");
			}

			if (parametros.containsKey("ativo")) {
				query.setParameter("ativo", parametros.get("ativo"));
			}
		}

		Long quantidade = (Long) query.getSingleResult();
		return quantidade != null ? quantidade.intValue() : 0;

	}

	@Override
	public int getQuantidadeTotal() throws BusinessException {

		try {
			Query query = em.createNamedQuery(Empresa.QUANTAS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todas as empresas", e);
		}
	}

}
