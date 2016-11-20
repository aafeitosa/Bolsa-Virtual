package br.ucs.lasis.core.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Traducao;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.session.TraducaoSession;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Stateless
public class TraducaoSessionBean implements TraducaoSession {

	@Inject
	@DataRepository
	private EntityManagerExtended em;
	
	@Inject
	private IdiomaSession idiomaSession;
	
	@Inject
	private ParametrosSingletonBean parametros;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> findMapaTraducoes(Idioma idioma) {
		
		Map<String, String> translationMap = new HashMap<>();
		Query q = em.createNamedQuery(Traducao.TODOS_POR_IDIOMA);
		q.setParameter("idioma", idioma);
		List<Traducao> results = q.getResultList();
		for (Traducao traducao : results) {
			String valorTraducao = traducao.getTraducao();
			if (valorTraducao == null) {
				valorTraducao = traducao.getCodigo();
			}
			translationMap.put(traducao.getCodigo(), valorTraducao);
		}

		return translationMap;
	}

	@Override
	public PagedResult<Traducao> findTranslationsWith(
			GridLazyLoaderDTO gridLazyLoaderDTO) {
		
		return em.findPageWithQuery("select e from Traducao as e order by e.codigo", gridLazyLoaderDTO.getFilters(), Traducao.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}

	@Override
	public void update(Traducao traducao) throws BusinessException {
		
		if(traducao==null) {
			throw new BusinessException("Tradução nula");
		} 
		
		try {
			if(traducao.getId()==null) {
				em.persist(traducao);
			} else {
				em.merge(traducao);
			}
		} catch (Exception e) {
			throw new BusinessException("Alterando Traducao", e);
		}
	}
	
	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, Traducao.class);
	}
	

	@Override
	public Traducao findById(Long primaryKey) {
		try {
			Traducao traducao = em.find(Traducao.class, primaryKey);
			return traducao;
		} catch (Exception e) {
			throw new BusinessException("Buscando Traducao", e);
		}
	}

	@Override
	public Traducao findByCodigoEIdioma(String codigo, Idioma idioma) {
		Query query = em.createNamedQuery(Traducao.TODOS_POR_CODIGO_E_IDIOMA);
		query.setParameter("codigo", codigo);
		query.setParameter("idioma", idioma);
		return (Traducao) query.getSingleResult();
	}

	@Override
	public Map<String, String> findMapaTraducoes(String locale) {
		Idioma idioma = idiomaSession.buscaPorLocale(locale);
		return findMapaTraducoes(idioma);
	}

	@Override
	public Idioma getIdiomaPadrao() throws BusinessException {
		Long id = parametros.buscarValorParametroAsId(ParametrosEnum.ID_IDIOMA_PADRAO);
		if(id!=null) {
			return idiomaSession.buscarPorId(id);
		}
		return null;
	}
}