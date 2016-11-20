package br.ucs.lasis.core.session;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;

@Local
public interface SistemaSession {
	
	Long getIdEmpresaPrincipal() throws BusinessException;
	
	Empresa getEmpresaPrincipal() throws BusinessException;
	
}
