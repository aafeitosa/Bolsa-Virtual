package br.ucs.lasis.core.view.jsf.backing;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.EmpresaSistema;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.UsuarioSession;

@Named("professorFormBack")
@ViewScoped
public class ProfessorFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;
	
	@Inject
	@EmpresaSistema
	private Empresa empresaPrincipal;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.usuario = usuarioSession.buscarPorId(id);
		} else {
			this.usuario = new Usuario();
			this.usuario.setAtivo(true);
		}

		this.definePrimeiraPagina();
	}

	public Usuario getProfessor() {
		return usuario;
	}

	public void setProfessor(Usuario usuario) {
		this.usuario = usuario;
	}

	public String listagem() {
		return "listaProfessores";
	}

	public String salva() {
		Set<Perfil> perfis = new HashSet<Perfil>();
		perfis.add(perfilProfessor);
		usuario.setPerfis(perfis);
		usuario.setEmpresa(empresaPrincipal);
		try {
			usuarioSession.salvar(this.usuario);
		} catch (BusinessException be) {
			this.showErrorMessage(
					getTranslation("login_duplicado"),
					getTranslation("login") + " "
							+ this.usuario.getLogin() + " "
							+ getTranslation("ja_utilizado") + " "
							+ getTranslation("operacao_cancelada"));
			return null;
		}
		return listagem();
	}

	public boolean isInsercao() {
		return null == this.usuario.getId();
	}
}