package br.ucs.lasis.lasis1.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RODADA")
@NamedQueries({
		@NamedQuery(name = Rodada.TODAS_RODADAS, query = "SELECT e FROM Rodada as e ORDER BY e.dataInicio, e.numero"),
		@NamedQuery(name = Rodada.MAIOR_NUMERO_NA_DATA, query = "SELECT max(e.numero) FROM Rodada as e where e.dataInicio between :dataInicio and :dataFim"),
		@NamedQuery(name = Rodada.MAIOR_NUMERO_NA_DATA_E_GRUPO, query = "SELECT max(e.numero) FROM Rodada as e where e.dataInicio between :dataInicio and :dataFim and e.grupo = :grupo"),
		@NamedQuery(name = Rodada.MAIOR_NUMERO_DO_GRUPO, query = "SELECT max(e.numero) FROM Rodada as e where e.grupo = :grupo"),
		@NamedQuery(name = Rodada.RODADA_COM_NUMERO_E_GRUPO, query = "SELECT e FROM Rodada as e where e.grupo = :grupo and e.numero = :numero"),
		@NamedQuery(name = Rodada.CONTA_TODAS, query = "SELECT count(e) FROM Rodada as e")})
@SequenceGenerator(name = "RODADA_SEQUENCE", sequenceName = "RODADA_SEQ", initialValue = 1, allocationSize = 0)
public class Rodada implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TODAS_RODADAS = "todas.rodadas";
	public static final String CONTA_TODAS = "conta.todas.rodadas";
	public static final String MAIOR_NUMERO_NA_DATA = "rodada.maior.numero";
	public static final String MAIOR_NUMERO_NA_DATA_E_GRUPO = "rodada.maior.numero.na.data.e.grupo";
	public static final String MAIOR_NUMERO_DO_GRUPO = "rodada.maior.numero.do.grupo";
	public static final String RODADA_COM_NUMERO_E_GRUPO = "rodada.com.numero.e.grupo";
	
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RODADA_SEQUENCE")
	private Long id;

	@Column(name = "numero", nullable = false)
	private Integer numero;

	@Column(name = "valor", nullable = false)
	private BigDecimal valor;
	
	@Column(name = "nro_maximo_investimentos", nullable = true)
	private Integer numeroMaximoInvestimentos;
	
	@ManyToOne(optional=false)
	private Periodo periodo;

	@ManyToOne(optional=false)
	@JoinColumn(name="grupo_id")
	private Grupo grupo;
	
	@Column(name = "data_inicio", nullable = false)
	private Date dataInicio;

	@Column(name = "data_fim", nullable = true)
	private Date dataFim;
	
	@OneToMany(mappedBy="rodada",cascade=CascadeType.ALL)
	private Set<Investimento> investimentos;

	@OneToMany(mappedBy="rodada",cascade=CascadeType.ALL)
	private Set<RodadaVariacao> variacoes;
	
	public Rodada() {
		this.variacoes = new HashSet<RodadaVariacao>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal diminuiValor(BigDecimal valor) {
		this.valor.subtract(valor);
		return this.valor;
	}
	
	public Integer getNumeroMaximoInvestimentos() {
		return numeroMaximoInvestimentos;
	}

	public void setNumeroMaximoInvestimentos(Integer numeroMaximoInvestimentos) {
		this.numeroMaximoInvestimentos = numeroMaximoInvestimentos;
	}

	public Set<Investimento> getInvestimentos() {
		return investimentos;
	}

	public void setInvestimentos(Set<Investimento> investimentos) {
		this.investimentos = investimentos;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public boolean isEncerrada() {
		return this.getDataFim() != null;
	}

	public void setEncerrada(boolean encerrada) {
		if(encerrada) {
			this.setDataFim(new Date());
		} else {
			this.setDataFim(null);
		}
	}

	public Set<RodadaVariacao> getVariacoes() {
		return variacoes;
	}

	public void setVariacoes(Set<RodadaVariacao> variacoes) {
		this.variacoes = variacoes;
	}
	
	public void addVariacao(RodadaVariacao variacao) {
		variacao.setRodada(this);
		this.variacoes.add(variacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rodada other = (Rodada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[RODADA id = " + id + " ,numero = " + numero + "]";
	}
}
