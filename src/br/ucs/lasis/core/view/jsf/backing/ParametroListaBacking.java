package br.ucs.lasis.core.view.jsf.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.ucs.lasis.core.model.Parametro;
import br.ucs.lasis.core.model.dto.ParametroDTO;
import br.ucs.lasis.core.session.ParametroSession;


@Named("parametroListaBack")
@ViewScoped 
public class ParametroListaBacking extends AbstractBacking{

	private static final long serialVersionUID = 1L;
	
	private Parametro parametro;
	
	private List<ParametroDTO> parametros;
	
	@Inject
	private ParametroSession parametroSession;
	
	@PostConstruct
	public void init() {
		this.parametros = parametroSession.buscaTodosDTO();
	}
	
	public Parametro getParametro() {
		return parametro;
	}
	
	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public List<ParametroDTO> getParametros() {
		return parametros;
	}

	public String listagem() {
		return "listaParametros";
	}
	
//	public void salva(ActionEvent event) {
//		try {
//			parametroSession.salvar(this.parametro);
//		} catch (Exception e) {
//			this.showConstraintError();
//		}
//	}
	
    public void onRowEdit(RowEditEvent event) {
    	
    	ParametroDTO dto = ((ParametroDTO) event.getObject());
    	
    	try {
			parametroSession.salvar(dto);
			this.showInfoMessage("Editado", dto.getTipo());
    	} catch (Exception e) {
			this.showConstraintError();
		}
    	
    }
     
    public void onRowCancel(RowEditEvent event) {
    	this.showInfoMessage("Edição Cancelada", "");
    }

}