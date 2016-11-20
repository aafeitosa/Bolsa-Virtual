package br.ucs.lasis.lasis1.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.ucs.lasis.core.model.RamoAtividade;

@Entity
@Table(name = "RODADA_VARIACAO")
@NamedQueries({ @NamedQuery(name = RodadaVariacao.VARIACOES_DO_GRUPO, query = "SELECT e FROM RodadaVariacao as e where e.rodada.grupo = :grupo and e.rodada.dataFim is not null") })
@SequenceGenerator(name = "RODADA_VARIACAO_SEQUENCE", sequenceName = "RODADA_VARIACAO_SEQ", initialValue = 1, allocationSize = 0)
public class RodadaVariacao implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String VARIACOES_DO_GRUPO = "variacoes.do.grupo";

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RODADA_VARIACAO_SEQUENCE")
	private Long id;

	@Column(name = "variacao", nullable = false)
	private Integer variacao;

	@ManyToOne(optional = false)
	@JoinColumn(name = "rodada_id")
	private Rodada rodada;

	@OneToOne(optional = false)
	@JoinColumn(name = "ramo_id")
	private RamoAtividade ramoAtividade;

	public RodadaVariacao() {
	}

	public RodadaVariacao(int variacao, RamoAtividade ramoAtividade) {
		this.variacao = variacao;
		this.ramoAtividade = ramoAtividade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVariacao() {
		return variacao;
	}

	public float getMultiplicadorVariacao() {
		if (getVariacao() == null) {
			return 1f;
		}
		float fator = (100f + getVariacao()) / 100f;
		return fator;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public void setVariacao(Integer variacao) {
		this.variacao = variacao;
	}

	public RamoAtividade getRamoAtividade() {
		return ramoAtividade;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((ramoAtividade == null) ? 0 : ramoAtividade.hashCode());
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
		RodadaVariacao other = (RodadaVariacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ramoAtividade == null) {
			if (other.ramoAtividade != null)
				return false;
		} else if (!ramoAtividade.equals(other.ramoAtividade))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this.ramoAtividade == null) {
			return "NULO";
		} else {
			return this.ramoAtividade.getNome() + " : " + this.variacao + " : "
					+ this.getMultiplicadorVariacao();
		}
	}

}
