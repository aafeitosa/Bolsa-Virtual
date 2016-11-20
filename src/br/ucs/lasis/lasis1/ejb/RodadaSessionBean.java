package br.ucs.lasis.lasis1.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import br.ucs.lasis.core.ejb.ParametrosSingletonBean;
import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.EntityManagerExtended;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.DataRepository;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Investimento;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.model.entity.RodadaVariacao;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.PeriodoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Stateless
// @Interceptors(PersistenceException.class)
public class RodadaSessionBean implements RodadaSession {

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
	@UsuarioLogado
	private Usuario usuarioLogado;

	@Inject
	@PeriodoCorrente
	private Periodo periodoCorrente;

	@Inject
	@PerfilCoordenador
	private Perfil perfilCoordenador;

	@EJB
	private PeriodoSession periodoSession;

	@EJB
	private GrupoSession grupoSession;
	
	@Inject
	private ParametrosSingletonBean parametroSingletonBean;
	
	@Override
	public Rodada buscaPorId(Long id) {
		return em.find(Rodada.class, id);
	}

	@Override
	public List<Rodada> buscaTodos() {
		return this.buscaTodos(null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rodada> buscaTodos(Integer start, Integer limit) {

		try {

			Query query;

			query = em.createNamedQuery(Rodada.TODAS_RODADAS);

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
	public List<Rodada> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) {

		try {

			Query query;

			query = em.createNamedQuery(Rodada.TODAS_RODADAS);

			if (start != null && limit != null) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new BusinessException("Buscando todos os rodadas", e);
		}
	}

	@Override
	public int getQuantidadeTotal(Map<String, Object> parametros) {
		try {
			Query query;

			query = em.createNamedQuery(Rodada.CONTA_TODAS);

			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os rodadas", e);
		}
	}

	@Override
	public int getQuantidadeTotal() {
		try {
			Query query;
			query = em.createNamedQuery(Rodada.CONTA_TODAS);
			int quantidade = ((Long) query.getSingleResult()).intValue();
			return quantidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Contando todos os grupos", e);
		}
	}

	@Override
	public void exclui(Long id) throws BusinessException {
		try {

			// Query q =
			// em.createNamedQuery(Investimento.INVESTIMENTOS_DA_RODADA);
			// q.setParameter("rodada", rodada);

			Rodada rodada = em.find(Rodada.class, id);
			em.remove(rodada);

			// em.remove(id, Rodada.class);
		} catch (PersistenceException p) {
			throw new BusinessException(
					"Não foi possível excluir o registro selecionado!");
		}
	}

	@Override
	public void salva(Rodada rodada) {

		if (rodada.getPeriodo() == null) {
			Periodo periodo = periodoSession.buscaPorData(new Date());
			if (periodo != null) {
				rodada.setPeriodo(periodo);
			}
		}
		em.merge(rodada);
		if (rodada.getId() != null) {
			salvaResultadosDaRodada(rodada);
		}

	}

	@Override
	public PagedResult<Rodada> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException {

		return em.findPageWithQuery(
				"select e from Rodada as e order by e.dataInicio",
				gridLazyLoaderDTO.getFilters(), Rodada.class,
				gridLazyLoaderDTO.getFirst(), gridLazyLoaderDTO.getPageSize());
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer getMaiorNumeroNaData(Date data, Grupo grupo) {

		Date dataInicio = (Date) data.clone();
		dataInicio.setHours(0);
		dataInicio.setMinutes(0);
		dataInicio.setSeconds(0);
		Date dataFim = (Date) data.clone();
		dataFim.setHours(23);
		dataFim.setMinutes(59);
		dataFim.setSeconds(59);
		Query q = em.createNamedQuery(Rodada.MAIOR_NUMERO_NA_DATA_E_GRUPO);
		q.setParameter("dataInicio", dataInicio);
		q.setParameter("dataFim", dataFim);
		q.setParameter("grupo", grupo);
		Integer numero = (Integer) q.getSingleResult();
		return numero;
	}

	public Integer getProximoNumeroDoGrupo(Grupo grupo) {
		Query q = em.createNamedQuery(Rodada.MAIOR_NUMERO_DO_GRUPO);
		q.setParameter("grupo", grupo);
		Integer numero = (Integer) q.getSingleResult();
		if (numero == null) {
			numero = 1;
		} else {
			numero++;
		}
		return numero;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> buscaGruposDoUsuario() {

		try {
			Query query;
			if (usuarioLogado.isAdministrador()
					|| usuarioLogado.getPerfis().contains(perfilCoordenador)) {
				//query = em.createNamedQuery(Grupo.TODOS_GRUPOS);
				query = em.createNamedQuery(Grupo.TODOS_GRUPOS_DO_PERIODO);
				query.setParameter("periodo", periodoCorrente);
//				System.out.println("buscaGruposDoUsuario() " + periodoCorrente);
			} else {
				query = em.createNamedQuery(Grupo.TODOS_GRUPOS_COM_PROFESSOR);
				query.setParameter("professor", usuarioLogado);
				query.setParameter("periodo", periodoCorrente);
//				System.out.println("buscaGruposDoUsuario() " + usuarioLogado + " " + periodoCorrente);
			}

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Buscando os grupos do usuário", e);
		}
	}

	public List<Investimento> buscaInvestimentosDaRodadaEEmpresa(Rodada rodada,
			Empresa empresa) {

		List<Investimento> investimentos = new ArrayList<Investimento>();

		List<Empresa> empresas = grupoSession
				.buscaOutrasEmpresasDoGrupo(empresa);
		for (Empresa empresa2 : empresas) {
			Investimento investimentoNovo = new Investimento();
			investimentoNovo.setInvestidora(empresa);
			investimentoNovo.setInvestida(empresa2);
			investimentoNovo.setRodada(rodada);
			Investimento investimento = this
					.buscaInvestimentoAnterior(investimentoNovo);
			if (investimento != null) {
				investimentos.add(investimento);
			} else {
				investimentos.add(investimentoNovo);
			}
		}

		return investimentos;
	}

	public void salvaInvestimentos(List<Investimento> investimentos)
			throws BusinessException {

		// for (Investimento investimento : investimentos) {
		// System.out.println(investimento);
		// }

		if (investimentos != null && !investimentos.isEmpty()) {

			for (Investimento investimento : investimentos) {
				if (investimento.getValor() == null) {
					Investimento velho = this
							.buscaInvestimentoAnterior(investimento);
					if (velho != null) {
						em.remove(velho.getId(), Investimento.class);
					}
				} else {
					em.merge(investimento);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Investimento buscaInvestimentoAnterior(Investimento novo) {
		Investimento velho = null;

		Query q = em
				.createNamedQuery(Investimento.INVESTIMENTOS_DA_RODADA_E_EMPRESA_E_INVESTIDA);
		q.setParameter("rodada", novo.getRodada());
		q.setParameter("empresa", novo.getInvestidora());
		q.setParameter("investida", novo.getInvestida());
		List<Investimento> investimentos = q.getResultList();
		if (!investimentos.isEmpty()) {
			velho = investimentos.get(0);
		}
		return velho;
	}

	// A Bolsa de Valores terá o seguinte processo. Inicialmente cada empresa
	// recebe 100.000,00 dinheiros
	// para aplicar em outras três empresas. Não é permitido aplicar na sua
	// própria empresa.
	// Há dois valores a serem considerados na Bolsa de Valores, o valor
	// recebido dos investidores e o
	// valor aplicado nas empresas. O valor recebido é multiplicado pelo seu
	// próprio índice.
	// O valor investido é multiplicado pelo índice da empresa na qual investiu.
	// A soma entre os investimentos recebidos multiplicado pelo seu índice e a
	// soma dos investimentos feitos
	// multiplicado pelo índice específicos de cada empresa formarão o resultado
	// final da empresa.
	// Será permitido que os participantes façam até quatro rodadas de
	// investimento para melhorar
	// sua posição na bolsa. As empresas que tiverem o melhor resultado recebem
	// 3 pontos a demais receberão
	// proporcionalmente ao resultado da melhor empresa. O melhor resultado é
	// obtido pela soma dos investimentos
	// feitos e recebidos considerados os índices de cada empresa.
	// Como existem índices negativos, poderá acontecer da empresa ficar
	// “devendo”. Caso isto ocorra, os
	// membros da empresa deverão pagar o saldo devedor com a nota das outras
	// atividades.
	private void calculaESalvaResultadosVelho(Rodada rodada) {

		// System.out.println("Calcula e salva resultados");

		// calcula o resultado de cada empresa do grupo na rodada
		Grupo grupo = rodada.getGrupo();
		grupo = em.find(Grupo.class, grupo.getId());

		rodada = em.find(Rodada.class, rodada.getId());

		Set<Investimento> investimentos = rodada.getInvestimentos();

		Set<Empresa> empresas = grupo.getEmpresas();

		for (Empresa empresa : empresas) {

			Double resultado = 0d;
			Double valoresRecebidos = 0d;
			Double valoresInvestidos = 0d;

			// System.out
			// .println("-----------------------------------------------------");
			//
			// System.out.println(empresa.getNome());
			//
			// System.out
			// .println("-----------------------------------------------------");

			for (Investimento investimento : investimentos) {
				if (investimento.getInvestida().equals(empresa)) {
					// System.out.println("recebeu " + investimento.getValor()
					// + " de " + investimento.getInvestidora().getNome());
					valoresRecebidos += investimento.getValor().doubleValue()
							* empresa.getIndice().doubleValue();
				}
			}

			for (Investimento investimento : investimentos) {
				if (investimento.getInvestidora().equals(empresa)) {
					// System.out.println("Investiu " + investimento.getValor()
					// + " em " + investimento.getInvestida().getNome());
					valoresInvestidos += investimento.getValor().doubleValue()
							* investimento.getInvestida().getIndice()
									.doubleValue();
				}
			}

			resultado = valoresInvestidos + valoresRecebidos;

			// System.out.println("Investiu " + valoresInvestidos);
			// System.out.println("Recebeu " + valoresRecebidos);
			// System.out.println("Resultado " + resultado);

			ResultadoRodada resultadoRodada = new ResultadoRodada();
			resultadoRodada.setEmpresa(empresa);
			resultadoRodada.setRodada(rodada);
			resultadoRodada.setResultado(new BigDecimal(resultado));

			// System.out.println("Merge da empresa " + empresa);
			em.merge(resultadoRodada);

		}
	}

	private void calculaESalvaResultados(Rodada rodada) {

		// if(rodada.isEncerrada()) {
		// return;
		// }

		// System.out.println("Calcula e salva resultados");

		// calcula o resultado de cada empresa do grupo na rodada
		Grupo grupo = rodada.getGrupo();
		grupo = em.find(Grupo.class, grupo.getId());

		rodada = em.find(Rodada.class, rodada.getId());

		// System.out.println("Variações");
		Set<RodadaVariacao> variacoes = rodada.getVariacoes();
		// for (RodadaVariacao rodadaVariacao : variacoes) {
		// System.out.println(rodadaVariacao);
		// }

		Set<Investimento> investimentos = rodada.getInvestimentos();

		Set<Empresa> empresas = grupo.getEmpresas();

		for (Empresa empresa : empresas) {

			Double resultado = 0d;
			Double valoresRecebidos = 0d;
			Double valoresInvestidos = 0d;

			// System.out
			// .println("-----------------------------------------------------");
			//
			// System.out.println(empresa.getNome());
			//
			// System.out
			// .println("-----------------------------------------------------");

			for (Investimento investimento : investimentos) {
				if (investimento.getInvestida().equals(empresa)) {
					// System.out.println("recebeu " + investimento.getValor()
					// + " de " + investimento.getInvestidora().getNome());
					valoresRecebidos += investimento.getValor().doubleValue()
							* empresa.getIndice().doubleValue();

					float desempenhoSetor = 1f;
					for (RodadaVariacao rodadaVariacao : variacoes) {
						if (rodadaVariacao.getRamoAtividade().equals(
								empresa.getRamoAtividade())) {
							// System.out.println(empresa.getRamoAtividade()
							// .getNome());
							desempenhoSetor = rodadaVariacao
									.getMultiplicadorVariacao();
							break;
						}
					}

					// System.out.println("desempenho investidora "
					// + desempenhoSetor);

					valoresRecebidos *= desempenhoSetor;

				}
			}

			for (Investimento investimento : investimentos) {
				if (investimento.getInvestidora().equals(empresa)) {
					// System.out.println("Investiu " + investimento.getValor()
					// + " em " + investimento.getInvestida().getNome());
					valoresInvestidos += investimento.getValor().doubleValue()
							* investimento.getInvestida().getIndice()
									.doubleValue();

					float desempenhoSetor = 1f;
					for (RodadaVariacao rodadaVariacao : variacoes) {
						if (rodadaVariacao.getRamoAtividade().equals(
								investimento.getInvestida().getRamoAtividade())) {
							// System.out.println(investimento.getInvestida()
							// .getRamoAtividade().getNome());
							desempenhoSetor = rodadaVariacao
									.getMultiplicadorVariacao();
							break;
						}
					}

					// System.out
					// .println("desempenho investida" + desempenhoSetor);

					valoresInvestidos *= desempenhoSetor;
				}
			}

			resultado = valoresInvestidos + valoresRecebidos;

			// System.out.println("Investiu " + valoresInvestidos);
			// System.out.println("Recebeu " + valoresRecebidos);
			// System.out.println("Resultado " + resultado);

			ResultadoRodada resultadoRodada = new ResultadoRodada();
			resultadoRodada.setEmpresa(empresa);
			resultadoRodada.setRodada(rodada);
			resultadoRodada.setResultado(new BigDecimal(resultado));

			// System.out.println("Merge da empresa " + empresa);
			em.merge(resultadoRodada);

		}

		atualizaIndiceAtratividade(rodada);

	}

	private void atualizaIndiceAtratividade(Rodada rodada) {
		System.out.println("ATUALIZA ATRATIVIDADE");
		
		List<ResultadoRodada> resultados = this.buscaResultadosDaRodada(rodada);

		BigDecimal valorTotal = new BigDecimal(0);
		for (ResultadoRodada resultadoRodada : resultados) {
			 System.out.println(resultadoRodada);
			valorTotal = valorTotal.add(resultadoRodada.getResultado());
		}

        // Obtém o percentual a ser dado pelo bônus de participação		
		Integer bonus = parametroSingletonBean.buscarValorParametroAsInteger(ParametrosEnum.BONUS_PERCENTUAL_PARTICIPACAO);
		if(bonus==null) {
			bonus = 0;
		}
		System.out.println("Valor Total : " + valorTotal);
		System.out.println("Valor do bônus : " + bonus);

		if (valorTotal.doubleValue()!=0) {
			System.out.println("Valor Total " + valorTotal);
			for (ResultadoRodada resultadoRodada : resultados) {
				System.out.println("era   " + resultadoRodada);
				
				BigDecimal indiceAtratividade = resultadoRodada
						.getResultado().divide(valorTotal, 2,
								RoundingMode.HALF_UP);
				
				resultadoRodada.setIndiceAtratividade(indiceAtratividade);
				System.out.println("Índice Atratividade : " + indiceAtratividade);
				
				indiceAtratividade = indiceAtratividade.multiply(new BigDecimal(bonus)).divide(new BigDecimal(100));
				System.out.println("Índice Atratividade depois : " + indiceAtratividade);
				// TODO O cálculo do bônus acontece aqui :
				
				indiceAtratividade = indiceAtratividade.add(new BigDecimal(1));
				System.out.println("Índice Atratividade mais 1 : " + indiceAtratividade);
//				Era :
//				BigDecimal resultado = resultadoRodada.getResultado().multiply(
//								resultadoRodada.getIndiceAtratividade().add(
//										new BigDecimal(1)));

//				Ficou : 
				BigDecimal resultado = resultadoRodada.getResultado().multiply(indiceAtratividade);
				
				resultadoRodada.setResultado(resultado);
				
				
				System.out.println("ficou " + resultadoRodada);
			}

			Collections.sort(resultados, new Comparator<ResultadoRodada>() {

				@Override
				public int compare(ResultadoRodada o1, ResultadoRodada o2) {
					return o2.getIndiceAtratividade().compareTo(
							o1.getIndiceAtratividade());
				}

			});

			int posicao = 1;
			for (ResultadoRodada resultadoRodada : resultados) {
				resultadoRodada.setPosicao(posicao++);
				em.merge(resultadoRodada);
			}
		}
	}

	// Busca resultados da rodada. Se acha, exclui
	@SuppressWarnings("unchecked")
	private void apagaResultados(Rodada rodada) {
		Query q = em.createNamedQuery(ResultadoRodada.RESULTADOS_DA_RODADA);
		q.setParameter("rodada", rodada);
		List<ResultadoRodada> resultados = q.getResultList();
		for (ResultadoRodada resultadoRodada : resultados) {
			em.remove(resultadoRodada);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ResultadoRodada> buscaResultadosDaRodada(Rodada rodada) {
		// System.out
		// .println("-------------------------------------------------------------");
		// System.out.println("Buscando resultados da rodada " +
		// rodada.getId());
		Query q = em.createNamedQuery(ResultadoRodada.RESULTADOS_DA_RODADA);
		q.setParameter("rodada", rodada);
		List<ResultadoRodada> resultados = q.getResultList();
		// for (ResultadoRodada resultadoRodada : resultados) {
		// System.out.println(resultadoRodada.getEmpresa().getNome() + " "
		// + resultadoRodada.getResultado());
		// }
		// System.out
		// .println("-------------------------------------------------------------");
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<RodadaVariacao> buscaVariacoesDoGrupo(Grupo grupo) {
		// System.out
		// .println("-------------------------------------------------------------");
		// System.out.println("Buscando variacoes do grupo " + grupo.getId() +
		// " "
		// + grupo.getNome());
		Query q = em.createNamedQuery(RodadaVariacao.VARIACOES_DO_GRUPO);
		q.setParameter("grupo", grupo);
		List<RodadaVariacao> variacoes = q.getResultList();
		// for (RodadaVariacao variacao : variacoes) {
		// System.out.println(variacao.getRodada().getNumero() + " "
		// + variacao.getRamoAtividade().getNome() + " "
		// + variacao.getVariacao());
		// }
		// System.out
		// .println("-------------------------------------------------------------");
		return variacoes;
	}

	@SuppressWarnings("unchecked")
	public List<ResultadoRodada> buscaResultadosDoGrupo(Grupo grupo) {
		// System.out
		// .println("-------------------------------------------------------------");
		// System.out.println("Buscando resultados do grupo " + grupo.getId()
		// + " " + grupo.getNome());
		Query q = em.createNamedQuery(ResultadoRodada.RESULTADOS_DO_GRUPO);
		q.setParameter("grupo", grupo);
		List<ResultadoRodada> resultados = q.getResultList();
		// for (ResultadoRodada resultadoRodada : resultados) {
		// System.out.println(resultadoRodada.getRodada().getNumero() + " "
		// + resultadoRodada.getEmpresa().getNome() + " "
		// + resultadoRodada.getResultado());
		// }
		// System.out
		// .println("-------------------------------------------------------------");
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<ResultadoRodada> buscaResultadosDaEmpresa(Empresa empresa) {
		// System.out
		// .println("-------------------------------------------------------------");
		// System.out.println("Buscando resultados da empresa " +
		// empresa.getId()
		// + " " + empresa.getNome());
		Query q = em.createNamedQuery(ResultadoRodada.RESULTADOS_DA_EMPRESA);
		q.setParameter("empresa", empresa);
		List<ResultadoRodada> resultados = q.getResultList();
		// for (ResultadoRodada resultadoRodada : resultados) {
		// System.out.println(resultadoRodada.getRodada().getNumero() + " "
		// + resultadoRodada.getEmpresa().getNome() + " "
		// + resultadoRodada.getResultado());
		// }
		// System.out
		// .println("-------------------------------------------------------------");
		return resultados;
	}

	public void salvaResultadosDaRodada(Rodada rodada) {

		apagaResultados(rodada);

		if (rodada.isEncerrada()) {
			calculaESalvaResultados(rodada);
		}
	}

	@Override
	public List<RodadaVariacao> inicializaVariacoes(Grupo grupo) {

		if (grupo != null) {
			grupo = em.find(Grupo.class, grupo.getId());
		}

		// System.out.println("Inicializando GRUPO = " + grupo);

		List<RodadaVariacao> variacoes = new ArrayList<RodadaVariacao>();
		Set<RamoAtividade> ramos = new HashSet<RamoAtividade>();

		Set<Empresa> empresas = grupo.getEmpresas();

		// System.out.println("Empresas >> " + empresas);

		for (Empresa empresa : empresas) {
			// System.out.println(empresa.getRamoAtividade().getNome());
			ramos.add(empresa.getRamoAtividade());
		}

		for (RamoAtividade ramo : ramos) {
			variacoes.add(new RodadaVariacao(getVariacaoInicial(), ramo));
		}

		return variacoes;
	}

	public Set<RodadaVariacao> buscaVariacoesDaRodada(Rodada rodada) {

		if (rodada != null) {
			rodada = em.find(Rodada.class, rodada.getId());
			rodada.getVariacoes().isEmpty();
			return rodada.getVariacoes();
		} else {
			return new HashSet<RodadaVariacao>();
		}

	}

	private int getVariacaoInicial() {
		// Sortear um número entre -10 e 10
		Random gerador = new Random();
		int randomico = gerador.nextInt(21);
		randomico = 10 - randomico;
		return randomico;
	}

	public Rodada buscaRodadaAnterior(Rodada rodada) {
		if (rodada != null && rodada.getNumero() != null
				&& rodada.getNumero() > 1) {
			Query q = em.createNamedQuery(Rodada.RODADA_COM_NUMERO_E_GRUPO);
			q.setParameter("numero", rodada.getNumero() - 1);
			q.setParameter("grupo", rodada.getGrupo());
			return (Rodada) q.getSingleResult();
		} else {
			return null;
		}
	}

	public ResultadoRodada buscaResultadoRodada(Rodada rodada, Empresa empresa) {

		if (rodada != null && empresa != null) {

			Rodada rodadaAnterior = buscaRodadaAnterior(rodada);

			// System.out.println("RODADA ANTERIOR " + rodadaAnterior);
			// System.out.println("EMPRESA " + empresa);

			if (rodadaAnterior != null) {
				Query q = em
						.createNamedQuery(ResultadoRodada.RESULTADO_DA_RODADA_E_EMPRESA);
				q.setParameter("empresa", empresa);
				q.setParameter("rodada", rodadaAnterior);
				ResultadoRodada resultadoRodada = null;
				try {
					resultadoRodada = (ResultadoRodada) q.getSingleResult();
				} catch (NoResultException nre) {
					resultadoRodada = null;
				}

				// System.out.println("RESULTADO RODADA " + resultadoRodada);

				return resultadoRodada;
			}
		}
		return null;
	}

	public ResultadoRodada buscaUltimoResultado(Empresa empresa) {

		List<ResultadoRodada> resultados = this
				.buscaResultadosDaEmpresa(empresa);
		ResultadoRodada resultado = null;
		if (!resultados.isEmpty()) {
			resultado = resultados.get(resultados.size() - 1);
		}
		return resultado;
	}
}