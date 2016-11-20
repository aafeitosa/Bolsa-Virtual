package br.ucs.lasis.core.session;

import java.util.Map;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Traducao;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;

@Local
public interface TraducaoSession {
	
	Map<String, String> findMapaTraducoes(Idioma idioma);

	PagedResult<Traducao> findTranslationsWith(GridLazyLoaderDTO gridLazyLoaderDTO);

	void update(Traducao translation) throws BusinessException;

	void exclui(Long id) throws BusinessException;
	
	Traducao findById(Long primaryKey);

	Traducao findByCodigoEIdioma(String codigo, Idioma idioma);

	Map<String, String> findMapaTraducoes(String localeCode);
	
	Idioma getIdiomaPadrao() throws BusinessException;

}
