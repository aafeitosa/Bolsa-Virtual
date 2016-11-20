package br.ucs.lasis.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class PerfilPermissaoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "perfil_id", nullable = false)
	private Long perfilId;

	@NotNull
	@Size(max = 255)
	@Column(name = "permissao_id", length = 255, nullable = false)
	private String permissao;

	public PerfilPermissaoPK() {

	}

	public Long getPerfilId() {
		return perfilId;
	}

	public void setPerfilId(Long perfilId) {
		this.perfilId = perfilId;
	}

	public String getPermissao() {
		return permissao;
	}

	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permissao == null) ? 0 : permissao.hashCode());
		result = prime * result + ((perfilId == null) ? 0 : perfilId.hashCode());
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
		PerfilPermissaoPK other = (PerfilPermissaoPK) obj;
		if (permissao == null) {
			if (other.permissao != null)
				return false;
		} else
			if (!permissao.equals(other.permissao))
				return false;
		if (perfilId == null) {
			if (other.perfilId != null)
				return false;
		} else
			if (!perfilId.equals(other.perfilId))
				return false;
		return true;
	}

	@Override
	public String toString() {
		return "";
	}
}
