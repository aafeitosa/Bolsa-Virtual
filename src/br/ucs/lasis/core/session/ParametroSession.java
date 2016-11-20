package br.ucs.lasis.core.session;

import java.util.List;

import javax.ejb.Local;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Parametro;
import br.ucs.lasis.core.model.dto.ParametroDTO;

@Local
public interface ParametroSession {

	void salvar(ParametroDTO parametroDTO) throws BusinessException;

	void exclui(Long id)  throws BusinessException;

	Parametro buscarPorId(long id) throws BusinessException;

	List<Parametro> buscaTodos() throws BusinessException;
	
	List<ParametroDTO> buscaTodosDTO() throws BusinessException;
	
	Parametro buscaPorTipo(ParametrosEnum tipo) throws BusinessException;

	List<Parametro> buscaTodos(Integer start, Integer limit) throws BusinessException;

	int getQuantidadeTotal() throws BusinessException;
	
}