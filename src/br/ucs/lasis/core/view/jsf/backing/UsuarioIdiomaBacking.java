package br.ucs.lasis.core.view.jsf.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.IdiomaSession;

@Named("dialogIdiomaBack")
@RequestScoped
public class UsuarioIdiomaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private List<Idioma> idiomas;

	@Inject
	private IdiomaSession idiomaSession;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	@PostConstruct
	public void init() {
		setIdiomas(idiomaSession.buscaTodos());
	}

	public List<Idioma> getIdiomas() {
		return idiomas;
	}

	public void setIdiomas(List<Idioma> idiomas) {
		this.idiomas = idiomas;
	}

	public void alteraSenha(ActionEvent event) {

		try {
//			usuarioSession.alteraSenha(this.idUsuario, this.senhaAtual,
//					this.novaSenha);
//			this.senhaAtual = null;
//			this.novaSenha = null;
			RequestContext.getCurrentInstance().execute(
					"PF('senhaAlterada').show()");
		} catch (BusinessException e) {
			this.showErrorMessage("Ocorreu um erro alterando a senha",
					e.getMessage());
		}
	}
}