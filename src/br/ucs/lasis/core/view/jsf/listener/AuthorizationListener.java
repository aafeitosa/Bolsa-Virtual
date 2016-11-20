package br.ucs.lasis.core.view.jsf.listener;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import br.ucs.lasis.core.view.enums.ViewEnum;
import br.ucs.lasis.core.view.jsf.backing.PermissaoBacking;

public class AuthorizationListener implements PhaseListener {

	private static final String PAGINA_NAO_AUTORIZADA = "/public/403.xhtml";
	private static final String LOGIN_PAGE = "/public/loginPage.xhtml";
	private static final String LOGIN_ERROR = "/public/loginError.xhtml";
	private static final String WELCOME_PAGE = "/secured/home.xhtml";

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PermissaoBacking sessionUserBacking;
	
	@Override
	public void afterPhase(PhaseEvent event) {

		FacesContext facesContext = event.getFacesContext();
		String paginaAtual = facesContext.getViewRoot().getViewId();

		if (paginaAtual.equals(LOGIN_PAGE) || paginaAtual.equals(WELCOME_PAGE) || paginaAtual.equals(LOGIN_ERROR)) {
			return;
		}

		ViewEnum viewEnum = ViewEnum.getByURL(paginaAtual);

		if (null == viewEnum) {
			NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
			navigationHandler.handleNavigation(facesContext, null, WELCOME_PAGE);
			return;
		}

		if (!sessionUserBacking.temPermissao(viewEnum.getPermission().getId())) {

			NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
			navigationHandler.handleNavigation(facesContext, null, PAGINA_NAO_AUTORIZADA);
		}

	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}