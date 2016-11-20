package br.ucs.lasis.core.view.jsf.backing;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.session.RamoAtividadeSession;

@Named("ramoAtividadeFormBack")
@ViewScoped
public class RamoAtividadeFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private RamoAtividade ramoAtividade;

	@Inject
	private RamoAtividadeSession ramoAtividadeSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.ramoAtividade = ramoAtividadeSession.buscarPorId(id);
		} else {
			this.ramoAtividade = new RamoAtividade();
		}
		this.definePrimeiraPagina();

	}

	public RamoAtividade getRamoAtividade() {
		return ramoAtividade;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}


	public String listagem() {
		return "listaRamosAtividades";
	}

	public String salva() {
		ramoAtividadeSession.salvar(this.ramoAtividade);
		return listagem();
	}

	public boolean isInsercao() {
		return null == this.ramoAtividade.getId();
	}

}