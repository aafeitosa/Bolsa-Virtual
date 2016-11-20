package br.ucs.lasis.core.view.jsf.backing;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("dashboardBack")
@ViewScoped
public class DashboardBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;
	private Empresa empresa;
	private boolean isAluno = true;
	private List<Usuario> alunos;
	private List<ResultadoRodada> resultados;
	private ChartModel chartModelEvolucao;
	private ChartModel chartModelPosicao;
	private ChartModel chartModelParticipacao;
	private ResultadoRodada ultimoResultado;
	private Grupo grupo;
	private int nroMembrosGrupo;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	private RodadaSession rodadaSession;

	@Inject
	private GrupoSession grupoSession;
	
	@Inject
	@PeriodoCorrente
	private Periodo periodoCorrente;
	
	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	@Inject
	@PerfilCoordenador
	private Perfil perfilCoordenador;

	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;

	@PostConstruct
	public void init() {
		this.empresa = usuarioLogado.getEmpresa();
		this.grupo = this.grupoSession.buscaPrimeiroGrupoDaEmpresa(periodoCorrente, this.empresa);
		this.nroMembrosGrupo = this.grupoSession.contaEmpresasDoGrupo(grupo);
		this.resultados = rodadaSession.buscaResultadosDaEmpresa(empresa);
		ultimoResultado = buscaUltimoResultado();

		alunos = usuarioSession.buscaTodosDaEmpresa(this.empresa);
		checkPerfil();
		chartModelEvolucao = initLinearModelEvolucao();
		chartModelPosicao = initLinearModelPosicao();
		chartModelParticipacao = initLinearModelParticipacao();
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public boolean isAluno() {
		return isAluno;
	}

	public List<Usuario> getAlunos() {
		return this.alunos;
	}

	public ChartModel getChartModelEvolucao() {
		return chartModelEvolucao;
	}

	public ChartModel getChartModelPosicao() {
		return chartModelPosicao;
	}

	public ChartModel getChartModelParticipacao() {
		return chartModelParticipacao;
	}

	private void checkPerfil() {
		isAluno = usuarioLogado.getPerfis().contains(perfilCoordenador)
				|| usuarioLogado.getPerfis().contains(perfilProfessor);
	}

	private LineChartModel initLinearModelEvolucao() {
		LineChartModel model = new LineChartModel();

//		model.setTitle(getTranslation("evolucao_da_empresa"));
		model.setLegendPosition("nw");
		model.setShowPointLabels(true);
		model.getAxes().put(AxisType.X,
				new CategoryAxis(getTranslation("rodadas")));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel(getTranslation("resultado"));

		ChartSeries serie = new ChartSeries();

		serie = new ChartSeries();
		serie.setLabel(empresa.getNomeSemApostrofes());
		for (ResultadoRodada resultadoRodada : resultados) {
			serie.set(resultadoRodada.getRodada().getNumero(),
					resultadoRodada.getResultado());
		}
		model.addSeries(serie);

		return model;
	}

	private LineChartModel initLinearModelPosicao() {
		LineChartModel model = new LineChartModel();

//		model.setTitle(getTranslation("posicao_da_empresa"));
		model.setLegendPosition("nw");
		model.setShowPointLabels(true);
		model.getAxes().put(AxisType.X,
				new CategoryAxis(getTranslation("rodadas")));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setMax(0);
		yAxis.setMin(nroMembrosGrupo + 1);
		yAxis.setLabel(getTranslation("posicao"));
		yAxis.setTickFormat("%1d");
		
		ChartSeries serie = new ChartSeries();

		serie = new ChartSeries();
		serie.setLabel(empresa.getNomeSemApostrofes());
		for (ResultadoRodada resultadoRodada : resultados) {
			serie.set(resultadoRodada.getRodada().getNumero(),
					resultadoRodada.getPosicao());
		}
		model.addSeries(serie);

		return model;
	}

	private LineChartModel initLinearModelParticipacao() {
		LineChartModel model = new LineChartModel();

//		model.setTitle(getTranslation("participacao_da_empresa"));
		model.setLegendPosition("nw");
		model.setShowPointLabels(true);
		model.getAxes().put(AxisType.X,
				new CategoryAxis(getTranslation("rodadas")));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel(getTranslation("participacao"));

		ChartSeries serie = new ChartSeries();

		serie = new ChartSeries();
		serie.setLabel(empresa.getNomeSemApostrofes());
		for (ResultadoRodada resultadoRodada : resultados) {
			Double participacao = resultadoRodada.getIndiceAtratividade()
					.doubleValue() * 100;
			serie.set(resultadoRodada.getRodada().getNumero(), participacao);
		}
		model.addSeries(serie);

		return model;
	}

	public ResultadoRodada buscaUltimoResultado() {
		ResultadoRodada resultado = null;
		if (resultados != null && !resultados.isEmpty()) {
			resultado = resultados.get(resultados.size() - 1);
//			System.out.println(resultado);
		}
		return resultado;
	}

	public Integer getPosicao() {
		if (ultimoResultado != null) {
			return ultimoResultado.getPosicao();
		} else {
			return 0;
		}
	}

	public BigDecimal getResultado() {
		if (ultimoResultado != null) {
			return ultimoResultado.getResultado();
		} else {
			return new BigDecimal(0);
		}
	}

	public BigDecimal getParticipacao() {
		if (ultimoResultado != null) {
			BigDecimal participacao = ultimoResultado.getIndiceAtratividade();
			participacao = participacao.multiply(new BigDecimal(100));
			return participacao;
		} else {
			return new BigDecimal(0);
		}
	}

}