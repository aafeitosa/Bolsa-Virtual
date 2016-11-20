package br.ucs.lasis.core.ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.session.EmpresaSession;
import br.ucs.lasis.core.session.SistemaSession;

@Stateless
public class SistemaSessionBean implements SistemaSession {

	@Inject
	private ParametrosSingletonBean parametrosSingletonBean;

	@Inject
	private EmpresaSession empresaSession;

	@Override
	public Long getIdEmpresaPrincipal() throws BusinessException {
		return parametrosSingletonBean.buscarValorParametroAsId(ParametrosEnum.ID_EMPRESA_PRINCIPAL);
	}
	
	@Override
	public Empresa getEmpresaPrincipal() throws BusinessException {
		
		Long idEmpresa = this.getIdEmpresaPrincipal();
		Empresa empresa = null;
		
		if(idEmpresa!=null) {
			empresa = empresaSession.buscarPorId(idEmpresa);
		}
		
		return empresa;
	}
}