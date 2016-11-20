package br.ucs.lasis.lasis1.model.entity;

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
@Table(name = "RESULTADO_RODADA")
@NamedQueries({
		@NamedQuery(name = ResultadoRodada.RESULTADOS_DA_EMPRESA, query = "SELECT e FROM ResultadoRodada as e where e.empresa = :empresa order by e.rodada.numero"),
		@NamedQuery(name = ResultadoRodada.RESULTADOS_DA_RODADA, query = "SELECT e FROM ResultadoRodada as e where e.rodada = :rodada order by e.resultado desc"),
		@NamedQuery(name = ResultadoRodada.RESULTADO_DA_RODADA_E_EMPRESA, query = "SELECT e FROM ResultadoRodada as e where e.rodada = :rodada and e.empresa = :empresa"),
		@NamedQuery(name = ResultadoRodada.RESULTADOS_DO_GRUPO, query = "SELECT e FROM ResultadoRodada as e where e.rodada.grupo = :grupo order by e.empresa.id, e.rodada.numero asc"),
		 })
@SequenceGenerator(name = "RESULTADO_RODADA_SEQUENCE", sequenceName = "RESULTADO_RODADA_SEQ", initialValue = 1, allocationSize = 0)
public class ResultadoRodada {
	
	public static final String RESULTADOS_DA_EMPRESA = "resultados.da.empresa";
	public static final String RESULTADOS_DA_RODADA = "resultados.da.rodada";
	public static final String RESULTADO_DA_RODADA_E_EMPRESA = "resultado.da.rodada.e.empresa";
	public static final String RESULTADOS_DO_GRUPO = "resultados.do.grupo";
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESULTADO_RODADA_SEQUENCE")
	private Long id;
	
	@Column(name = "resultado")
	private BigDecimal resultado;

	@Column(name = "indice_atratividade")
	private BigDecimal indiceAtratividade;
	
	@Column(name = "posicao")
	private Integer posicao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;

	@ManyToOne(optional = false)
	@JoinColumn(name = "rodada_id")
	private Rodada rodada;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public BigDecimal getIndiceAtratividade() {
		return indiceAtratividade;
	}

	public void setIndiceAtratividade(BigDecimal indiceAtratividade) {
		this.indiceAtratividade = indiceAtratividade;
	}
	
	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		ResultadoRodada other = (ResultadoRodada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		if(this.empresa==null) {
			return "NULO";
		} else {
			return this.rodada.getNumero() + " : " + this.empresa.getNome() + " : " + this.getResultado() + " : " + this.indiceAtratividade + " : " + this.posicao;
		}
	}
}