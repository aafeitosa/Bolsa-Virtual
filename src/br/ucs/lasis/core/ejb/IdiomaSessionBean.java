package br.ucs.lasis.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Arquivo;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Stateless
// @Interceptors(value = PersistenceExceptionInterceptor.class)
public class IdiomaSessionBean implements IdiomaSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;

	@Override
	public void salvar(Idioma idioma) throws BusinessException {
		try {
			if (idioma.getId() == null) {
				em.persist(idioma);
			} else {
				em.merge(idioma);
				em.getEntityManagerFactory().getCache()
						.evict(Idioma.class, idioma.getId());
				if (idioma.getArquivo() != null) {
					em.getEntityManagerFactory().getCache()
							.evict(Arquivo.class, idioma.getArquivo().getId());
				}
			}
		} catch (Exception e) {
			throw new BusinessException(this.getClass().getName(), e);
		}
	}

	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, Idioma.class);

	}

	@Override
	public Idioma buscarPorId(long id) throws BusinessException {
		try {
			Idioma idioma = em.find(Idioma.class, id);
			return idioma;
		} catch (Exception e) {
			throw new BusinessException("Buscando Idioma", e);
		}
	}

	@Override
	public Idioma buscaPorLocale(String locale) throws BusinessException {
		Query query = em.createNamedQuery(Idioma.POR_LOCALE);
		query.setParameter("locale", locale);
		return (Idioma) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Idioma> buscaTodos() throws BusinessException {
		return em.createNamedQuery(Idioma.TODOS_IDIOMAS).getResultList();
	}

	@Override
	public PagedResult<Idioma> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException {

		return em.findPageWithQuery(
				"select e from Idioma as e order by e.nome",
				gridLazyLoaderDTO.getFilters(), Idioma.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}
}