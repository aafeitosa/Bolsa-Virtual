package br.ucs.lasis.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "RAMO_ATIVIDADE")
@NamedQueries({
		@NamedQuery(name = RamoAtividade.TODOS_RAMOS, query = "SELECT e FROM RamoAtividade as e order by e.nome"),
		@NamedQuery(name = RamoAtividade.QUANTOS, query = "SELECT count(e) FROM RamoAtividade as e"),
		@NamedQuery(name = RamoAtividade.POR_NOME, query = "SELECT e FROM RamoAtividade as e WHERE e.nome = :nome"), })
@SequenceGenerator(name = "RAMO_ATIVIDADE_SEQUENCE", sequenceName = "RAMO_ATIVIDADE_SEQ", initialValue = 1, allocationSize = 0)
public class RamoAtividade implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TODOS_RAMOS = "todos.ramos";
	public static final String QUANTOS = "ramos.quantos";
	public static final String POR_NOME = "ramos.por.nome";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RAMO_ATIVIDADE_SEQUENCE")
	private Long id;

	@Column(name = "nome", length = 60, nullable = false)
	private String nome;

	@Column(name = "descricao", length = 255, nullable = false)
	private String descricao;
	
	@Column(name = "indice")
	private BigDecimal indice;
	

	public RamoAtividade() {
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public BigDecimal getIndice() {
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
		RamoAtividade other = (RamoAtividade) obj;
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
