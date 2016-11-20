package br.ucs.lasis.core.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.model.Parametro;
import br.ucs.lasis.core.model.dto.ParametroDTO;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.session.ParametroSession;

@Stateless
// @Interceptors(value = PersistenceExceptionInterceptor.class)
public class ParametroSessionBean implements ParametroSession {

	@Inject
	@DataRepository
	EntityManagerExtended em;
	
	@Inject
	private Event<Parametro> parametroEvent;

	@Override
	public void salvar(ParametroDTO parametroDTO) {

		if (parametroDTO == null) {
			return;
		}

		Parametro parametro = buscaPorTipo(parametroDTO.getParametrosEnum());
		if (parametro == null) {
			parametro = new Parametro(parametroDTO.getParametrosEnum(),
					parametroDTO.getValor());
			em.persist(parametro);
		} else {
			parametro.setValor(parametroDTO.getValor());
			em.merge(parametro);
		}
		
		parametroEvent.fire(parametro);

	}

	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, Parametro.class);
	}

	@Override
	public Parametro buscarPorId(long id) throws BusinessException {
		try {
			Parametro parametro = em.find(Parametro.class, id);
			return parametro;
		} catch (Exception e) {
			throw new BusinessException("Buscando Parametro", e);
		}
	}

	@Override
	public Parametro buscaPorTipo(ParametrosEnum tipo) throws BusinessException {
		
		Query query = em.createNamedQuery(Parametro.POR_TIPO);
		query.setParameter("tipo", tipo);
		Parametro parametro = null;
		try {
			parametro = (Parametro) query.getSingleResult();
		} catch (NoResultException nre) {
			// NOP
		}
		
		return parametro;
	}

	@Override
	public List<ParametroDTO> buscaTodosDTO() throws BusinessException {

		List<Parametro> parametros;
		List<ParametroDTO> parametrosDTO = new ArrayList<>();

		parametros = this.buscaTodos();

		for (ParametrosEnum item : ParametrosEnum.values()) {
			ParametroDTO dto = new ParametroDTO(item);
			for (Parametro parametro : parametros) {
				if (parametro.getTipo().equals(item)) {
					dto.setValor(parametro.getValor());
				}
			}
			parametrosDTO.add(dto);
		}

		return parametrosDTO;
	}

	@Override
	public List<Parametro> buscaTodos() throws BusinessException {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Parametro> buscaTodos(Integer start, Integer limit)
			throws BusinessException {

		try {

			Query query = em.createNamedQuery(Parametro.TODOS_PARAMETROS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todas os parâmetros", e);
		}
	}

	@Override
	public int getQuantidadeTotal() throws BusinessException {

		try {
			Query query = em.createNamedQuery(Parametro.QUANTOS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todas os parâmetros", e);
		}
	}

}
