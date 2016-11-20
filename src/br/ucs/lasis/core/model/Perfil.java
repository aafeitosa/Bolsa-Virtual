package br.ucs.lasis.core.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PERFIL")
@NamedQueries({
		@NamedQuery(name = Perfil.TODOS_PERFIS, query = "SELECT e FROM Perfil as e"),
		@NamedQuery(name = Perfil.QUANTOS, query = "SELECT count(e) FROM Perfil as e"),
		@NamedQuery(name = Perfil.TODOS_PERFIS_EXTERNOS, query = "SELECT e FROM Perfil as e where e.interno = false"),
		@NamedQuery(name = Perfil.POR_NOME, query = "SELECT e FROM Perfil as e WHERE e.nome = :nome"), })
@SequenceGenerator(name = "PERFIL_SEQUENCE", sequenceName = "PERFIL_SEQ", initialValue = 1, allocationSize = 0)
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TODOS_PERFIS = "todos.perfis";
	public static final String QUANTOS = "todos.perfis.quantos";
	public static final String TODOS_PERFIS_EXTERNOS = "todos.perfis.internos";
	public static final String POR_NOME = "perfil.por.nome";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFIL_SEQUENCE")
	private Long id;

	@Column(name = "nome", length = 60, nullable = false)
	private String nome;

	@Column(name = "descricao", length = 60, nullable = false)
	private String descricao;

	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	@Column(name = "interno", nullable = false)
	private boolean interno;

	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "PERFIL_USUARIO", joinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"))
	private List<Usuario> usuarios;

	@OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PerfilPermissao> permissoes;

	public Perfil() {
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setInterno(boolean interno) {
		this.interno = interno;
	}
	
	public boolean getInterno() {
		return interno;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<PerfilPermissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<PerfilPermissao> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
