package br.ucs.lasis.core.session;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Local
public interface RamoAtividadeSession {

	void salvar(RamoAtividade ramoAtividade) throws BusinessException;

	void exclui(Long id) throws BusinessException;

	RamoAtividade buscarPorId(long id) throws BusinessException;

	List<RamoAtividade> buscaTodos() throws BusinessException;

	List<RamoAtividade> buscaTodos(Integer start, Integer limit) throws BusinessException;

	List<RamoAtividade> buscaTodos(Integer start, Integer limit, Map<String, Object> parametros) throws BusinessException;
	
	int getQuantidadeTotal() throws BusinessException;
	
	int getQuantidadeTotal(Map<String, Object> parametros) throws BusinessException;
	
	PagedResult<RamoAtividade> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException;

}
