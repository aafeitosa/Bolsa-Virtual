package br.ucs.lasis.core.view.jsf.backing;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.UsuarioSession;

@Named("dialogSenhaBack")
@ViewScoped
public class UsuarioSenhaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Long idUsuario;

	private String senhaAtual;

	private String novaSenha;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	private Usuario usuarioAlvo;

	@PostConstruct
	public void init() {
		if (usuarioLogado != null) {
			this.idUsuario = usuarioLogado.getId();
		}
	}

	public Usuario getUsuario() {
		return usuarioAlvo;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public void alteraSenha(ActionEvent event) {

		try {
			if (!isUsuarioDiferenteLogado()) {
				usuarioSession.alteraSenha(this.idUsuario, this.senhaAtual,
						this.novaSenha);
			} else {
				usuarioSession.alteraSenha(this.idUsuario, this.novaSenha);
			}
			this.senhaAtual = null;
			this.novaSenha = null;
			RequestContext.getCurrentInstance().execute(
					"PF('senhaAlterada').show()");
		} catch (BusinessException e) {
			e.printStackTrace();
			this.showErrorMessage("Ocorreu um erro alterando a senha",
					e.getMessage());
		}
	}

	public void trocaSenha(ActionEvent event) {

		Long idUsuarioAlvo = getParametroId();
//		System.out.println("Pedido para trocar a senha do usuário "
//				+ idUsuarioAlvo);

		usuarioAlvo = usuarioSession.buscarPorId(idUsuarioAlvo);
		if (usuarioAlvo != null) {
//			System.out.println("Nome do usuário " + usuarioAlvo.getNome());
			idUsuario = usuarioAlvo.getId();
		}
	}

	public boolean isUsuarioDiferenteLogado() {
		return !usuarioLogado.equals(usuarioAlvo);
	}

}