package br.ucs.lasis.core.view.jsf.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.session.RamoAtividadeSession;

@Named("ramosAtividadesBack")
@ViewScoped
public class RamosAtividadesBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private List<RamoAtividade> ramosAtividades;

	@Inject
	private RamoAtividadeSession ramoAtividadeSession;
	
	@PostConstruct
	public void init() {
		this.ramosAtividades = ramoAtividadeSession.buscaTodos();
	}
	
	public List<RamoAtividade> getTodosRamosAtividades() {
		return ramosAtividades;
	}
}
