package br.ucs.lasis.core.model.dto;

import java.io.Serializable;

import br.ucs.lasis.core.enums.ParametrosEnum;

public class ParametroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private ParametrosEnum parametrosEnum;
	private String valor;
	
	public ParametroDTO() {
	}
	
	public ParametroDTO(ParametrosEnum parametrosEnum) {
		this.parametrosEnum = parametrosEnum;
	}
	
	public ParametroDTO(ParametrosEnum parametrosEnum, String valor) {
		this.parametrosEnum = parametrosEnum;
		this.valor = valor;
	}

	public ParametrosEnum getParametrosEnum() {
		return parametrosEnum;
	}
	
	public String getModulo() {
		return getParametrosEnum().getModulo();
	}
	
	public String getTipo() {
		return getParametrosEnum().getDescricao();
	}

	public String getTipoDado() {
		return getParametrosEnum().getTipo().getDescription();
	}
	
	public void setParametrosEnum(ParametrosEnum parametrosEnum) {
		this.parametrosEnum = parametrosEnum;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parametrosEnum == null) ? 0 : parametrosEnum.hashCode());
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
		ParametroDTO other = (ParametroDTO) obj;
		if (parametrosEnum != other.parametrosEnum)
			return false;
		return true;
	}
}