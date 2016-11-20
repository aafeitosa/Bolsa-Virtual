package br.ucs.lasis.core.view.lazymodels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.session.UsuarioSession;

public class LazyUsuarioDataModel extends LazyDataModel<Usuario> {

	private static final long serialVersionUID = 1L;
	private UsuarioSession session;
	private Map<String, Object> parametros;

	public LazyUsuarioDataModel(UsuarioSession session) {
		this.session = session;
	}
	
	public LazyUsuarioDataModel(UsuarioSession session, Map<String, Object> parametros) {
		this.session = session;
		this.parametros = parametros;
	}

	@Override
	public Usuario getRowData(String rowKey) {

		Long id = null;
		Usuario entidade = null;
		try {
			id = Long.parseLong(rowKey);
			entidade = this.session.buscarPorId(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return entidade;
	}

	@Override
	public Object getRowKey(Usuario entidade) {
		return entidade.getId();
	}

	@Override
	public List<Usuario> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		// rowCount
		this.setRowCount(this.session.getQuantidadeTotal(this.parametros));
		// dados
		return this.session.buscaTodos(first, pageSize, this.parametros);
	}
}