package br.ucs.lasis.core.view.jsf.backing;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.producer.UsuarioLogadoProducer;
import br.ucs.lasis.core.qualifier.PeriodoCorrente;
import br.ucs.lasis.core.session.MailSession;
import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.core.util.BackingUtils;
import br.ucs.lasis.core.util.StringUtils;
import br.ucs.lasis.lasis1.model.entity.Periodo;
import br.ucs.lasis.lasis1.session.PeriodoSession;

@Named("loginBack")
@SessionScoped
public class LoginBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	private MailSession mailSession;

	@Inject
	private PeriodoSession periodoSession;
	
	@Inject
	@PeriodoCorrente
	private Periodo periodoCorrente;
	
	private Periodo periodo;

	private String user;
	private String password;
	private Usuario usuario;
	private Idioma idioma;

	private List<Periodo> periodos;
	
	@PostConstruct
	public void init() {
		this.periodos = periodoSession.buscaTodos();
		this.periodo = periodoCorrente;
	}
	
	public LoginBacking() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}
	
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}

	public String getPeriodoCorrente() {
		return periodoCorrente!=null ? periodoCorrente.getNome() : "";
	}
	
	public Periodo getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	
	public void mudaPeriodo(ValueChangeEvent event) throws IOException {
//		System.out.println("Muda Período");
		Periodo p = (Periodo) event.getNewValue();
		this.periodoCorrente = p;
		this.periodo = p;
		UsuarioLogadoProducer producer = (UsuarioLogadoProducer) BackingUtils.getBackingBean("geralProducer");
		if(producer!=null) {
//			System.out.println("Alterou no produtor para " + p);
			producer.setPeriodoCorrente(p);
		}
//		System.out.println("Novo período " + producer.getPeriodoCorrente());
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/secured/home");
	}

	public String logout() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		session.invalidate();

		try {
			request.logout();
			usuario = null;
			user = null;
			password = null;

		} catch (ServletException e) {
			e.printStackTrace();
		}
		return "/public/login.jsf";
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		if(context.getExternalContext().getUserPrincipal()!=null) {
			return "/secured/home.jsf";
		}

		try {

			usuario = usuarioSession.buscaPorLogin(user);
			
			if (usuario == null) {
				this.showErrorMessage("Problema de autenticação",
						"O usuário ou a senha informados estão incorretos.");
				return null;
			}

			if (!usuario.isAtivo()) {
				this.showErrorMessage("Problema de autenticação",
						"O usuário informado está inativo.");
				return null;
			}

			String crypt = StringUtils.getHash(user + password);

			request.login(user, crypt);

			this.idioma = usuario.getEmpresa().getIdioma();

			return "/secured/home.jsf";

		} catch (ServletException e) {
			this.showErrorMessage("Problema de autenticação",
					"O usuário ou a senha informados estão incorretos.");
			return "/public/login.jsf";
		} catch (BusinessException e) {
			this.showErrorMessage("Problema de autenticação",
					"O usuário ou a senha informados estão incorretos.");
			return "/public/login.jsf";
		}
	}

	public Usuario getUsuarioLogado() {
		return usuario;
	}

	public String home() {
		return "/secured/home";
	}

	public String getNomeUsuarioLogado() {
		return this.usuario != null ? this.usuario.getNome() : null;
	}
	
	public Long getIdUsuarioLogado() {
		return this.usuario != null ? this.usuario.getId() : null;
	}

	public String getNomeEmpresaUsuarioLogado() {
		return this.usuario != null ? this.usuario.getEmpresa().getNome()
				: null;
	}

	public Empresa getEmpresaUsuarioLogado() {
		return this.usuario != null ? this.usuario.getEmpresa() : null;
	}

	public String getRemetente() {
		return this.getParametrosSingletonBean().buscarValorParametroAsString(
				ParametrosEnum.REMETENTE_EMAIL);
	}

	public String getTituloEmail() {
		return this.getParametrosSingletonBean().buscarValorParametroAsString(
				ParametrosEnum.TITULO_EMAIL);
	}

	public String getMensagemEmail() {
		return this.getParametrosSingletonBean().buscarValorParametroAsString(
				ParametrosEnum.MENSAGEM_EMAIL);
	}

	public void janelaConfirmacaoEsqueciMinhaSenha() {

		if (user.isEmpty()) {
			this.showInfoMessage(this.getTranslation("en_US", "aviso"),
					this.getTranslation("en_US", "usuario_nao_preenchido"));
		} else {
			usuario = usuarioSession.buscaPorLogin(user);
			if (usuario == null) {
				this.showErrorMessage(this.getTranslation("en_US", "aviso"),
						this.getTranslation("en_US", "usuario_nao_existe"));
			} else if (!usuario.isAtivo()) {
				this.showErrorMessage(this.getTranslation("en_US", "aviso"),
						this.getTranslation("en_US", "usuario_inativo"));
			} else {
				RequestContext.getCurrentInstance().execute(
						"PF('confirmaw').show()");
			}
		}
	}

	public void esqueciMinhaSenha() {

		Usuario usuarioSemSenha = usuarioSession.buscaPorLogin(user);
		String novaSenha = usuarioSession.geraNovaSenha(usuarioSemSenha);
		usuarioSemSenha.setPassword(usuarioSession.criptografarNovaSenha(
				usuarioSemSenha.getLogin(), novaSenha));

		Boolean enviado = enviarEmailNovaSenha(usuarioSemSenha.getEmail(),
				novaSenha);
		if (enviado) {
			usuarioSession.salvar(usuarioSemSenha);
			RequestContext.getCurrentInstance().execute("PF('avisow').show()");
		} else {
			RequestContext.getCurrentInstance().execute(
					"PF('erroEnviow').show()");
		}
	}

	public Boolean enviarEmailNovaSenha(String email, String novaSenha) {

		if (email != null && novaSenha != null && getTituloEmail() != null
				&& getMensagemEmail() != null) {
			try {
				mailSession.sendHTMLMessage(email, getTituloEmail(),
						getMensagemEmail() + novaSenha);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

}