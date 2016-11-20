package br.ucs.lasis.core.view.jsf.backing;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.session.EmpresaSession;
import br.ucs.lasis.core.view.lazymodels.LazyEmpresaDataModel;


@Named("empresaListaBack")
@ViewScoped 
public class EmpresaListaBacking extends AbstractBacking{

	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Empresa> dataModel;
	
	private Empresa empresa;
	
	private Map<String, Object> parametros;

	@Inject
	private EmpresaSession empresaSession;
	
	@PostConstruct
	public void init(){
		this.parametros = new HashMap<String, Object>();
		this.empresa = new Empresa();
		this.empresa.setAtivo(true);
		this.pesquisar(null);
		this.dataModel = new LazyEmpresaDataModel(empresaSession, parametros);
		this.definePrimeiraPagina();
	}
	
	public LazyDataModel<Empresa> getDataModel() {
		return this.dataModel;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String novo() {
		return "formEmpresas";
	}
	
	public String listagem() {
		return "listaEmpresas";
	}
	
	public void exclui(ActionEvent event) {
		
		try {
			empresaSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}
	
	public void pesquisar(ActionEvent event) {
		
		if (empresa.getNome() != null) {
			this.parametros.put("nome", empresa.getNome());
		}

		this.parametros.put("ativo", empresa.getAtivo());
		
	}
}
