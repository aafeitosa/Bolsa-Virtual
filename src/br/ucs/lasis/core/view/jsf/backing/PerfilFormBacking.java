package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.PerfilPermissao;
import br.ucs.lasis.core.permissions.PermissionTree;
import br.ucs.lasis.core.session.PerfilSession;

@Named("perfilFormBack")
@ViewScoped
public class PerfilFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Perfil perfil;
	private TreeNode[] selecionados;
	private PermissionTree permissionTree;
	
	@Inject
	private PerfilSession perfilSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();
		
		if (id != null) {
			this.perfil = perfilSession.buscarPorId(id);
		} else {
			this.perfil = new Perfil();
		}
		
		criarArvore();
		this.definePrimeiraPagina();

	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	public TreeNode[] getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(TreeNode[] selecionados) {
		this.selecionados = selecionados;
	}

	public PermissionTree getPermissionTree() {
		return permissionTree;
	}

	public void setPermissionTree(PermissionTree permissionTree) {
		this.permissionTree = permissionTree;
	}

	public void expandir() {
		permissionTree.expandAllNodes();
	}

	public void contrair() {
		permissionTree.colapseAllNodes();
	}

	public void marcarTodos() {
		permissionTree.selectAllNodes();
	}

	public void desmarcarTodos() {
		permissionTree.deselectAllNodes();
	}
	
	private List<PerfilPermissao> getPermissoesSelecionadas() {
		List<PerfilPermissao> permissions = new ArrayList<>();
		permissionTree.getNodesBySelection(PermissionTree.NodeTypeSelectionEnum.FULL_OR_PARTIAL_SELECTED);
		for (String permissaoId : permissionTree.getPermissionIds(Arrays.asList(selecionados))) {
			PerfilPermissao perfilPermissao = new PerfilPermissao();
			perfilPermissao.setPerfil(perfil);
			perfilPermissao.setPermissao(permissaoId);
			permissions.add(perfilPermissao);
		}
		return permissions;
	}
	
	public void criarArvore() {
		TreeNode root = new CheckboxTreeNode();

		permissionTree = new PermissionTree();
		permissionTree.createTree(root);

		if (perfil.getPermissoes() != null) {
			permissionTree.selectNodesByPermissionIds(perfil.getPermissoes());
		}
	}
	
	public String listagem() {
		return "listaPerfis";
	}

	public String salva() {
		perfil.setPermissoes(getPermissoesSelecionadas());
		perfilSession.salvar(this.perfil);
		return listagem();
	}
	

}
