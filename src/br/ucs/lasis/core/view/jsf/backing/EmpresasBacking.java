package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.EmpresaSession;

@Named("empresasBack")
@ViewScoped
public class EmpresasBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private List<Empresa> empresasAtivas;

	@Inject
	private EmpresaSession empresaSession;
	
	@Inject 
	@UsuarioLogado
	private Usuario usuarioLogado;
	
	@PostConstruct
	public void init() {
		carregaEmpresas();
	}
	
	private void carregaEmpresas() {
		if(this.isEmpresaPrincipal()) {
			empresasAtivas = empresaSession.buscaTodasAtivas();
		} else {
			empresasAtivas = new ArrayList<Empresa>();
		}
	}
	
	public List<Empresa> getTodasEmpresasAtivas() {
		return empresasAtivas;
	}

}
