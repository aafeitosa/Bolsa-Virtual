package br.ucs.lasis.lasis1.session;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Investimento;
import br.ucs.lasis.lasis1.model.entity.ResultadoRodada;
import br.ucs.lasis.lasis1.model.entity.Rodada;
import br.ucs.lasis.lasis1.model.entity.RodadaVariacao;

@Local
public interface RodadaSession {

	Rodada buscaPorId(Long id);
	List<Rodada> buscaTodos();
	List<Rodada> buscaTodos(Integer start, Integer limit);
	List<Rodada> buscaTodos(Integer start, Integer limit, Map<String, Object> parametros);
	int getQuantidadeTotal();
	int getQuantidadeTotal(Map<String, Object> parametros);
	void exclui(Long id) throws BusinessException;
	void salva(Rodada rodada);
	PagedResult<Rodada> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException;
	Integer getMaiorNumeroNaData(Date data, Grupo grupo);
	Integer getProximoNumeroDoGrupo(Grupo grupo);
	List<Grupo> buscaGruposDoUsuario();
	List<Investimento> buscaInvestimentosDaRodadaEEmpresa(Rodada rodada, Empresa empresa);
	void salvaInvestimentos(List<Investimento> investimentos) throws BusinessException;
	List<ResultadoRodada> buscaResultadosDaRodada(Rodada rodada);
	List<ResultadoRodada> buscaResultadosDoGrupo(Grupo grupo);
	List<ResultadoRodada> buscaResultadosDaEmpresa(Empresa empresa);
	List<RodadaVariacao> buscaVariacoesDoGrupo(Grupo grupo);
	void salvaResultadosDaRodada(Rodada rodada);
	List<RodadaVariacao> inicializaVariacoes(Grupo grupo);
	Set<RodadaVariacao> buscaVariacoesDaRodada(Rodada rodada);
	Rodada buscaRodadaAnterior(Rodada rodada);
	ResultadoRodada buscaResultadoRodada(Rodada rodada, Empresa empresa);
	ResultadoRodada buscaUltimoResultado(Empresa empresa);

	
}