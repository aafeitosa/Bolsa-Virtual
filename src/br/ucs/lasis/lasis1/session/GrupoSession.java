package br.ucs.lasis.lasis1.session;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.model.entity.Periodo;

@Local
public interface GrupoSession {

	Grupo buscaPorId(Long id);
	Grupo buscaPorIdComEmpresas(Long id);
	List<Grupo> buscaTodos();
	List<Grupo> buscaTodos(Integer start, Integer limit);
	List<Grupo> buscaTodos(Integer start, Integer limit, Map<String, Object> parametros);
	int getQuantidadeTotal();
	int getQuantidadeTotal(Map<String, Object> parametros);
	void exclui(Long id);
	void salva(Grupo grupo);
	PagedResult<Grupo> buscaTodosCom(GridLazyLoaderDTO gridLazyLoaderDTO)
			throws BusinessException;
	List<Empresa> buscaEmpresasSemGrupo();
	List<Usuario> buscaProfessores();
	Grupo buscaPrimeiroGrupoDaEmpresa(Periodo periodo, Empresa empresa);
	List<Empresa> buscaOutrasEmpresasDoGrupo(Empresa empresa);
	List<Grupo> buscaGruposDoUsuario();
	Set<Empresa> buscaEmpresasDoGrupo(Grupo grupo);
	int contaEmpresasDoGrupo(Grupo grupo);
	
}