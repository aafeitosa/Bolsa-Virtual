package br.ucs.lasis.core.ejb;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.session.RamoAtividadeSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Stateless
// @Interceptors(value = PersistenceExceptionInterceptor.class)
public class RamoAtividadeSessionBean implements RamoAtividadeSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;

	@Override
	public void salvar(RamoAtividade ramoAtividade) throws BusinessException {
		em.merge(ramoAtividade);
	}


	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, RamoAtividade.class);
	}

	@Override
	public RamoAtividade buscarPorId(long id) throws BusinessException {
		try {
			RamoAtividade ramoAtividade = em.find(RamoAtividade.class, id);
			return ramoAtividade;
		} catch (Exception e) {
			throw new BusinessException("Buscando Ramo de Atividade", e);
		}
	}

	@Override
	public List<RamoAtividade> buscaTodos() throws BusinessException {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RamoAtividade> buscaTodos(Integer start, Integer limit)
			throws BusinessException {

		try {

			Query query = em.createNamedQuery(RamoAtividade.TODOS_RAMOS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todas os ramos de atividades", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<RamoAtividade> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) throws BusinessException {

		StringBuilder queryString = new StringBuilder(
				"SELECT e FROM RamoAtividade as e");

		queryString.append(" WHERE 1 = 1");

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				queryString.append(" AND UPPER(e.nome) like UPPER(:nome)");
			}
		}

		queryString.append(" ORDER BY e.nome");

		Query query = em.createQuery(queryString.toString());

		if (parametros != null && parametros.size() > 0) {
			if (parametros.containsKey("nome")) {
				query.setParameter("nome", "%" + parametros.get("nome") + "%");
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
				"SELECT count(e) FROM RamoAtividade as e");
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
		}

		Long quantidade = (Long) query.getSingleResult();
		return quantidade != null ? quantidade.intValue() : 0;

	}

	@Override
	public int getQuantidadeTotal() throws BusinessException {

		try {
			Query query = em.createNamedQuery(RamoAtividade.QUANTOS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os ramos de atividade", e);
		}
	}
	
	@Override
	public PagedResult<RamoAtividade> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException {

		return em.findPageWithQuery("select e from RamoAtividade as e order by e.nome",
				gridLazyLoaderDTO.getFilters(), RamoAtividade.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}


}
