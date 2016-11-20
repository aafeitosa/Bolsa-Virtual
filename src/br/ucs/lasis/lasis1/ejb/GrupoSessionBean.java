package br.ucs.lasis.lasis1.ejb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.session.SistemaSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Stateless
public class GrupoSessionBean implements GrupoSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;

	@Inject
	@EmpresaLogada
	private Empresa empresaLogada;

	@Inject
	@EmpresaPrincipal
	private boolean isEmpresaPrincipal;

	@Inject
	@PeriodoCorrente
	private Periodo periodoCorrente;

	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;
	
	@EJB
	private RodadaSession rodadaSession;

	@EJB
	private SistemaSession sistemaSession;

	@Override
	public Grupo buscaPorId(Long id) {
		return em.find(Grupo.class, id);
	}

	@Override
	public Grupo buscaPorIdComEmpresas(Long id) {
		Grupo grupo = buscaPorId(id);
		grupo.getEmpresas().isEmpty();
		grupo.getProfessores().isEmpty();
		return grupo;
	}

	@Override
	public List<Grupo> buscaTodos() {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> buscaTodos(Integer start, Integer limit) {

		try {

			Query query;

			query = em.createNamedQuery(Grupo.TODOS_GRUPOS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os grupos", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) {

		try {

			Query query;

			query = em.createNamedQuery(Grupo.TODOS_GRUPOS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os grupos", e);
		}
	}

	@Override
	public int getQuantidadeTotal(Map<String, Object> parametros) {
		try {
			Query query;

			query = em.createNamedQuery(Grupo.CONTA_TODOS);

			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os grupos", e);
		}
	}

	@Override
	public int getQuantidadeTotal() {
		try {
			Query query;
			query = em.createNamedQuery(Grupo.TODOS_GRUPOS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os grupos", e);
		}
	}

	@Override
	public void exclui(Long id) {
		em.remove(id, Grupo.class);
	}

	@Override
	public void salva(Grupo grupo) {
		em.merge(grupo);
	}

	@Override
	public PagedResult<Grupo> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException {

		return em.findPageWithQuery("select e from Grupo as e order by e.nome",
				gridLazyLoaderDTO.getFilters(), Grupo.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Empresa> buscaEmpresasSemGrupo() {

		String sql = "select * from empresa as e where e.id not in (select ge.empresa_id from grupo_empresa ge) and e.ativo='t' order by e.nome";
		Query q = em.createNativeQuery(sql, Empresa.class);
		List<Empresa> empresas = q.getResultList();

		Empresa empresaPrincipal = sistemaSession.getEmpresaPrincipal();

		empresas.remove(empresaPrincipal);

		return empresas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscaProfessores() {

		Query q = em.createNamedQuery(Usuario.TODOS_USUARIOS_COM_PERFIL);
		q.setParameter("perfil", perfilProfessor);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Grupo buscaPrimeiroGrupoDaEmpresa(Periodo periodo, Empresa empresa) {
		Query q = em.createNamedQuery(Grupo.TODOS_GRUPOS_DO_PERIODO);
		q.setParameter("periodo", periodo);
		List<Grupo> grupos = q.getResultList();
		for (Grupo grupo : grupos) {
			if (grupo.getEmpresas().contains(empresa)) {
				return grupo;
			}
		}
		return null;
	}

	public List<Empresa> buscaOutrasEmpresasDoGrupo(Empresa empresa) {
		List<Empresa> empresas = new ArrayList<Empresa>();
		Grupo grupo = this
				.buscaPrimeiroGrupoDaEmpresa(periodoCorrente, empresa);
		if (grupo != null) {
			Set<Empresa> empresasDoGrupo = grupo.getEmpresas();
			for (Empresa empresa2 : empresasDoGrupo) {
				if(!empresa2.equals(empresa)) {
					empresas.add(empresa2);
				}
			}
			return empresas;
		} else {
			return new ArrayList<Empresa>();
		}
	}
	
	@Override
	public List<Grupo> buscaGruposDoUsuario() {
		return rodadaSession.buscaGruposDoUsuario();
	}
	
	public Set<Empresa> buscaEmpresasDoGrupo(Grupo grupo) {
		if(grupo==null) {
			return new HashSet<Empresa>();
		}
		Grupo g = em.find(Grupo.class, grupo.getId());
		g.getEmpresas().isEmpty();
		return g.getEmpresas();
	}
	
	public int contaEmpresasDoGrupo(Grupo grupo) {
		return buscaEmpresasDoGrupo(grupo).size();
	}
}
