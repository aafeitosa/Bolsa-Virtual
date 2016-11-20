package br.ucs.lasis.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
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

@Entity
@Cacheable
@Table(name = "EMPRESA")
@NamedQueries({
		@NamedQuery(name = Empresa.TODAS_EMPRESAS, query = "SELECT e FROM Empresa as e order by e.nome, e.acronimo"),
		@NamedQuery(name = Empresa.TODAS_EMPRESAS_ATIVAS, query = "SELECT e FROM Empresa as e where e.ativo = true order by e.nome, e.acronimo"),
		@NamedQuery(name = Empresa.QUANTAS, query = "SELECT count(e) FROM Empresa as e"),
		@NamedQuery(name = Empresa.POR_NOME, query = "SELECT e FROM Empresa as e WHERE e.nome = :nome order by e.nome, e.acronimo"), })
@SequenceGenerator(name = "EMPRESA_SEQUENCE", sequenceName = "EMPRESA_SEQ", initialValue = 1, allocationSize = 0)
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TODAS_EMPRESAS = "todas.empresas";
	public static final String TODAS_EMPRESAS_ATIVAS = "todas.empresas.ativas";
	public static final String QUANTAS = "todas.empresas.quantas";
	public static final String POR_NOME = "empresa.por.nome";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPRESA_SEQUENCE")
	private Long id;

	@Column(name = "nome", length = 60, nullable = false)
	private String nome;

	@Column(name = "descricao", length = 255, nullable = false)
	private String descricao;

	@Column(name = "cnpj", length = 14, nullable = true)
	private String cnpj;

	@Column(name = "acronimo", length = 10, nullable = false)
	private String acronimo;

	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	@Column(name = "indice")
	private BigDecimal indice;

	@ManyToOne(optional = true)
	@JoinColumn(name = "idioma_id")
	private Idioma idioma;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ramo_atividade_id")
	private RamoAtividade ramoAtividade;

	public Empresa() {
		ativo = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeSemApostrofes() {
		if (this.nome != null) {
			return this.nome.replaceAll("'", "_");
		}
		return this.nome;
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	public RamoAtividade getRamoAtividade() {
		return ramoAtividade;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}

	public BigDecimal getIndice() {
		if (indice == null) {
			return new BigDecimal(1);
		}
		return indice;
	}

	public void setIndice(BigDecimal indice) {
		this.indice = indice;
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getId() + " " + this.getNome();
	}

}
