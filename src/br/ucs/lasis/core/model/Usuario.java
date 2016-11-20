package br.ucs.lasis.core.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Cacheable
@Table(name = "USUARIO")
@NamedQueries({
		@NamedQuery(name = Usuario.TODOS_USUARIOS, query = "select e from Usuario as e"),
		@NamedQuery(name = Usuario.TODOS_USUARIOS_EMPRESA, query = "select e from Usuario as e where e.empresa = :empresa order by e.nome"),
		@NamedQuery(name = Usuario.QUANTOS, query = "select count(e) from Usuario as e"),
		@NamedQuery(name = Usuario.QUANTOS_EMPRESA, query = "select count(e) from Usuario as e where e.empresa = :empresa"),
		@NamedQuery(name = Usuario.NOME_COMECANDO_POR, query = "select e from Usuario as e where e.nome like :nome"),
		@NamedQuery(name = Usuario.TODOS_USUARIOS_COM_PERFIL, query = "select e from Usuario as e where :perfil member of e.perfis and e.ativo = true"),
		@NamedQuery(name = Usuario.POR_LOGIN, query = "select e from Usuario as e left join fetch e.perfis where e.login = :login") })
@NamedNativeQueries({ @NamedNativeQuery(name = Usuario.TODOS_USUARIOS_PERIODO, query = "select u.id, u.email, u.login, u.nome, u.password, u.ramal, u.empresa_id, u.ativo "
		+ "from usuario u, empresa e, grupo_empresa gu, grupo g, periodo p "
		+ "where u.empresa_id = e.id "
		+ "and e.id = gu.empresa_id "
		+ "and gu.grupo_id = g.id "
		+ "and g.periodo_id = p.id "
		+ "and p.nome = ?",
resultClass = Usuario.class) })
@SequenceGenerator(name = "USUARIO_SEQUENCE", sequenceName = "USUARIO_SEQ", initialValue = 1, allocationSize = 0)
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TODOS_USUARIOS = "todos.usuarios";
	public static final String TODOS_USUARIOS_EMPRESA = "todos.usuarios.empresa";
	public static final String TODOS_USUARIOS_PERIODO = "todos.usuarios.periodo";
	public static final String TODOS_USUARIOS_PE = "todos.usuarios.empresa";
	public static final String QUANTOS = "todos.usuarios.quantos";
	public static final String QUANTOS_EMPRESA = "todos.usuarios.quantos.empresa";
	public static final String NOME_COMECANDO_POR = "usuario.nome.comecando.por";
	public static final String POR_LOGIN = "usuario.por.login";
	public static final String TODOS_USUARIOS_COM_PERFIL = "todos.usuarios.com.perfil";

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQUENCE")
	private Long id;

	@Column(name = "nome", length = 60, nullable = false)
	private String nome;

	@Column(name = "login", length = 60, nullable = false)
	private String login;

	@Column(name = "password", length = 64, nullable = false)
	private String password;

	@Column(name = "email", length = 60, nullable = true)
	private String email;

	@Column(name = "ramal", nullable = true)
	private Integer ramal;

	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PERFIL_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id"))
	private Set<Perfil> perfis;

	@Transient
	boolean administrador;

	public Usuario() {
		this.perfis = new HashSet<Perfil>();
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRamal() {
		return ramal;
	}

	public void setRamal(Integer ramal) {
		this.ramal = ramal;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Set<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(Set<Perfil> perfis) {
		this.perfis = perfis;
	}

	@Transient
	public String getPassword() {
		return this.password;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
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
		Usuario other = (Usuario) obj;
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
