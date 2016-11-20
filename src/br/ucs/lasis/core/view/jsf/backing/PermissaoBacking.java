package br.ucs.lasis.core.view.jsf.backing;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.PerfilPermissao;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.session.UsuarioSession;

@ManagedBean(name = "permissao")
@SessionScoped
public class PermissaoBacking extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject 
	private LoginBacking loginBack; 
	
	private Usuario usuario;

	private Set<String> permissoes;

	public PermissaoBacking() {
		super();
	}

	@PostConstruct
	public void postConstruct() {
		permissoes = new HashSet<String>();
		usuario = loginBack.getUsuarioLogado();
		if (usuario== null || usuario.getPerfis() == null || usuario.getPerfis().isEmpty()) {
			showErrorMessage("Problemas!","Não foram defiidas permissões para o usuário.");
			return;
		}

		try {
			carregaPermissoes();
		} catch (BusinessException e) {
			showErrorMessage("Problemas","Houve um erro ao carregar as permissões");
			e.printStackTrace();
		}
	}

	public void carregaPermissoes() throws BusinessException {
		
		if (usuario != null) {

			List<PerfilPermissao> permissaoList = usuarioSession.buscarPermissoes(usuario);

			permissoes = new HashSet<String>();
			for (PerfilPermissao perfilPermissao : permissaoList) {
				permissoes.add(perfilPermissao.getId().getPermissao());
			}
		}
	}

	public boolean temPermissao(String permissionId) {
		return permissoes.contains(permissionId);
	}

	public boolean rendered(String permissionId) {
		return permissoes.contains(permissionId);
	}

	public boolean disabled(String permissionId) {
		return !permissoes.contains(permissionId);
	}

}
