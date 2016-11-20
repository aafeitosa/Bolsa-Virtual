package br.ucs.lasis.core.view.lazymodels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.session.PerfilSession;

public class LazyPerfilDataModel extends LazyDataModel<Perfil> {

	private static final long serialVersionUID = 1L;
	private PerfilSession session;

	public LazyPerfilDataModel(PerfilSession session) {
		this.session = session;
	}

	@Override
	public Perfil getRowData(String rowKey) {

		Long id = null;
		Perfil entidade = null;
		try {
			id = Long.parseLong(rowKey);
			entidade = this.session.buscarPorId(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return entidade;
	}

	@Override
	public Object getRowKey(Perfil entidade) {
		return entidade.getId();
	}

	@Override
	public List<Perfil> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		// rowCount
		this.setRowCount(this.session.getQuantidadeTotal());
		// dados
		return this.session.buscaTodos(first, pageSize);
	}
}