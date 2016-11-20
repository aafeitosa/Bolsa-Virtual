package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.EmpresaSession;
import br.ucs.lasis.core.session.PerfilSession;
import br.ucs.lasis.core.session.UsuarioSession;

@Named("usuarioFormBack")
@ViewScoped
public class UsuarioFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private DualListModel<Perfil> perfilDualList;
	private List<Perfil> perfilSource;
	private List<Perfil> perfilTarget;

	private List<Empresa> empresasAtivas;

	@Inject
	private UsuarioSession usuarioSession;

	@EJB
	private PerfilSession perfilSession;

	@Inject
	private EmpresaSession empresaSession;

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

		carregaEmpresas();
		editar();
		this.definePrimeiraPagina();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String listagem() {
		return "listaUsuarios";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String salva() {
		usuario.setPerfis(new HashSet(perfilDualList.getTarget()));
		usuarioSession.salvar(this.usuario);
		return listagem();
	}

	private void carregaEmpresas() {
		if (this.isEmpresaPrincipal()) {
			empresasAtivas = empresaSession.buscaTodasAtivas();
		} else {
			Empresa empresa = usuarioLogado.getEmpresa();
			this.usuario.setEmpresa(empresa);
		}
	}

	public void editar() {

		perfilSource = new ArrayList<Perfil>();
		perfilSource = buscarPerfis();
		perfilTarget = new ArrayList<Perfil>();

		List<Perfil> perfis = new ArrayList<Perfil>();
		perfis = new ArrayList<Perfil>(usuario.getPerfis());
		perfilSource.removeAll(perfis);
		perfilTarget.addAll(perfis);

		configDualList();
	}

	public void configDualList() {
		perfilDualList = new DualListModel<Perfil>(perfilSource, perfilTarget);
	}

	private List<Perfil> buscarPerfis() {
		List<Perfil> perfis = null;
		try {

			if (isEmpresaPrincipal()
					&& this.getEmpresaLogada()
							.equals(this.usuario.getEmpresa())) {
				perfis = perfilSession.buscaTodos();
			} else {
				perfis = perfilSession.buscaTodosExternos();
			}
		} catch (BusinessException e) {
			return null;
		}
		return perfis;
	}

	public DualListModel<Perfil> getPerfilDualList() {
		return perfilDualList;
	}

	public void setPerfilDualList(DualListModel<Perfil> perfilDualList) {
		this.perfilDualList = perfilDualList;
	}

	public boolean isInsercao() {
		return null == this.usuario.getId();
	}

	public List<Empresa> getTodasEmpresasAtivas() {
		return empresasAtivas;
	}
}
