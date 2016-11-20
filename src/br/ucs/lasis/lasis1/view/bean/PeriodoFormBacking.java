package br.ucs.lasis.lasis1.view.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Named("periodoFormBack")
@ViewScoped
public class PeriodoFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Periodo periodo;
	
	@EJB
	private PeriodoSession periodoSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.periodo = periodoSession.buscaPorId(id);
		} else {
			this.periodo = new Periodo();
		}
		
		this.editar();
		this.definePrimeiraPagina();
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setGrupo(Periodo periodo) {
		this.periodo = periodo;
	}

	public String listagem() {
		return "listaPeriodos";
	}

	public String salva() {
		periodoSession.salva(this.periodo);
			return listagem();
	}

	public boolean isInsercao() {
		return null == this.periodo.getId();
	}

	public void editar() {

	}
	
}