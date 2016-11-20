package br.ucs.lasis.core.view.jsf.backing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Named("periodosBack")
@ViewScoped
public class PeriodosBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private List<Periodo> periodos;

	@Inject
	private PeriodoSession periodoSession;
	
	@PostConstruct
	public void init() {
		carregaPeriodos();
	}
	
	private void carregaPeriodos() {
		if(this.isEmpresaPrincipal()) {
			periodos = periodoSession.buscaTodos();
		} else {
			periodos = new ArrayList<Periodo>();
		}
	}
	
	public List<Periodo> getTodosPeriodos() {
		return periodos;
	}

}
