package br.ucs.lasis.core.session;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;

@Local
public interface EmpresaSession {

	void salvar(Empresa empresa) throws BusinessException;

	void salvar(Empresa empresa, boolean criarUsuario) throws BusinessException;
	
	void exclui(Long id)  throws BusinessException;

	Empresa buscarPorId(long id) throws BusinessException;

	List<Empresa> buscaTodos() throws BusinessException;

	List<Empresa> buscaTodasAtivas() throws BusinessException;
	
	List<Empresa> buscaTodos(Integer start, Integer limit) throws BusinessException;

	List<Empresa> buscaTodos(Integer start, Integer limit, Map<String, Object> parametros) throws BusinessException;
	
	int getQuantidadeTotal() throws BusinessException;
	
	int getQuantidadeTotal(Map<String, Object> parametros) throws BusinessException;

}
