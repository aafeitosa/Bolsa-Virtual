package br.ucs.lasis.lasis1.model.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "PERIODO")
@NamedQueries({
		@NamedQuery(name = Periodo.TODOS_PERIODOS, query = "SELECT e FROM Periodo as e ORDER BY e.nome"),
	    @NamedQuery(name = Periodo.PERIODO_NA_DATA, query = "SELECT e FROM Periodo as e where :data between e.dataInicio and e.dataFim"),
		@NamedQuery(name = Periodo.CONTA_TODOS, query = "SELECT count(e) FROM Periodo as e")})
@SequenceGenerator(name = "PERIODO_SEQUENCE", sequenceName = "PERIODO_SEQ", initialValue = 1, allocationSize = 0)
public class Periodo implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TODOS_PERIODOS = "todos.periodos";
	public static final String CONTA_TODOS = "conta.todos.periodos";
	public static final String PERIODO_NA_DATA = "periodo.na.data";
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERIODO_SEQUENCE")
    private Long id;
	
	@Column(name = "nome", length = 60, nullable = false)
	private String nome;
	
	@Column(name = "descricao", length = 255, nullable = true)
	private String descricao;
	
	@Column(name = "data_inicio", nullable = true)
	private Date dataInicio;
	
	@Column(name = "data_fim", nullable = true)
	private Date dataFim;
	
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
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String toString() {
		return this.getNome();
	};

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
		Periodo other = (Periodo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}