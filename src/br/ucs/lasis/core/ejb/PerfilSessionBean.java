package br.ucs.lasis.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.session.PerfilSession;

@Stateless
// @Interceptors(value = PersistenceExceptionInterceptor.class)
public class PerfilSessionBean implements PerfilSession {

	@Inject
	@DataRepository
	EntityManagerExtended em;
	
	@Override
	public void salvar(Perfil perfil) throws BusinessException {
		try {
			if (perfil.getId() == null) {
				em.persist(perfil);
			} else {
				em.merge(perfil);
			}
		} catch (Exception e) {
			throw new BusinessException(this.getClass().getName(), e);
		}
	}

	@Override
	public void exclui(Long id) throws BusinessException {
		em.remove(id, Perfil.class);
	}

	@Override
	public Perfil buscarPorId(long id) throws BusinessException {
		try {
			Perfil perfil = em.find(Perfil.class, id);
			//força carga das permissões
			perfil.getPermissoes().isEmpty();
			return perfil;
		} catch (Exception e) {
			throw new BusinessException("Buscando Perfil", e);
		}
	}

	@Override
	public List<Perfil> buscaTodos() throws BusinessException {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Perfil> buscaTodosExternos()
			throws BusinessException {

		try {

			Query query = em.createNamedQuery(Perfil.TODOS_PERFIS_EXTERNOS);

			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os perfis externos", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Perfil> buscaTodos(Integer start, Integer limit)
			throws BusinessException {

		try {

			Query query = em.createNamedQuery(Perfil.TODOS_PERFIS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os perfis", e);
		}
	}

	@Override
	public int getQuantidadeTotal() throws BusinessException {

		try {
			Query query = em.createNamedQuery(Perfil.QUANTOS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os perfis", e);
		}
	}

}
