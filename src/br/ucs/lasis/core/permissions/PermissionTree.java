package br.ucs.lasis.core.permissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import br.ucs.lasis.core.model.PerfilPermissao;
import br.ucs.lasis.core.view.enums.PermissionEnum;

public class PermissionTree implements Serializable {

	private static final long serialVersionUID = 1L;

	protected TreeNode root;

	protected TreeNode[] selectedNodes;

	public PermissionTree() {

	}

	public enum NodeTypeSelectionEnum {
		FULL_SELECTED, PARTIAL_SELECTED, FULL_OR_PARTIAL_SELECTED;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	/**
	 * Get nodes by selection.
	 * 
	 * @param selectionType
	 * @return
	 */
	public List<TreeNode> getNodesBySelection(NodeTypeSelectionEnum selectionType) {
		return new ArrayList<>(getNodesBySelection(root, selectionType));
	}

	/**
	 * Retorna somente os nodos totalmente selecionados.
	 * 
	 * @return
	 */
	public List<TreeNode> getFullySelectedNodes() {
		return getNodesBySelection(NodeTypeSelectionEnum.FULL_SELECTED);
	}

	/**
	 * Retorna somente os nodos parcialmente selecionados, ex: selecionou somente um filho de um nodo, o pai ficara
	 * parcialmente selecionado.
	 * 
	 * @return
	 */
	public List<TreeNode> getPartiallySelectedNodes() {
		return getNodesBySelection(NodeTypeSelectionEnum.PARTIAL_SELECTED);
	}

	/**
	 * Retorna todos os nodes tanto os selecionados, ou parcialmente selecionados.
	 * 
	 * @return
	 */
	public List<TreeNode> getFullOrPartiallySelectedNodes() {
		return getNodesBySelection(NodeTypeSelectionEnum.FULL_OR_PARTIAL_SELECTED);
	}

	/**
	 * Select permissions by id.
	 * 
	 * @param permissionIds
	 */
	public void selectNodesByPermissionIds(Collection<PerfilPermissao> permissoesPerfil) {
		Set<Permission> permissions = new HashSet<>();
		for (PerfilPermissao permissaoPerfil : permissoesPerfil) {
			Permission permission = PermissionEnum.getById(permissaoPerfil.getId().getPermissao());
			if (permission != null) {
				permissions.add(permission);
			}
		}
		selectNodesByPermissions(permissions);
	}

	/**
	 * Select nodes that stores the permissions sent through the parameter <code>permissions</code>.
	 * 
	 * @param permissions
	 *            Permissions to find and select nodes.
	 * @throws Exception
	 */
	public void selectNodesByPermissions(Collection<Permission> permissions) {
		Set<TreeNode> selected = findNodes(root, permissions);
		for (TreeNode node : selected) {

			// Only select if the node is leaf (the parent nodes are partial or full selected automatically)
			if (node.isLeaf()) {
				node.setSelected(true);
			}
		}
	}

	/**
	 * Get the permissions from a list of nodes.
	 * 
	 * @return {@link List} containing permissions.
	 */
	public List<Permission> getPermissions(List<TreeNode> nodes) {
		List<Permission> permissions = new ArrayList<>();
		for (TreeNode node : nodes) {
			Permission permission = (Permission) node.getData();
			if (permission == null) {
				continue;
			}
			permissions.add(permission);
		}
		return permissions;
	}

	/**
	 * Get the ids from a list of nodes.
	 * 
	 * @return {@link List} containing permission's ids.
	 */
	public List<String> getPermissionIds(List<TreeNode> nodes) {
		Set<String> permissionIds = new HashSet<>();
		for (TreeNode node : getFullOrPartiallySelectedNodes()) {
			Permission permission = (Permission) node.getData();
			if (permission == null) {
				continue;
			}
			String permissionId = permission.getId();
			permissionIds.add(permissionId);
		}
		return new ArrayList<String>(permissionIds);
	}

	/**
	 * Recursively, find all nodes that store the permissions sent through the parameter <code>permissions</code>.
	 * 
	 * @param node
	 *            Source node.
	 * @param permissions
	 *            Permissions to filter nodes.
	 * @return {@link Set} containing {@link TreeNode}s that stores the permissions sent through the parameter
	 *         <code>permissions</code>.
	 */
	protected Set<TreeNode> findNodes(TreeNode node, Collection<Permission> permissions) {

		Set<TreeNode> selected = new HashSet<>();
		List<TreeNode> children = node.getChildren();

		if (permissions == null) {
			return selected;
		}

		for (TreeNode child : children) {
			Permission permission = (Permission) child.getData();
			if (node != null) {
				if (permissions.contains(permission)) {
					selected.add(child);
				}
			}
			selected.addAll(findNodes(child, permissions));
		}

		return selected;
	}

	public void expandAllNodes() {
		expandOrColapseChild(root, true);
	}

	public void colapseAllNodes() {
		expandOrColapseChild(root, false);
	}

	public void selectAllNodes() {
		selectOrDeselectChild(root, true);
	}

	public void deselectAllNodes() {

		// The method is called twice in order to remove partial selection properly
		selectOrDeselectChild(root, false);
		selectOrDeselectChild(root, false);
	}

	/**
	 * Expand or collapse all children from a node, including.
	 * 
	 * @param node
	 *            Node to be expanded or collpsed.
	 * @param expand
	 *            Flag to indicate if the node must be expanded or collapsed.
	 */
	protected void expandOrColapseChild(TreeNode node, boolean expand) {
		node.setExpanded(expand);
		List<TreeNode> children = node.getChildren();
		for (TreeNode child : children) {
			expandOrColapseChild(child, expand);
		}
	}

	/**
	 * Marca ou desmarca todas os nodos filhos a partir de um n�.
	 * 
	 * @param node
	 *            Nodo a ser expandido ou colapsado.
	 * @param select
	 *            Flag para indicar se o n� deve ser selecionada ou n�o.
	 */
	protected void selectOrDeselectChild(TreeNode node, boolean select) {
		node.setSelected(select);
		node.setPartialSelected(false);
		List<TreeNode> children = node.getChildren();
		for (TreeNode child : children) {
			selectOrDeselectChild(child, select);
		}
	}

	/**
	 * Create a permission tree.
	 * 
	 * @param root
	 * @return Permission tree root (and its children).
	 * @throws Exception
	 */
	public void createTree(TreeNode root) {

		selectedNodes = null;
		Map<Permission, TreeNode> lookup = createPermissionNodeMap();

		lookup.put(null, root);

		for (Permission permission : PermissionEnum.values()) {
			Permission parentPermission = permission.getParent();
			TreeNode parentNode = lookup.get(parentPermission);
			TreeNode node = lookup.get(permission);
			node.setParent(parentNode);

			// I'm sure Primefaces 4 is missing something because by invoking node.setParent(parentNode), the children
			// are not defined on the parent
			// For more details, see https://code.google.com/p/primefaces/issues/detail?id=6364
			parentNode.getChildren().add(node);
		}
		this.root = root;
	}

	/**
	 * Create a map with the permission and its node.
	 * 
	 * @param permissions
	 * @param lookupTranslation
	 * @return
	 */
	private Map<Permission, TreeNode> createPermissionNodeMap() {
		Map<Permission, TreeNode> permissionNode = new HashMap<>();
		for (Permission permission : PermissionEnum.values()) {
			TreeNode node = new CheckboxTreeNode(permission);
			permissionNode.put(permission, node);
		}
		return permissionNode;
	}

	/**
	 * Get a node's children by selection.
	 * 
	 * @param node
	 * @param selectionType
	 * @return
	 */
	private Set<TreeNode> getNodesBySelection(TreeNode node, NodeTypeSelectionEnum selectionType) {
		Set<TreeNode> selected = new HashSet<>();
		if (checkNodeSelection(node, selectionType)) {
			selected.add(node);
		}
		for (TreeNode child : node.getChildren()) {
			if (checkNodeSelection(child, selectionType)) {
				selected.add(child);
			}
			selected.addAll(getNodesBySelection(child, selectionType));
		}
		return selected;
	}

	/**
	 * Check if the node matches a selection type.
	 * 
	 * @param node
	 * @param selectionType
	 * @return
	 */
	private boolean checkNodeSelection(TreeNode node, NodeTypeSelectionEnum selectionType) {
		if (node.getData() == null) {
			return false;
		}
		switch (selectionType) {
		case FULL_SELECTED:
			return node.isSelected();
		case PARTIAL_SELECTED:
			return node.isPartialSelected();
		case FULL_OR_PARTIAL_SELECTED:
			// During the development, TreeNode.isPartialSelected() returned 'true' while should return 'false'.
			// I know that feeling, man... It's really sad... :'(
			// So, the method hasAnyChildrenSelected(TreeNode) was created.
			// return node.isSelected() || node.isPartialSelected();
			return node.isSelected() || hasSelectedChildreen(node);
		default:
			return false;
		}
	}

	private boolean hasSelectedChildreen(TreeNode node) {
		List<TreeNode> children = node.getChildren();
		for (TreeNode child : children) {
			if (child.isSelected()) {
				return true;
			}
			if (hasSelectedChildreen(child)) {
				return true;
			}
		}
		return false;
	}
}
