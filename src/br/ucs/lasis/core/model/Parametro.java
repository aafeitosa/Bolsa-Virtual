package br.ucs.lasis.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ucs.lasis.core.enums.ParametrosEnum;

@Entity
@Table(name = "PARAMETRO")
@NamedQueries({ @NamedQuery(name = Parametro.TODOS_PARAMETROS, query = "SELECT e FROM Parametro as e "),
				@NamedQuery(name = Parametro.QUANTOS, query = "SELECT count(e) FROM Parametro as e "),
				@NamedQuery(name = Parametro.POR_TIPO, query = "SELECT e FROM Parametro as e WHERE e.tipo = :tipo ") })
@SequenceGenerator(name = "PARAMETRO_SEQUENCE", sequenceName = "PARAMETRO_SEQ", initialValue = 1, allocationSize = 1)
public class Parametro implements Serializable {

	public static final String TODOS_PARAMETROS = "todos.parametros";
	public static final String POR_TIPO = "parametro.por.tipo";
	public static final String QUANTOS = "parametro.quantos";

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETRO_SEQUENCE")
	private Long id;

	@Column(length = 50, name = "tipo", nullable = false)
	@Enumerated(EnumType.STRING)
	private ParametrosEnum tipo;

	@Column(length = 500, name = "valor", nullable = false)
	@Size(min = 1, max = 500)
	@NotNull
	private String valor;

	public Parametro() {

	}

	public Parametro(ParametrosEnum tipo) {
		this.tipo = tipo;
	}

	public Parametro(ParametrosEnum tipo, String valor) {
		this.tipo = tipo;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParametrosEnum getTipo() {
		return tipo;
	}

	public void setTipo(ParametrosEnum tipo) {
		this.tipo = tipo;
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
		Parametro other = (Parametro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
