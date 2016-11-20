package br.ucs.lasis.lasis1.view.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.CellEditEvent;

import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.model.entity.RodadaVariacao;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("rodadaFormBack")
@ViewScoped
public class RodadaFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Rodada rodada;

	private List<ResultadoRodada> resultados;
	private List<RodadaVariacao> variacoes;

	@EJB
	private RodadaSession rodadaSession;

	@EJB
	private GrupoSession grupoSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.rodada = rodadaSession.buscaPorId(id);
			this.resultados = rodadaSession.buscaResultadosDaRodada(rodada);
			this.variacoes = new ArrayList<RodadaVariacao>();
			this.variacoes.addAll(rodadaSession.buscaVariacoesDaRodada(rodada));
		} else {
			this.rodada = new Rodada();

			Grupo grupo = (Grupo) FacesContext.getCurrentInstance()
					.getExternalContext().getFlash().get("grupo");

			if (grupo != null) {
				this.rodada.setGrupo(grupo);
				this.variacoes = rodadaSession.inicializaVariacoes(grupo);
			}

			Date dataInicio = new Date();
			this.rodada.setDataInicio(dataInicio);
			this.rodada.setNumero(this.rodadaSession
					.getProximoNumeroDoGrupo(grupo));
		}

		this.editar();
		this.definePrimeiraPagina();
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setGrupo(Rodada rodada) {
		this.rodada = rodada;
	}

	public List<ResultadoRodada> getResultados() {
		return resultados;
	}

	public void setResultados(List<ResultadoRodada> resultados) {
		this.resultados = resultados;
	}

	public List<RodadaVariacao> getVariacoes() {
		return variacoes;
	}

	public void setVariacoes(List<RodadaVariacao> variacoes) {
		this.variacoes = variacoes;
	}

	public String listagem() {
		return "listaRodadas";
	}

	public String salva() {

		if (this.rodada.getId() == null) {
			for (RodadaVariacao variacao : variacoes) {
				this.rodada.addVariacao(variacao);
			}
		} else {
			for (RodadaVariacao variacao : variacoes) {
				variacao.setRodada(rodada);
			}
			Set<RodadaVariacao> setVariacoes = new HashSet<RodadaVariacao>(); 
			setVariacoes.addAll(variacoes);
			this.rodada.setVariacoes(setVariacoes);
		}
		rodadaSession.salva(this.rodada);
		return listagem();
	}

	public boolean isInsercao() {
		return null == this.rodada.getId();
	}

	public void editar() {

	}

	public void onCellEdit(CellEditEvent event) {

		Integer oldValue = (Integer) event.getOldValue();
		Integer newValue = (Integer) event.getNewValue();

		if (newValue == null) {
			showErrorMessage(getTranslation("valor_invalido"),
					getTranslation("variacao_nao_pode_ser_nula"));
			int indice = event.getRowIndex();
			this.variacoes.get(indice).setVariacao(oldValue);
		} else {

			if (newValue < -10 || newValue > 10) {
				showErrorMessage(getTranslation("valor_invalido"),
						getTranslation("variacao_limites"));
				int indice = event.getRowIndex();
				this.variacoes.get(indice).setVariacao(oldValue);
			}
		}
	}

}