package br.ucs.lasis.lasis1.view.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.comparator.RodadaComparator;
import br.ucs.lasis.lasis1.model.dto.ResultadoEmpresaDTO;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.model.entity.RodadaVariacao;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("resultadoFormBack")
@ViewScoped
public class ResultadoFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Grupo grupo;
	private Rodada rodada;
	private List<ResultadoRodada> resultados;
	private List<ResultadoEmpresaDTO> resultadosDtos;
	Set<RodadaVariacao> variacoes;
	List<RodadaVariacao> variacoesGrupo;

	private LineChartModel chartModel;
	private LineChartModel chartModelSetor;

	@Inject
	private GrupoSession grupoSession;

	@Inject
	private RodadaSession rodadaSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.grupo = grupoSession.buscaPorIdComEmpresas(id);
			this.resultados = this.rodadaSession
					.buscaResultadosDoGrupo(this.grupo);
			this.atualizaVariacoes();
			this.variacoesGrupo = this.rodadaSession
					.buscaVariacoesDoGrupo(grupo);
		}

		chartModel = this.initLinearModel();
		chartModelSetor = this.initLinearModelSetor();
		this.definePrimeiraPagina();
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public LineChartModel getChartModel() {
		return chartModel;
	}

	public LineChartModel getChartModelSetor() {
		return chartModelSetor;
	}

	public void setChartModel(LineChartModel chartModel) {
		this.chartModel = chartModel;
	}

	public List<ResultadoRodada> getResultados() {
		return resultados;
	}

	public void setResultados(List<ResultadoRodada> resultados) {
		this.resultados = resultados;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public List<ResultadoEmpresaDTO> getResultadosDtos() {
		return resultadosDtos;
	}

	public void setResultadosDtos(List<ResultadoEmpresaDTO> resultadosDtos) {
		this.resultadosDtos = resultadosDtos;
	}

	public String listagem() {
		return "listaResultados";
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		model.setTitle(getTranslation("evolucao_das_empresas"));
		model.setLegendPosition("sw");
		model.setShowPointLabels(true);
		model.getAxes().put(AxisType.X,
				new CategoryAxis(getTranslation("rodadas")));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel(getTranslation("resultado"));

		Empresa empresa = null;
		ChartSeries serie = new ChartSeries();
		for (ResultadoRodada resultadoRodada : resultados) {
			if (!resultadoRodada.getEmpresa().equals(empresa)) {
				empresa = resultadoRodada.getEmpresa();
				serie = new ChartSeries();
				serie.setLabel(resultadoRodada.getEmpresa().getNomeSemApostrofes());
				serie.set(resultadoRodada.getRodada().getNumero(),
						resultadoRodada.getResultado());
				model.addSeries(serie);
			} else {
				serie.setLabel(resultadoRodada.getEmpresa().getNomeSemApostrofes());
				serie.set(resultadoRodada.getRodada().getNumero(),
						resultadoRodada.getResultado());
			}
		}

		return model;
	}

	private LineChartModel initLinearModelSetor() {
		LineChartModel model = new LineChartModel();

		model.setTitle(getTranslation("variacao"));
		model.setLegendPosition("sw");
		model.setShowPointLabels(true);
		model.getAxes().put(AxisType.X,
				new CategoryAxis(getTranslation("rodadas")));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel(getTranslation("percentual"));

		RamoAtividade ramoAtividade = null;
		ChartSeries serie = new ChartSeries();

		Collections.sort(variacoesGrupo, new Comparator<RodadaVariacao>() {
			@Override
			public int compare(RodadaVariacao o1, RodadaVariacao o2) {
				return o1.getRamoAtividade().getNome()
						.compareTo(o2.getRamoAtividade().getNome());
			}
		});

		for (RodadaVariacao variacao : variacoesGrupo) {
			if (!variacao.getRamoAtividade().equals(ramoAtividade)) {
				ramoAtividade = variacao.getRamoAtividade();
				serie = new ChartSeries();
				serie.setLabel(variacao.getRamoAtividade().getNome());
				serie.set(variacao.getRodada().getNumero(),
						variacao.getVariacao());
				model.addSeries(serie);
			} else {
				serie.setLabel(variacao.getRamoAtividade().getNome());
				serie.set(variacao.getRodada().getNumero(),
						variacao.getVariacao());
			}
		}

		return model;
	}

	public Set<Rodada> getRodadas() {
		Set<Rodada> rodadas = new TreeSet<Rodada>(new RodadaComparator());

		for (ResultadoRodada resultado : resultados) {
			rodadas.add(resultado.getRodada());
		}
		return rodadas;
	}

	public Set<RodadaVariacao> getVariacoes() {
		return variacoes;
	}

	public void setVariacoes(Set<RodadaVariacao> variacoes) {
		this.variacoes = variacoes;
	}

	public List<ResultadoEmpresaDTO> filtraResultados(Rodada rodada) {
		List<ResultadoEmpresaDTO> dtos = new ArrayList<ResultadoEmpresaDTO>();

		if (rodada != null) {
			for (ResultadoRodada resultado : resultados) {
				if (resultado.getRodada().equals(rodada)) {
					ResultadoEmpresaDTO dto = new ResultadoEmpresaDTO(resultado);
					dtos.add(dto);
				}
			}
			Collections.sort(dtos);

			Integer posicao = 1;
			for (ResultadoEmpresaDTO dto : dtos) {
				dto.setPosicaoAtual(posicao++);
			}
		}
		return dtos;
	}

	private void atualizaVariacoes() {
		if (rodada != null) {
			this.variacoes = rodadaSession.buscaVariacoesDaRodada(this.rodada);
		} else {
			this.variacoes = new HashSet<RodadaVariacao>();
		}
	}

	private Rodada buscaRodadaAnterior(Integer numero) {

		for (ResultadoRodada resultadoRodada : resultados) {
			if (resultadoRodada.getRodada().getNumero() == numero - 1) {
				return resultadoRodada.getRodada();
			}
		}
		return null;
	}

	public void onRodadaSelect(AjaxBehaviorEvent event) {
		if(rodada == null) {
			return;
		}
		this.resultadosDtos = filtraResultados(rodada);

		// System.out.println("rodada " + rodada);

		if (rodada.getNumero() > 1) {

			Rodada rodadaAnterior = buscaRodadaAnterior(rodada.getNumero());
			// System.out.println("rodadaAnterior " + rodadaAnterior);
			List<ResultadoEmpresaDTO> resultadosAnteriores = filtraResultados(rodadaAnterior);

			// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			// for (ResultadoEmpresaDTO dtoAtual : this.resultadosDtos) {
			// System.out.println("Atual : " + dtoAtual);
			// }

			// for (ResultadoEmpresaDTO dtoAnterior : resultadosAnteriores) {
			// System.out.println("Anterior : " + dtoAnterior);
			// }
			// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			for (ResultadoEmpresaDTO dtoAtual : this.resultadosDtos) {
				// System.out.println("Atual : " + dtoAtual);
				for (ResultadoEmpresaDTO dtoAnterior : resultadosAnteriores) {
					// System.out.println("Anterior : " + dtoAnterior);
					if (dtoAtual.getEmpresa().equals(dtoAnterior.getEmpresa())) {
						dtoAtual.setPosicaoAnterior(dtoAnterior
								.getPosicaoAtual());
						// System.out.println("Iguais");
					}
				}
			}
		}

		this.atualizaVariacoes();
	}

}