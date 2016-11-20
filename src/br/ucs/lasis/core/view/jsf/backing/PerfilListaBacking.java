package br.ucs.lasis.core.view.jsf.backing;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.session.PerfilSession;
import br.ucs.lasis.core.view.lazymodels.LazyPerfilDataModel;


@Named("perfilListaBack")
@ViewScoped 
public class PerfilListaBacking extends AbstractBacking{

	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Perfil> dataModel;

	@Inject
	private PerfilSession perfilSession;
	
	@PostConstruct
	public void init(){
		this.dataModel = new LazyPerfilDataModel(perfilSession);
		this.definePrimeiraPagina();
	}
	
	public LazyDataModel<Perfil> getDataModel() {
		return this.dataModel;
	}
	
	public String novo() {
		return "formPerfis";
	}
	
	public String listagem() {
		return "listaPerfis";
	}
	
	public void exclui(ActionEvent event) {
		try {
			perfilSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}
}
