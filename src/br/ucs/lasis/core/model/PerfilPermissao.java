package br.ucs.lasis.core.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERFIL_PERMISSAO")
public class PerfilPermissao implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PerfilPermissaoPK id;

	@NotNull
	@ManyToOne
	@MapsId("perfilId")
	private Perfil perfil;

	public PerfilPermissao() {
		this.id = new PerfilPermissaoPK();
	}

	public PerfilPermissaoPK getId() {
		return id;
	}

	public void setId(PerfilPermissaoPK id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		id.setPerfilId(perfil.getId());
		this.perfil = perfil;
	}

	public void setPermissao(String permissao) {
		id.setPermissao(permissao);
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PerfilPermissao other = (PerfilPermissao) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else
			if (!id.equals(other.id)) {
				return false;
			}
		return true;
	}

	@Override
	public String toString() {
		return id.getPermissao();
	}
}
