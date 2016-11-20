package br.ucs.lasis.core.jpa;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import br.ucs.lasis.core.model.Idioma;

public class QueryExecutor<E> {

	/*
	 * ========================================================================
	 * Atributos
	 * ========================================================================
	 */

	private EntityManager em;
	private QueryType queryType;
	private String queryName;
	private String sql;
	@SuppressWarnings("rawtypes")
	private Class entityClass;
	private Integer start;
	private Integer limit;
	@SuppressWarnings("rawtypes")
	private Map parameters;
	private String resultSetMapping;

	/*
	 * ========================================================================
	 * Construtores
	 * ========================================================================
	 */

	@SuppressWarnings("rawtypes")
	public QueryExecutor(EntityManager em, QueryType queryType,
			String queryNameOrSQL, Class entityClass, Integer start,
			Integer limit, Map parameters) {
		this(em, queryType, queryNameOrSQL, entityClass);
		this.start = start;
		this.limit = limit;
		this.parameters = parameters;
	}

	@SuppressWarnings("rawtypes")
	public QueryExecutor(EntityManager em, QueryType queryType,
			String queryNameOrSQL, Class entityClass) {
		super();
		this.em = em;
		this.queryType = queryType;
		this.entityClass = entityClass;
		if (queryType.equals(QueryType.Named)) {
			this.queryName = queryNameOrSQL;
		} else {
			this.sql = queryNameOrSQL;
		}
	}

	public QueryExecutor(EntityManager em, QueryType queryType,
			String queryNameOrSQL) {
		this(em, queryType, queryNameOrSQL, null);
	}

	/*
	 * ========================================================================
	 * Getters e Setters
	 * ========================================================================
	 */

	public String getQueryName() {
		return queryName;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@SuppressWarnings("rawtypes")
	public Map getParameters() {
		return parameters;
	}

	@SuppressWarnings("rawtypes")
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public String getResultSetMapping() {
		return resultSetMapping;
	}

	public void setResultSetMapping(String resultSetMapping) {
		this.resultSetMapping = resultSetMapping;
	}

	/*
	 * ========================================================================
	 * Métodos Públicos
	 * ========================================================================
	 */

	@SuppressWarnings({ "unchecked" })
	public List<E> getList() {

		// Cria a query
		Query query = null;
		switch (queryType) {

		case Native:

			if (resultSetMapping == null || resultSetMapping.isEmpty()) {
				query = em.createNativeQuery(sql, entityClass);
			} else {
				query = em.createNativeQuery(sql, resultSetMapping);
			}

			applyParametersNativeQuery(query);

			break;

		case Named:

			query = em.createNamedQuery(queryName);

			applyParameters(query);

			break;

		case Custom:

			// TODO alterar aqui

			query = prepareQuery(QueryOption.Fetch);

			break;

		}

		// Verifica parâmetros de paginação
		if (start != null) {
			query.setFirstResult(start);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}

		// Consulta
		List<E> entidades = query.getResultList();

		// Retorna
		return entidades;
	}

	@SuppressWarnings("unchecked")
	private Query prepareQuery(QueryOption option) {
		
//		System.out.println("--------------------------------------------------------");

//		System.out.println(sql);
		
		String upperSql = sql.toUpperCase();
		String queryStart = null;
		String orderBy = null;
		if (upperSql.contains("ORDER BY")) {
			queryStart = sql.substring(0, upperSql.indexOf("ORDER BY"));
			orderBy = sql.substring(upperSql.indexOf("ORDER BY"));
		} else {
			queryStart = sql;
		}
		
		if(QueryOption.Count.equals(option)) {
			String alias = getSelectAlias();
			queryStart = queryStart.replace(selectClauseToReplace(), "SELECT COUNT("
						+ alias + ") ");
//			System.out.println("Prepara para contar");
		}
//		else {
//			System.out.println("Prepara para buscar");
//		}

		StringBuilder sb = new StringBuilder(queryStart);

		// inclui parametros
		if (parameters != null && !parameters.isEmpty()) {
			sb.append(" WHERE 1 = 1");

			Set<String> names = parameters.keySet();
			for (String name : names) {
				Object value = parameters.get(name);
				if (value != null) {
					if (value instanceof String) {
						sb.append(" AND UPPER(e." + name + ") like UPPER(:"
								+ name + ")");
					} else {
						sb.append(" AND e." + name + " = :" + name);
					}
				}
			}
		}

		if (orderBy != null && !QueryOption.Count.equals(option)) {
			sb.append(" " + orderBy);
		}
		
//		System.out.println("QUERY : " + sb.toString());

		Query query = em.createQuery(sb.toString());

		// parametriza
		if (parameters != null && !parameters.isEmpty()) {

			Set<String> names = parameters.keySet();
			for (String name : names) {
				Object value = parameters.get(name);
				if (value != null) {
					if (value instanceof String) {
						query.setParameter(name, "%"+value+"%");
					} else {
						query.setParameter(name, value);
					}
				}
			}
		}

		return query;
	}

	public QueryExecutor(String sql, Map<String,Object> parameters) {
		this.sql = sql;
		this.parameters = parameters;
	}

	public static void main(String[] args) {

		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("nome", "teste");
		parameters.put("idade", new Integer(25));
		
		QueryExecutor<Idioma> executor = new QueryExecutor<Idioma>(
				"select e FROM idioma as e order by e.nome", parameters);
		executor.prepareQuery(QueryOption.Fetch);
		executor.prepareQuery(QueryOption.Count);
	}

	public PagedResult<E> getPagedResult() {

		// Cria o resultado
		PagedResult<E> result = new PagedResult<E>();

		// Busca as entidades
		List<E> entidades = getList();

		// Define a página
		result.setPage(entidades);

		// Contagem de registros
		String sqlCount;
		Query query = null;
		switch (queryType) {

		case Native:

			String upperCase = sql.toUpperCase();

			if (upperCase.contains("UNION")) {
				StringBuilder queryCount = new StringBuilder();
				String[] querys = upperCase.split("UNION");

				queryCount.append(" SELECT SUM(RowNumber) FROM ( ");

				for (int i = 0; i < querys.length; i++) {
					String editQuery = querys[i];
					int indexOfFirstFrom = editQuery.indexOf("FROM");

					if (i != 0) {
						queryCount.append(" UNION ");
					}
					editQuery = "SELECT COUNT(*) RowNumber "
							+ editQuery.substring(indexOfFirstFrom);

					queryCount.append(editQuery);
				}
				queryCount.append(" ) ");
				upperCase = queryCount.toString();
			} else {
				int indexOfFirstFrom = upperCase.indexOf("FROM");
				upperCase = "SELECT COUNT(*) "
						+ upperCase.substring(indexOfFirstFrom);
			}

			query = em.createNativeQuery(upperCase);

			applyParametersNativeQuery(query);

			break;

		case Custom:
			
//			System.out.println("Contagem");

			query = prepareQuery(QueryOption.Count);

			break;

		case Named:
			// busca a anotacao "NamedQuery" da classe referenciada e troca o
			// "e" por "count(e)" para fazer o
			// count da query passada

			// armazena todas as anotacoes da classe referenciada
			Annotation[] annotations = entityClass.getAnnotations();

			// percorre todas as anotacoes que foram retornadas
			for (Annotation a : annotations) {

				// entra somente se a anotacao for NamedQuery
				if (a instanceof NamedQueries) {

					// armazena todas as NamedQueries
					NamedQuery[] namedQuery = ((NamedQueries) a).value();

					// percorre todas as NamedQueries
					for (NamedQuery named : namedQuery) {

						// altera a query de "SELECT e" por "SELECT COUNT(*)" da
						// query passada para retornar o Count
						// da mesma query
						if (queryName.equals(named.name())) {
							sql = named.query();
							sqlCount = sql.replace("SELECT e",
									"SELECT COUNT(e)");

							String upperSqlCount = sqlCount.toUpperCase();
							if (upperSqlCount.contains("ORDER BY")) {
								// Se tiver order by, tira
								sqlCount = sqlCount.substring(0,
										upperSqlCount.indexOf("ORDER BY"));
							}

							query = em.createQuery(sqlCount);
							break;

						}
					}
				}
			}

			applyParameters(query);

			break;

		}

		// Executa
		try {

			Object countValue = query.getSingleResult();

			if (countValue instanceof Long) {
				result.setTotalSize(((Long) countValue).intValue());
			} else {
				if (countValue instanceof BigDecimal) {
					result.setTotalSize(((BigDecimal) countValue).intValue());
				}
			}

		} catch (NonUniqueResultException e) {
			result.setTotalSize(query.getResultList().size());
		} catch (NoResultException f) {
			result.setTotalSize(0);
		}

		// Retorna
		return result;

	}

	private String selectClauseToReplace() {

		String[] split = sql.split("FROM");
		
		if(split.length < 2) {
			split = sql.split("from");
		}

		return split[0];

	}

	private String getSelectAlias() {

		String[] split = sql.split("FROM");

		if(split.length < 2) {
			split = sql.split("from");
		}
		
		String selectClause = split[0];

		String alias = null;

		if (selectClause.startsWith("SELECT DISTINCT")) {
			alias = selectClause.replace("SELECT DISTINCT", "");
		} else {
			alias = selectClause.replace("SELECT", "");
			alias = selectClause.replace("select", "");
		}

		if (alias.contains(".")) {
			return aliasFinder(alias, "\\.");
		}

		if (alias.contains(",")) {
			return aliasFinder(alias, ",");
		}

		return alias.trim();
	}

	private String aliasFinder(String string, String splitChar) {

		String[] aliasArray = string.split(splitChar);

		return aliasArray[0].trim();

	}

	/*
	 * ========================================================================
	 * Métodos Privados
	 * ========================================================================
	 */

	/**
	 * Aplica os parâmetros da consulta.
	 * 
	 * @param query
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void applyParameters(Query query) {
		if ((parameters != null) && (parameters.entrySet().size() > 0)) {
			Set<Entry> rawParameters = parameters.entrySet();
			for (Entry entry : rawParameters) {
				query.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
	}

	private void applyParametersNativeQuery(Query query) {
		if (parameters != null) {
			for (int i = 1; i <= parameters.size(); i++) {
				query.setParameter(i, parameters.get(String.valueOf(i)));
			}
		}
	}
}
