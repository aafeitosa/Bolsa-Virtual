package br.ucs.lasis.core.view.jsf.backing;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.core.view.lazymodels.LazyUsuarioDataModel;


@Named("usuarioListaBack")
@ViewScoped 
public class UsuarioListaBacking extends AbstractBacking{

	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Usuario> dataModel;
	
	private Usuario usuario;
	
	private Map<String, Object> parametros;

	@Inject
	private UsuarioSession usuarioSession;
	
	@PostConstruct
	public void init(){
		this.parametros = new HashMap<String, Object>();
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		this.pesquisar(null);
		this.dataModel = new LazyUsuarioDataModel(usuarioSession, this.parametros);
		this.definePrimeiraPagina();
	}
	
	public LazyDataModel<Usuario> getDataModel() {
		return this.dataModel;
	}
		
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String novo() {
		return "formUsuarios";
	}
	
	public String listagem() {
		return "listaUsuarios";
	}
	
	public void exclui(ActionEvent event) {
		try {
			usuarioSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}
	
	public void pesquisar(ActionEvent event) {
		
		this.parametros.put("empresa", usuario.getEmpresa());
		
		if (usuario.getNome() != null) {
			this.parametros.put("nome", usuario.getNome());
		}

		this.parametros.put("ativo", usuario.isAtivo());
		
	}
}
