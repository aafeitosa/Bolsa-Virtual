package br.ucs.lasis.lasis1.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.session.SistemaSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Stateless
public class PeriodoSessionBean implements PeriodoSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;

	@Inject
	@EmpresaLogada
	private Empresa empresaLogada;

	@Inject
	@EmpresaPrincipal
	private boolean isEmpresaPrincipal;
	
	@EJB
	private SistemaSession sistemaSession;

	@Override
	public Periodo buscaPorId(Long id) {
		return em.find(Periodo.class, id);
	}

	
	@Override
	public List<Periodo> buscaTodos() {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Periodo> buscaTodos(Integer start, Integer limit) {

		try {

			Query query;

			query = em.createNamedQuery(Periodo.TODOS_PERIODOS);

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
	public List<Periodo> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) {

		try {

			Query query;

			query = em.createNamedQuery(Periodo.TODOS_PERIODOS);
			
			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os períodos", e);
		}
	}

	@Override
	public int getQuantidadeTotal(Map<String, Object> parametros) {
		try {
			Query query;

			query = em.createNamedQuery(Periodo.CONTA_TODOS);

			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os períodos", e);
		}
	}

	@Override
	public int getQuantidadeTotal() {
		try {
			Query query;
			query = em.createNamedQuery(Periodo.CONTA_TODOS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os grupos", e);
		}
	}

	@Override
	public void exclui(Long id) {
		em.remove(id, Periodo.class);
	}

	@Override
	public void salva(Periodo periodo) {
		em.merge(periodo);
	}

	@Override
	public PagedResult<Periodo> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException {

		return em.findPageWithQuery(
				"select e from Periodo as e order by e.nome",
				gridLazyLoaderDTO.getFilters(), Periodo.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}


	@SuppressWarnings("unchecked")
	@Override
	public Periodo buscaPorData(Date date) {
		try {
			Query query;
			query = em.createNamedQuery(Periodo.PERIODO_NA_DATA);
			query.setParameter("data", date);
			List<Periodo> periodos = query.getResultList();
			if(periodos!=null && !periodos.isEmpty()) {
//				System.out.println("Encontrado periodo " + periodos.get(0));
				return periodos.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Buscando o peŕiodo na data " + date, e);
		}
//		System.out.println("Não achou período");
		return null;
	}

	
}
