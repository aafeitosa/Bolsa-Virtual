package br.ucs.lasis.lasis1.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Usuario;

@Entity
@Table(name = "GRUPO")
@NamedQueries({
		@NamedQuery(name = Grupo.TODOS_GRUPOS, query = "SELECT e FROM Grupo as e ORDER BY e.nome"),
		@NamedQuery(name = Grupo.TODOS_GRUPOS_DO_PERIODO, query = "SELECT e FROM Grupo as e WHERE e.periodo = :periodo ORDER BY e.nome"),
		@NamedQuery(name = Grupo.TODOS_GRUPOS_COM_PROFESSOR, query = "SELECT e FROM Grupo as e WHERE e.periodo = :periodo and :professor MEMBER OF e.professores ORDER BY e.nome"),
		@NamedQuery(name = Grupo.CONTA_TODOS, query = "SELECT count(e) FROM Grupo as e"),
        @NamedQuery(name = Grupo.CONTA_TODOS_DO_PERIODO, query = "SELECT count(e) FROM Grupo as e WHERE e.periodo = :periodo")})
@SequenceGenerator(name = "GRUPO_SEQUENCE", sequenceName = "GRUPO_SEQ", initialValue = 1, allocationSize = 0)
public class Grupo implements Serializable {

	public static final String TODOS_GRUPOS = "todos.grupos";
	public static final String CONTA_TODOS = "conta.todos.grupos";
	public static final String TODOS_GRUPOS_DO_PERIODO = "todos.grupos.do.periodo";
	public static final String CONTA_TODOS_DO_PERIODO = "conta.todos.grupos.do.periodo";
	public static final String TODOS_GRUPOS_COM_PROFESSOR = "todos.grupos.com.professor";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_SEQUENCE")
	private Long id;

	@Column(length = 30, name = "nome", nullable = false)
	@NotNull
	private String nome;

	@Column(length = 255, name = "descricao")
	@Size(max = 255)
	private String descricao;

	@OneToMany
	@JoinTable(name = "GRUPO_EMPRESA", joinColumns = @JoinColumn(name = "GRUPO_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID"))
	private Set<Empresa> empresas;
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name = "GRUPO_USUARIO", joinColumns = @JoinColumn(name = "GRUPO_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID"))
	private Set<Usuario> professores;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="periodo_id")
	private Periodo periodo;

	public Grupo() {
		empresas = new HashSet<Empresa>();
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
	
	public Set<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(Set<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Set<Usuario> getProfessores() {
		return professores;
	}

	public void setProfessores(Set<Usuario> professores) {
		this.professores = professores;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
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
		Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getNome();
	}
}
