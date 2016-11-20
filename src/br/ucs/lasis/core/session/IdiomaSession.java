package br.ucs.lasis.core.session;

import java.util.List;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Local
public interface IdiomaSession {

	void salvar(Idioma idioma) throws BusinessException;

	void exclui(Long id)  throws BusinessException;

	Idioma buscarPorId(long id) throws BusinessException;
	
	Idioma buscaPorLocale(String locale) throws BusinessException;

	List<Idioma> buscaTodos() throws BusinessException;

	public PagedResult<Idioma> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)  throws BusinessException;

}
