package br.ucs.lasis.core.session;

import java.util.List;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Perfil;

@Local
public interface PerfilSession {

	void salvar(Perfil perfil) throws BusinessException;

	void exclui(Long id)  throws BusinessException;

	Perfil buscarPorId(long id) throws BusinessException;

	List<Perfil> buscaTodos() throws BusinessException;

	List<Perfil> buscaTodosExternos() throws BusinessException;
	
	List<Perfil> buscaTodos(Integer start, Integer limit) throws BusinessException;

	int getQuantidadeTotal() throws BusinessException;

}
