package br.ucs.lasis.core.session;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.PerfilPermissao;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.lasis1.model.entity.Periodo;

@Local
public interface UsuarioSession {

	void salvar(Usuario usuario) throws BusinessException;
	
	void alteraSenha(Long idUsuario, String senhaAtual, String novaSenha) throws BusinessException;

	void alteraSenha(Long idUsuario, String novaSenha) throws BusinessException;
	
	void exclui(Long id) throws BusinessException;

	Usuario buscarPorId(long id) throws BusinessException;

	List<Usuario> buscaTodos() throws BusinessException;

	List<Usuario> buscaTodos(Integer start, Integer limit)
			throws BusinessException;

	List<Usuario> buscaTodos(Integer start, Integer limit,
			Map<String, Object> parametros) throws BusinessException;
	
	List<Usuario> buscaTodos(Integer start, Integer limit,
			Periodo periodo) throws BusinessException;

	int getQuantidadeTotal() throws BusinessException;

	int getQuantidadeTotal(Map<String, Object> parametros)
			throws BusinessException;

	List<Usuario> buscaNomeComecandoPor(String nome) throws BusinessException;

	List<Usuario> buscaComPerfil(Perfil perfil) throws BusinessException;
	
	Usuario buscaPorLogin(String login) throws BusinessException;

	List<PerfilPermissao> buscarPermissoes(Usuario usuario)
			throws BusinessException;
	
	boolean temPermissao(String viewId) throws BusinessException;

	String geraNovaSenha(Usuario usuario) throws BusinessException;

	String criptografarNovaSenha(String login, String novaSenha) throws BusinessException;
	
	List<Usuario> buscaTodosDaEmpresa(Empresa empresa) throws BusinessException;

}
