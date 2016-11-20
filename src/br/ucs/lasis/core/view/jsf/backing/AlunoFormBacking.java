package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.PerfilAluno;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.GrupoSession;
import br.ucs.lasis.lasis1.session.RodadaSession;

@Named("alunoFormBack")
@ViewScoped
public class AlunoFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	private GrupoSession grupoSession;

	@Inject
	@PerfilAluno
	private Perfil perfilAluno;

	@Inject
	@PeriodoCorrente
	private Periodo periodo;

	private Empresa empresa;
	private List<Empresa> empresas;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	private Grupo grupo;
	private List<Grupo> grupos;

	@EJB
	private RodadaSession rodadaSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		this.empresas = new ArrayList<Empresa>();

		this.grupos = rodadaSession.buscaGruposDoUsuario();
		if (isGrupoUnico()) {
			this.grupo = this.grupos.get(0);
			this.empresas.addAll(grupoSession.buscaEmpresasDoGrupo(grupo));
			this.empresa = this.empresas.get(0);
		}

		if (id != null) {
			this.usuario = usuarioSession.buscarPorId(id);
			this.empresa = this.usuario.getEmpresa();
			this.grupo = grupoSession.buscaPrimeiroGrupoDaEmpresa(periodo,
					this.empresa);
			this.empresas.addAll(grupoSession.buscaEmpresasDoGrupo(grupo));

//			System.out.println(this.usuario);
//			System.out.println(this.grupo);
//			System.out.println(this.empresas);
//			System.out.println(this.empresa);
		} else {
			this.usuario = new Usuario();
			this.usuario.setAtivo(true);
		}

		this.definePrimeiraPagina();
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

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String listagem() {
		return "listaAlunos";
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String salva() {
		Set<Perfil> perfis = new HashSet<Perfil>();
		perfis.add(perfilAluno);
		usuario.setPerfis(perfis);
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