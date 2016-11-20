package br.ucs.lasis.core.view.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ucs.lasis.core.session.UsuarioSession;
import br.ucs.lasis.core.view.enums.ViewEnum;

public class AuthorizationFilter implements Filter {

	private static final String PASTA_SEGURA = "/secured/";

	private static final String PAGINA_NAO_AUTORIZADA = "/public/403.xhtml";
	private static final String PAGINA_NAO_ENCONTRADA = "/public/404.xhtml";
	private static final String LOGIN_PAGE = "/public/login.xhtml";
	private static final String LOGIN_ERROR = "/public/loginError.xhtml";
	private static final String WELCOME_PAGE = "/secured/home.xhtml";

	@Inject
	private UsuarioSession usuarioSession;

	/**
	 * Checks if user is logged in. If not it redirects to the login page.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String paginaAtual = getViewId(req.getRequestURI());
		boolean saiu = false;
		
		request.setCharacterEncoding("UTF-8");

		if (paginaAtual.equals(LOGIN_PAGE) || paginaAtual.equals(LOGIN_ERROR)
				|| paginaAtual.equals(WELCOME_PAGE)) {
			chain.doFilter(request, response);
			saiu = true;
		}

		ViewEnum viewEnum = ViewEnum.getByURL(paginaAtual);

		if (null == viewEnum) {
			if (!redirect(request, response, PAGINA_NAO_ENCONTRADA)) {
				chain.doFilter(request, response);
				saiu = true;
			}
		}

		if (viewEnum != null
				&& !usuarioSession.temPermissao(viewEnum.getPermission()
						.getId())) {
			if (!redirect(request, response, PAGINA_NAO_AUTORIZADA)) {
				chain.doFilter(request, response);
				saiu = true;
			}
		}

		if (!saiu) {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig config) throws ServletException {
		// Nothing to do here!
	}

	public void destroy() {
		// Nothing to do here!
	}

	public String getViewId(String uri) {

		String viewId = uri.substring(uri.indexOf(PASTA_SEGURA));
		viewId = viewId.replace(".jsf", ".xhtml");

		return viewId;
	}

	// Gera o erro UT010019: Response already commited
	// Bug documentado do Widfly : https://issues.jboss.org/browse/WFLY-3664
	// Aguardando solução
	public boolean redirect(ServletRequest request, ServletResponse response,
			String pagina) {

		String contextPath = ((HttpServletRequest) request).getContextPath();
		try {
			((HttpServletResponse) response).sendRedirect(contextPath + pagina);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
