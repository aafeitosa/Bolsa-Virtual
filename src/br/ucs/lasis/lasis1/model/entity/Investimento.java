package br.ucs.lasis.lasis1.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.ucs.lasis.core.model.Empresa;

@Entity
@Table(name = "INVESTIMENTO")
@SequenceGenerator(name = "INVESTIMENTO_SEQUENCE", sequenceName = "INVESTIMENTO_SEQ", initialValue = 1, allocationSize = 0)
@NamedQueries({
		@NamedQuery(name = Investimento.INVESTIMENTOS_DA_RODADA_E_EMPRESA, query = "SELECT e FROM Investimento as e WHERE e.rodada = :rodada and e.investidora = :empresa ORDER BY e.investida.nome"),
		@NamedQuery(name = Investimento.INVESTIMENTOS_DA_RODADA, query = "SELECT e FROM Investimento as e WHERE e.rodada = :rodada"),
		@NamedQuery(name = Investimento.INVESTIMENTOS_DA_RODADA_E_EMPRESA_E_INVESTIDA, query = "SELECT e FROM Investimento as e WHERE e.rodada = :rodada and e.investidora = :empresa and e.investida = :investida ORDER BY e.investida.nome") })
public class Investimento implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String INVESTIMENTOS_DA_RODADA_E_EMPRESA = "investimentos.da.rodada.e.empresa";
	public static final String INVESTIMENTOS_DA_RODADA = "investimentos.da.rodada";
	public static final String INVESTIMENTOS_DA_RODADA_E_EMPRESA_E_INVESTIDA = "investimentos.da.rodada.e.empresa.e.investida";

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVESTIMENTO_SEQUENCE")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "investidora_id")
	private Empresa investidora;

	@ManyToOne(optional = false)
	@JoinColumn(name = "investida_id")
	private Empresa investida;

	@Column(name = "valor")
	private BigDecimal valor;

	@ManyToOne(optional = false)
	@JoinColumn(name = "rodada_id")
	private Rodada rodada;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empresa getInvestidora() {
		return investidora;
	}

	public void setInvestidora(Empresa investidora) {
		this.investidora = investidora;
	}

	public Empresa getInvestida() {
		return investida;
	}

	public void setInvestida(Empresa investida) {
		this.investida = investida;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
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
		Investimento other = (Investimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.id + " " + this.investidora.getNome() + " investiu " + this.valor + " em " + this.investida.getNome();
	}
}
