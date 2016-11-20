package br.ucs.lasis.lasis1.view.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CellEditEvent;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Investimento;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("investimentoFormBack")
@ViewScoped
public class InvestimentoFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Rodada rodada;

	@Inject
	@EmpresaLogada
	private Empresa empresa;
	
	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;
	
	@Inject
	@PerfilCoordenador
	private Perfil perfilCoordenador;
	
	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;
	
	private Grupo grupo;
	private List<Grupo> grupos;
	private List<Empresa> empresas;
	private Double resultadoAnterior;
	private Double valorRodada;

	@EJB
	private RodadaSession rodadaSession;

	@EJB
	private GrupoSession grupoSession;

	private List<Investimento> investimentos;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.rodada = rodadaSession.buscaPorId(id);
			investimentos = rodadaSession.buscaInvestimentosDaRodadaEEmpresa(
					rodada, empresa);
			
			atualizaResultadoAnterior();
			atualizaValorRodada();
			
		}
		
		this.grupos = rodadaSession.buscaGruposDoUsuario();
		
		Long idGrupo = this.getParametroLong("grupo_id");
		if (idGrupo != null) {
			this.grupo = grupoSession.buscaPorId(idGrupo);
			this.empresas = new ArrayList<Empresa>(grupoSession.buscaEmpresasDoGrupo(this.grupo));
		}
		
		this.definePrimeiraPagina();
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setGrupo(Rodada rodada) {
		this.rodada = rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Double getValor() {
		Double valor = valorRodada - this.getValorInvestido(); 
		valor += resultadoAnterior;
		return valor;
	}

	public Double getResultadoAnterior() {
		return resultadoAnterior;
	}
	
	public Double getValorRodada() {
		return valorRodada;
	}

	public List<Investimento> getInvestimentos() {
		return investimentos;
	}

	public void setInvestimentos(List<Investimento> investimentos) {
		this.investimentos = investimentos;
	}

	public String listagem() {
		return "listaInvestimentos";
	}
	
	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String salva() {
		rodadaSession.salvaInvestimentos(investimentos);
		if(isTrocaEmpresas()) {
			return null;
		}
		return listagem();
	}

	public boolean isInsercao() {
		return null == this.rodada.getId();
	}
	
	private void atualizaResultadoAnterior() {
		resultadoAnterior = this.buscaResultadoRodadaAnterior(rodada, empresa);
	}

	private void atualizaValorRodada() {
		valorRodada = this.rodada!=null?this.rodada.getValor()!=null?this.rodada.getValor().doubleValue():0:0;
	}

	
	public void onCellEdit(CellEditEvent event) {

		BigDecimal oldValue = (BigDecimal) event.getOldValue();

		if (this.getNroInvestimentos() > this.rodada
				.getNumeroMaximoInvestimentos()) {
			showErrorMessage(getTranslation("limite_excedido"),
					getTranslation("nro_investimentos_maximo_ultrapassado"));
			int indice = event.getRowIndex();
			this.investimentos.get(indice).setValor(oldValue);
		} else {
			if (this.getValor() < 0) {
				showErrorMessage(getTranslation("limite_excedido"),
						getTranslation("valor_maximo_ultrapassado"));
				int indice = event.getRowIndex();
				this.investimentos.get(indice).setValor(oldValue);
			} 
		}
	}
	
	public Double getValorInvestido() {
		Double valor = 0d;
		for (Investimento investimento : investimentos) {
			if(investimento.getValor()!=null) {
				valor += investimento.getValor().doubleValue();
			}
		}
		return valor;
	}

	public int getNroInvestimentos() {
		int cont = 0;
		for (Investimento investimento : investimentos) {
			if (investimento.getValor() != null) {
				cont++;
			}
		}
		return cont;
	}

	public boolean isTrocaEmpresas() {
		return (usuarioLogado.isAdministrador() || usuarioLogado.getPerfis().contains(perfilCoordenador) || usuarioLogado.getPerfis().contains(perfilProfessor));
	}
	
	public void onGrupoSelect(AjaxBehaviorEvent event) {
		this.empresas = new ArrayList<Empresa>(grupoSession.buscaEmpresasDoGrupo(grupo));
		investimentos = new ArrayList<Investimento>();
	}

	public void onEmpresaSelect(AjaxBehaviorEvent event) {
//		System.out.println("Trocou empresa");
		investimentos = rodadaSession.buscaInvestimentosDaRodadaEEmpresa(
				rodada, empresa);
		atualizaResultadoAnterior();
	}
	
	private Double buscaResultadoRodadaAnterior(Rodada rodada, Empresa empresa) {
		
		ResultadoRodada resultadoRodada = rodadaSession.buscaResultadoRodada(rodada, empresa);
		
		if(resultadoRodada!=null && resultadoRodada.getResultado() != null) {
			return resultadoRodada.getResultado().doubleValue();
		} else {
			return 0d;
		}
	}
	
}