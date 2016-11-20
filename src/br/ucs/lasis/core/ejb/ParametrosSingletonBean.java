package br.ucs.lasis.core.ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Parametro;
import br.ucs.lasis.core.session.ParametroSession;

@Singleton
public class ParametrosSingletonBean {

	private List<Parametro> parametros;
	
	@Inject 
	private ParametroSession parametroSession;
	
	@PostConstruct
	public void init() {
		this.buscaListaParametros();
	}
	
	private void buscaListaParametros() {
		parametros = parametroSession.buscaTodos();
	}
	
	public void atualizaLista(Parametro parametro) {
		
		int indice = -1;
		
		for (int i = 0; i < parametros.size(); i++) {
			Parametro param = parametros.get(i);
			
			if(param.equals(parametro)){
				indice = i;
				break;
			}
		}
		
		if(indice>0) {
			parametros.set(indice, parametro);
		} else {
			parametros.add(parametro);
		}
	}
	
	public void listener(@Observes Parametro parametro) {
		this.atualizaLista(parametro);
	} 
		
	private Parametro buscaPorTipo(ParametrosEnum param) {
		
		for (Parametro parametro : parametros) {
			if(param.equals(parametro.getTipo())){
				return parametro;
			}
		}
		
		return null;
	}
	
	
	public String buscarValorParametroAsString(ParametrosEnum param) {
		Parametro parametro = this.buscaPorTipo(param);
		if(parametro!=null) {
			return parametro.getValor();
		} 
		return null;
	}
	
	public Boolean buscarValorParametroAsBoolean(ParametrosEnum param) throws BusinessException	{

		// Declara o retorno
		Boolean retorno = null;

		// Busca o parâmetro
		String parameter = buscarValorParametroAsString(param);

		// Verifica se foi encontrado
		if (parameter != null) {

			// Converter para Boolean
			try {
				retorno = Boolean.parseBoolean(parameter);
			} catch (Exception e) {
				throw new BusinessException("Erro ao converter o parâmetro " + param + " para Boolean", e);
			}
		}

		// Retorna
		return retorno;
	}
	
	public Integer buscarValorParametroAsInteger(ParametrosEnum param) throws BusinessException	{

		// Declara o retorno
		Integer retorno = null;

		// Busca o parâmetro
		String parameter = buscarValorParametroAsString(param);

		// Verifica se foi encontrado
		if (parameter != null) {

			// Converter para Integer
			try {
				retorno = Integer.parseInt(parameter);
			} catch (Exception e) {
				throw new BusinessException("Erro ao converter o parâmetro " + param + " para Integer", e);
			}
		}

		// Retorna
		return retorno;
	}
	
	public Long buscarValorParametroAsId(ParametrosEnum param) throws BusinessException	{

		// Declara o retorno
		Long retorno = null;

		// Busca o parâmetro
		String parameter = buscarValorParametroAsString(param);

		// Verifica se foi encontrado
		if (parameter != null) {

			// Converter para Long
			try {
				retorno = Long.parseLong(parameter);
			} catch (Exception e) {
				throw new BusinessException("Erro ao converter o parâmetro " + param + " para Long", e);
			}
		}

		// Retorna
		return retorno;
	}
	
}
