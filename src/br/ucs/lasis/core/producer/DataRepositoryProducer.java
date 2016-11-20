package br.ucs.lasis.core.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.EntityManagerFacade;
import br.ucs.lasis.core.qualifier.DataRepository;

@ApplicationScoped
public class DataRepositoryProducer {

	@PersistenceContext
	private EntityManager entityManager;

	@Produces
	@DataRepository
	public EntityManagerExtended getEntityManager() {
		return new EntityManagerFacade(entityManager);
	}

}
