package br.ucs.lasis.lasis1.session;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Periodo;

@Local
public interface PeriodoSession {

	Periodo buscaPorId(Long id);
	Periodo buscaPorData(Date date);
	List<Periodo> buscaTodos();
	List<Periodo> buscaTodos(Integer start, Integer limit);
	List<Periodo> buscaTodos(Integer start, Integer limit, Map<String, Object> parametros);
	int getQuantidadeTotal();
	int getQuantidadeTotal(Map<String, Object> parametros);
	void exclui(Long id);
	void salva(Periodo periodo);
	PagedResult<Periodo> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException;
	
}