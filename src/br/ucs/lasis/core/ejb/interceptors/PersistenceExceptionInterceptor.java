package br.ucs.lasis.core.ejb.interceptors;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import br.ucs.lasis.core.exceptions.BusinessException;

public class PersistenceExceptionInterceptor {

	@Resource
	private SessionContext sessionContext;

	@PersistenceContext
	private EntityManager em;

	@AroundInvoke
	public Object interceptor(InvocationContext ic) throws Exception {
		Object o = null;
		try {
			o = ic.proceed();
			if (!sessionContext.getRollbackOnly()) {
				em.flush();
			}
		} catch (PersistenceException ex) {

			Throwable t = ex.getCause();
			// aqui t é org.hibernate.exception.ConstraintViolationException

			// System.out.println(t.getClass().getName());

			t = t.getCause();
			// agora t deve ser
			// java.sql.SQLIntegrityConstraintViolationException

			// System.out.println(4);
			// System.out.println(t.getClass().getName());

			if ("org.postgresql.util.PSQLException".equals(t.getClass()
					.getName())) {
				throw new BusinessException("Erro de banco de dados : "
						+ t.getMessage());
			}

		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw e;
			} else {
				throw new BusinessException("Erro interno", e);
			}
		}
		return o;
	}

}
