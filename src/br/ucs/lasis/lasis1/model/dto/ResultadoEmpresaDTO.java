package br.ucs.lasis.lasis1.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;

public class ResultadoEmpresaDTO implements Serializable,
		Comparable<ResultadoEmpresaDTO> {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;
	private BigDecimal resultado;
	private BigDecimal atratividade;
	private Integer posicaoAtual;
	private Integer posicaoAnterior;

	public ResultadoEmpresaDTO() {
	}

	public ResultadoEmpresaDTO(ResultadoRodada resultadoRodada) {
		this.empresa = resultadoRodada.getEmpresa();
		this.resultado = resultadoRodada.getResultado();
		if (resultadoRodada.getIndiceAtratividade() != null) {
			this.atratividade = resultadoRodada.getIndiceAtratividade()
					.multiply(new BigDecimal(100));
		}
	}

	public ResultadoEmpresaDTO(Empresa empresa, BigDecimal resultado) {
		this.empresa = empresa;
		this.resultado = resultado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public String getAtratividade() {
		if (this.atratividade != null) {
			return atratividade.toString() + " %";
		} else {
			return "";
		}
	}

	public void setAtratividade(BigDecimal atratividade) {
		this.atratividade = atratividade;
	}

	public Integer getPosicaoAtual() {
		return posicaoAtual;
	}

	public void setPosicaoAtual(Integer posicaoAtual) {
		this.posicaoAtual = posicaoAtual;
	}

	public Integer getPosicaoAnterior() {
		return posicaoAnterior;
	}

	public void setPosicaoAnterior(Integer posicaoAnterior) {
		this.posicaoAnterior = posicaoAnterior;
	}

	public Integer getVariacao() {
		if (this.posicaoAnterior != null && this.posicaoAtual != null) {
			return this.posicaoAnterior - this.posicaoAtual;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
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
		ResultadoEmpresaDTO other = (ResultadoEmpresaDTO) obj;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		return true;
	}

	@Override
	public int compareTo(ResultadoEmpresaDTO dto) {
		if (dto != null && dto.getResultado() != null) {
			return dto.getResultado().compareTo(this.getResultado());
		}
		return 0;
	}

	@Override
	public String toString() {
		return this.empresa + " - " + this.getResultado() + " - "
				+ this.getPosicaoAtual();
	}
}