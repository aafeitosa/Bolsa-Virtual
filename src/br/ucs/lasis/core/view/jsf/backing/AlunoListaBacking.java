package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.PerfilAluno;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.core.view.lazymodels.LazyUsuarioDataModel;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("alunoListaBack")
@ViewScoped
public class AlunoListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private LazyDataModel<Usuario> dataModel;

	private Usuario usuario;

	private Map<String, Object> parametros;

	private Grupo grupo;
	private List<Grupo> grupos;
	private Empresa empresa;
	private List<Empresa> empresas;

	@Inject
	private UsuarioSession usuarioSession;

	@EJB
	private RodadaSession rodadaSession;

	@Inject
	private GrupoSession grupoSession;

	@Inject
	@PerfilAluno
	private Perfil perfilAluno;

	@PostConstruct
	public void init() {
		this.parametros = new HashMap<String, Object>();
		this.parametros.put("perfil", perfilAluno);
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		this.grupos = rodadaSession.buscaGruposDoUsuario();
		this.empresas = new ArrayList<Empresa>();
		if (this.isGrupoUnico()) {
			this.grupo = this.grupos.get(0);
			this.empresas.addAll(grupoSession.buscaEmpresasDoGrupo(grupo));
			this.empresa = this.empresas.get(0);
		}
		this.pesquisar(null);
		this.dataModel = new LazyUsuarioDataModel(usuarioSession,
				this.parametros);
		this.definePrimeiraPagina();
	}

	public LazyDataModel<Usuario> getDataModel() {
		return this.dataModel;
	}

	public Usuario getAluno() {
		return usuario;
	}

	public void setAluno(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String novo() {
		return "formAlunos";
	}

	public String listagem() {
		return "listaAlunos";
	}

	public void exclui(ActionEvent event) {
		try {
			usuarioSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {

//		if (this.empresa == null) {
//			parametros.put("empresa", new Empresa());
//		} else {
//			this.parametros.put("empresa", this.empresa);
//		}

		this.parametros.put("empresa", this.empresa);
		
		this.parametros.put("grupo", this.grupo);

		if (usuario.getNome() != null) {
			this.parametros.put("nome", usuario.getNome());
		}

		this.parametros.put("ativo", usuario.isAtivo());

	}

	public void onGrupoChange() {
		if (grupo != null) {
			this.empresas.clear();
			this.empresas.addAll(grupoSession.buscaEmpresasDoGrupo(grupo));
		} else {
			this.empresas = new ArrayList<Empresa>();
		}
	}

	public boolean isGrupoUnico() {
		return (this.grupos != null && this.grupos.size() == 1);
	}
}
