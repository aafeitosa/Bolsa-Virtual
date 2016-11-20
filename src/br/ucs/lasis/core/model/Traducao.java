package br.ucs.lasis.core.model;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TRADUCAO")
@NamedQueries({
				@NamedQuery(name = Traducao.TODOS_POR_CODIGO_E_IDIOMA, query = "SELECT e FROM Traducao as e where e.codigo = :codigo and e.idioma = :idioma"),
				@NamedQuery(name = Traducao.TODOS_POR_IDIOMA, query = "SELECT e FROM Traducao as e where e.idioma = :idioma order by e.codigo") })
@SequenceGenerator(name = "TRADUCAO_SEQUENCE", sequenceName = "TRADUCAO_SEQ", initialValue = 1, allocationSize = 0)
public class Traducao implements Serializable {

	public static final String TODOS_POR_IDIOMA = "todos.traducao.por.idioma";
	public static final String TODOS_POR_CODIGO_E_IDIOMA = "todos.traducao.por.codigo.e.idioma";

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRADUCAO_SEQUENCE")
	private Long id;

	@Column(length = 100, name = "codigo", nullable = false)
	@Size(max = 100, min = 1)
	@NotNull
	private String codigo;

	@Column(length = 100, name = "descricao")
	@Size(max = 100)
	private String descricao;

	@Column(length = 255, name = "traducao")
	@Size(max = 255)
	private String traducao;

	@ManyToOne
	@JoinColumn(name = "idioma_id")
	private Idioma idioma;
	
	@Column(name = "traduzido", nullable = false)
	private boolean jaTraduzido;

	public Traducao() {
	}

	public Traducao(String codigo, String traducao, Idioma idioma) {
		this.codigo = codigo;
		this.traducao = traducao;
		this.idioma = idioma;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTraducao() {
		return traducao;
	}

	public void setTraducao(String traducao) {
		this.traducao = traducao;
		this.setJaTraduzido(this.jaTraduziu());
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}
	

	public boolean isJaTraduzido() {
		return jaTraduzido;
	}

	public void setJaTraduzido(boolean jaTraduzido) {
		this.jaTraduzido = jaTraduzido;
	}
	
	public boolean jaTraduziu() {
		if(this.codigo!=null) {
			return !this.codigo.equals(this.traducao);
		}
		return false;
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
		Traducao other = (Traducao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
