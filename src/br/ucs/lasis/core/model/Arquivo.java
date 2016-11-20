package br.ucs.lasis.core.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Cacheable
@Table(name = "ARQUIVO")
@SequenceGenerator(name = "ARQUIVO_SEQUENCE", sequenceName = "ARQUIVO_SEQ", initialValue = 1, allocationSize = 0)
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_SEQUENCE")
	private Long id;

	@Column(length = 255, name = "nome_original", nullable = false)
	@Size(min = 1, max = 255)
	private String nomeOriginal;

	@Column(length = 255, name = "nome_interno", nullable = false)
	@Size(min = 1, max = 255)
	private String nomeInterno;

	@Column(length = 10, name = "tipo", nullable = true)
	@Size(min = 1, max = 10)
	private String tipo;

	@Column(name = "tamanho", nullable = true)
	private Long tamanho;

	public Arquivo() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public String getNomeInterno() {
		return nomeInterno;
	}

	public void setNomeInterno(String nomeInterno) {
		this.nomeInterno = nomeInterno;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
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
		Arquivo other = (Arquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
