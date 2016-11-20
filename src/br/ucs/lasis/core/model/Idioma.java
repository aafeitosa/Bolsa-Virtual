package br.ucs.lasis.core.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Cacheable
@Table(name = "IDIOMA")
@NamedQueries({ @NamedQuery(name = Idioma.TODOS_IDIOMAS, query = "SELECT e FROM Idioma as e ORDER BY e.nome"),
				@NamedQuery(name = Idioma.POR_LOCALE, query = "SELECT e FROM Idioma as e WHERE UPPER(e.locale) = UPPER(:locale) ") })
@SequenceGenerator(name = "IDIOMA_SEQUENCE", sequenceName = "IDIOMA_SEQ", initialValue = 1, allocationSize = 0)
public class Idioma implements Serializable {

	public static final String TODOS_IDIOMAS = "todos.idiomas";
	public static final String POR_LOCALE = "idioma.por.locale";

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDIOMA_SEQUENCE")
	private Long id;

	@Column(length = 30, name = "nome", nullable = false)
	@Size(min = 1, max = 30)
	@NotNull
	private String nome;

	@Column(length = 100, name = "descricao")
	@Size(max = 100)
	private String descricao;

	@Column(length = 30, name = "formato_data", nullable = false)
	@Size(min = 1, max = 30)
	private String formatoData;

	@Column(length = 30, name = "formato_telefone", nullable = false)
	@Size(min = 1, max = 30)
	private String formatoTelefone;

	@Column(length = 10, name = "locale", nullable = true, unique=true)
	@Size(min = 0, max = 10)
	private String locale;

	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "file_id", nullable = true)
	private Arquivo arquivo;

	public Idioma() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFormatoData() {
		return formatoData;
	}

	public void setFormatoData(String formatoData) {
		this.formatoData = formatoData;
	}

	public String getFormatoTelefone() {
		return formatoTelefone;
	}

	public void setFormatoTelefone(String formatoTelefone) {
		this.formatoTelefone = formatoTelefone;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
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
		Idioma other = (Idioma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
