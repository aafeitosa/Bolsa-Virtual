package br.ucs.lasis.core.ejb;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.ucs.lasis.core.ejb.interceptors.PersistenceExceptionInterceptor;
import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.PerfilPermissao;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.session.PerfilSession;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.core.util.SenhaUtils;
import br.ucs.lasis.core.util.StringUtils;
import br.ucs.lasis.lasis1.model.entity.Periodo;

@Stateless
@Interceptors(value = PersistenceExceptionInterceptor.class)
public class UsuarioSessionBean implements UsuarioSession {

	@PersistenceContext
	@DataRepository
	private EntityManagerExtended em;

	@Inject
	@EmpresaLogada
	private Empresa empresaLogada;

	@Inject
	@EmpresaPrincipal
	private boolean isEmpresaPrincipal;

	@Resource
	SessionContext context;

	@Inject
	private ParametrosSingletonBean parametroSingletonBean;

	@Inject
	private PerfilSession perfilSession;

	@Override
	public void salvar(Usuario usuario) throws BusinessException {
		try {
			if (usuario.getId() == null) {
				String crypt = StringUtils.getHash(usuario.getLogin()
						+ usuario.getPassword());
				usuario.setPassword(crypt);
				em.persist(usuario);
			} else {
				em.merge(usuario);
				em.getEntityManagerFactory().getCache()
						.evict(Usuario.class, usuario.getId());
			}
		} catch (Exception e) {
			throw new BusinessException(this.getClass().getName(), e);
		}
	}

	@Override
	public void alteraSenha(Long idUsuario, String senhaAtual, String novaSenha)
			throws BusinessException {

		if (senhaAtual == null || senhaAtual.isEmpty()) {
			String mensagem = "A senha antiga não pode ser deixada em branco";
//			System.out.println(mensagem);
			throw new BusinessException(mensagem);
		}

		Usuario buscado = em.find(Usuario.class, idUsuario);

		String crypt = StringUtils.getHash(buscado.getLogin() + senhaAtual);

		if (!crypt.equals(buscado.getPassword())) {
			String mensagem = "A senha informada não confere";
//			System.out.println(mensagem);
			throw new BusinessException(mensagem);
		}
		
		String crypt2 = StringUtils.getHash(buscado.getLogin() + novaSenha);

		if (crypt2.equals(buscado.getPassword())) {
			String mensagem = "A nova senha não pode ser igual à antiga";
//			System.out.println(mensagem);
			throw new BusinessException(mensagem);
		}

		alteraSenha(idUsuario, novaSenha);
	}

	@Override
	public void alteraSenha(Long idUsuario, String novaSenha)
			throws BusinessException {

		Usuario buscado = em.find(Usuario.class, idUsuario);

		String crypt2 = StringUtils.getHash(buscado.getLogin() + novaSenha);

		buscado.setPassword(crypt2);

		try {
			em.merge(buscado);
		} catch (Exception e) {
			throw new BusinessException(this.getClass().getName(), e);
		}
	}
	
	@Override
	public void exclui(Long id) throws BusinessException {
		Usuario usuario = em.find(Usuario.class, id);
		if (usuario != null) {
			em.remove(usuario);
		}
	}

	@Override
	public Usuario buscarPorId(long id) throws BusinessException {
		try {
			Usuario usuario = em.find(Usuario.class, id);
			// Para forçar a carga das listas
			usuario.getPerfis().isEmpty();
			return usuario;
		} catch (Exception e) {
			throw new BusinessException("Buscando Usuario", e);
		}
	}

	@Override
	public List<Usuario> buscaTodos() throws BusinessException {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaTodos(Integer start, Integer limit)
			throws BusinessException {

		try {

			Query query;

			if (isEmpresaPrincipal) {
				query = em.createNamedQuery(Usuario.TODOS_USUARIOS);
			} else {
				query = em.createNamedQuery(Usuario.TODOS_USUARIOS_EMPRESA);
				query.setParameter("empresa", this.empresaLogada);
			}

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os usuários", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) throws BusinessException {

//		System.out.println("Parametros >>");
//		System.out.println(parametros.keySet());

		StringBuilder queryString = new StringBuilder();

		// if (parametros != null && parametros.size() > 0 &&
		// parametros.containsKey("grupo")
		// && parametros.get("grupo") != null) {
		// queryString.append("SELECT e FROM Usuario as e, Grupo as g\n");
		// queryString.append(" WHERE g = :grupo\n");
		// queryString.append(" AND e MEMBER OF g.empresas\n");
		//
		// } else {
		// queryString.append("SELECT e FROM Usuario as e\n");
		// queryString.append(" WHERE 1 = 1\n");
		// }

		queryString.append("SELECT e FROM Usuario as e\n");
		queryString.append(" WHERE 1 = 1\n");

		if (!isEmpresaPrincipal) {
			parametros.put("empresa", this.empresaLogada);
		}

		if (parametros != null && parametros.size() > 0) {

			if (parametros.containsKey("empresa")
					&& parametros.get("empresa") != null) {
				queryString.append(" AND e.empresa = :empresa\n");
			}

			// if (parametros.containsKey("grupo")
			// && parametros.get("grupo") != null) {
			// queryString.append(" AND g = :grupo");
			// }

			/*
			 * SELECT crs FROM Cruise AS crs, IN (crs.reservations) AS res,
			 * Customer AS cust WHERE cust = :myCustomer AND cust MEMBER OF
			 * res.customers “Busca todos os cruzeiros para os quais um
			 * determinado cliente possua reserva”
			 */

			if (parametros.containsKey("nome")) {
				queryString.append(" AND UPPER(e.nome) like UPPER(:nome)\n");
			}

			if (parametros.containsKey("ativo")) {
				queryString.append(" AND e.ativo = :ativo\n");
			}

			if (parametros.containsKey("perfil")) {
				queryString.append(" AND :perfil MEMBER OF e.perfis\n");
			}

		}

		queryString.append(" ORDER BY e.nome\n");

		Query query = em.createQuery(queryString.toString());

//		System.out.println(queryString.toString());

		if (parametros != null && parametros.size() > 0) {

			if (parametros.containsKey("empresa")
					&& parametros.get("empresa") != null) {
				query.setParameter("empresa",
						(Empresa) parametros.get("empresa"));
			}

			// if (parametros.containsKey("grupo")
			// && parametros.get("grupo") != null) {
			// query.setParameter("grupo",
			// (Grupo) parametros.get("grupo"));
			// }

			if (parametros.containsKey("nome")) {
				query.setParameter("nome", "%" + parametros.get("nome") + "%");
			}

			if (parametros.containsKey("ativo")) {
				query.setParameter("ativo", parametros.get("ativo"));
			}

			if (parametros.containsKey("perfil")) {
				query.setParameter("perfil", parametros.get("perfil"));
			}
		}

		if (start != null && limit != null) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}

		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaTodos(Integer start, Integer limit,
			Periodo periodo) throws BusinessException {
		
		Query query = em.createNativeQuery(Usuario.TODOS_USUARIOS_PERIODO);
		query.setParameter(1, periodo);
		
		if (start != null && limit != null) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}


	@Override
	public int getQuantidadeTotal() throws BusinessException {

		try {
			Query query;

			if (isEmpresaPrincipal) {
				query = em.createNamedQuery(Usuario.QUANTOS);
			} else {
				query = em.createNamedQuery(Usuario.QUANTOS_EMPRESA);
				query.setParameter("empresa", this.empresaLogada);
			}

			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os usuários", e);
		}
	}

	@Override
	public int getQuantidadeTotal(Map<String, Object> parametros)
			throws BusinessException {

		StringBuilder queryString = new StringBuilder(
				"SELECT count(e) FROM Usuario as e");
		queryString.append(" WHERE 1 = 1");

		if (!isEmpresaPrincipal) {
			parametros.put("empresa", this.empresaLogada);
		}

		if (parametros != null && parametros.size() > 0) {

			if (parametros.containsKey("empresa")
					&& parametros.get("empresa") != null) {
				queryString.append(" AND e.empresa = :empresa");
			}

			if (parametros.containsKey("nome")) {
				queryString.append(" AND UPPER(e.nome) like UPPER(:nome)");
			}

			if (parametros.containsKey("ativo")) {
				queryString.append(" AND e.ativo = :ativo");
			}

			if (parametros.containsKey("perfil")) {
				queryString.append(" AND :perfil MEMBER OF e.perfis");
			}
		}

		Query query = em.createQuery(queryString.toString());

		if (parametros != null && parametros.size() > 0) {

			if (parametros.containsKey("empresa")
					&& parametros.get("empresa") != null) {
				query.setParameter("empresa",
						(Empresa) parametros.get("empresa"));
			}

			if (parametros.containsKey("nome")) {
				query.setParameter("nome", "%" + parametros.get("nome") + "%");
			}

			if (parametros.containsKey("ativo")) {
				query.setParameter("ativo", parametros.get("ativo"));
			}

			if (parametros.containsKey("perfil")) {
				query.setParameter("perfil", parametros.get("perfil"));
			}
		}

		Long quantidade = (Long) query.getSingleResult();

		return quantidade != null ? quantidade.intValue() : 0;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaNomeComecandoPor(String nome)
			throws BusinessException {
		try {

			Query query = em.createNamedQuery(Usuario.NOME_COMECANDO_POR);
			query.setParameter("nome", nome);
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException(
					"Buscando todas as usuarios com nome começando por", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaComPerfil(Perfil perfil) throws BusinessException {
		try {

			Query query = em
					.createNamedQuery(Usuario.TODOS_USUARIOS_COM_PERFIL);
			query.setParameter("perfil", perfil);
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException(
					"Buscando todas as usuarios com determinado perfil", e);
		}
	}

	private Perfil buscaPerfilAdministrador() {

		Perfil perfil = null;

		Long id = new Long(
				parametroSingletonBean
						.buscarValorParametroAsInteger(ParametrosEnum.ID_PERFIL_ADMINISTRADOR));
		if (id != null) {
			perfil = perfilSession.buscarPorId(id);
		}

		return perfil;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usuario buscaPorLogin(String login) throws BusinessException {

		Usuario usuario = null;

		if (login == null) {
			return null;
		}

		try {

			Query query = em.createNamedQuery(Usuario.POR_LOGIN);
			query.setParameter("login", login);

			List<Usuario> usuarios = query.getResultList();
			if (!usuarios.isEmpty()) {
				usuario = usuarios.get(0);
			}

			// Para forçar a carga dos perfis
			if (usuario != null) {
				usuario.getPerfis().isEmpty();
			}

			Perfil perfilAdministrador = buscaPerfilAdministrador();
			if (perfilAdministrador != null) {
				if (usuario.getPerfis().contains(perfilAdministrador)) {
					usuario.setAdministrador(true);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(
					"Buscando todas as usuarios com login igual ao informado",
					e);
		}
		return usuario;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerfilPermissao> buscarPermissoes(Usuario usuario)
			throws BusinessException {

		StringBuilder queryString = new StringBuilder(
				"SELECT e.permissoes FROM Perfil as e WHERE :usuario MEMBER OF e.usuarios AND e.ativo = true ");

		List<PerfilPermissao> permissoes = em
				.createQuery(queryString.toString())
				.setParameter("usuario", usuario).getResultList();

		return permissoes;
	}

	@Override
	public boolean temPermissao(String viewId) throws BusinessException {

		Principal principal = context.getCallerPrincipal();

		if (principal == null) {
			return false;
		}

		StringBuilder queryString = new StringBuilder("select count(*) ");
		queryString
				.append("from usuario as u, perfil_permissao as pp, perfil_usuario as pu ");
		queryString.append("where pp.perfil_id = pu.perfil_id ");
		queryString.append("and pu.usuario_id = u.id ");
		queryString.append("and u.login = :login ");
		queryString.append("and pp.permissao_id = :viewId");

		Query q = em.createNativeQuery(queryString.toString());
		q.setParameter("login", principal.getName());
		q.setParameter("viewId", viewId);

		BigInteger quantidade = (BigInteger) q.getSingleResult();

		return (quantidade != null && quantidade.intValue() > 0);

	}

	@Override
	public String geraNovaSenha(Usuario usuario) throws BusinessException {
		String novaSenha = SenhaUtils.geradorSenha();
		return novaSenha;
	}

	@Override
	public String criptografarNovaSenha(String login, String novaSenha)
			throws BusinessException {
		String crypt = StringUtils.getHash(login + novaSenha);
		return crypt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaTodosDaEmpresa(Empresa empresa)
			throws BusinessException {

		try {

			Query query;
			query = em.createNamedQuery(Usuario.TODOS_USUARIOS_EMPRESA);
			query.setParameter("empresa", empresa);

			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException(
					"Buscando todos os usuários da empresa", e);
		}
	}
}
