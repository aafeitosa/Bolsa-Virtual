package br.ucs.lasis.core.view.lazymodels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.session.EmpresaSession;

public class LazyEmpresaDataModel extends LazyDataModel<Empresa> {

	private static final long serialVersionUID = 1L;
	private EmpresaSession session;
	private Map<String, Object> parametros;

	public LazyEmpresaDataModel(EmpresaSession session) {
		this.session = session;
	}
	
	public LazyEmpresaDataModel(EmpresaSession session,  Map<String, Object> parametros) {
		this.session = session;
		this.parametros = parametros;
	}

	@Override
	public Empresa getRowData(String rowKey) {

		Long id = null;
		Empresa entidade = null;
		try {
			id = Long.parseLong(rowKey);
			entidade = this.session.buscarPorId(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return entidade;
	}

	@Override
	public Object getRowKey(Empresa entidade) {
		return entidade.getId();
	}

	@Override
	public List<Empresa> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		
		// dados
		List<Empresa> empresas = this.session.buscaTodos(first, pageSize, this.parametros);
		
		// rowCount
		this.setRowCount(this.session.getQuantidadeTotal(this.parametros));

		return empresas; 
	}
	
	
}