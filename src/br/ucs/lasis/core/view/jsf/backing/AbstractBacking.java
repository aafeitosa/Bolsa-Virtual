package br.ucs.lasis.core.view.jsf.backing;

import java.io.Serializable;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import br.ucs.lasis.core.Constantes;
import br.ucs.lasis.core.ejb.ParametrosSingletonBean;
import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.qualifier.EmpresaLogada;
import br.ucs.lasis.core.qualifier.EmpresaPrincipal;
import br.ucs.lasis.core.view.resourcebundle.TranslationsResourceBundle;

public class AbstractBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ParametrosSingletonBean parametrosSingletonBean;

	@Inject
	@EmpresaPrincipal
	private boolean isEmpresaPrincipal;

	@Inject
	@EmpresaLogada
	private Empresa empresaLogada;

	// para retornar a página correta das listagens quando editando registros
	private int first;

	// O número de registros usados na página
	private int nroRegistrosPorPagina;

	@PostConstruct
	public void postConstruct() {
		this.nroRegistrosPorPagina = parametrosSingletonBean
				.buscarValorParametroAsInteger(ParametrosEnum.NRO_REGISTROS_POR_PAGINA);
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getNroRegistrosPorPagina() {
		return nroRegistrosPorPagina;
	}

	public void setNroRegistrosPorPagina(int nroRegistrosPorPagina) {
		this.nroRegistrosPorPagina = nroRegistrosPorPagina;
	}

	protected void definePrimeiraPagina() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String sFirst = (String) fc.getExternalContext()
				.getRequestParameterMap().get("first");
		if (sFirst != null) {
			this.setFirst(Integer.parseInt(sFirst));
		}
		String sNro = (String) fc.getExternalContext().getRequestParameterMap()
				.get("nro");
		if (sNro != null) {
			this.setNroRegistrosPorPagina(Integer.parseInt(sNro));
		}
	}

	public String getOpcoesRegistrosPorPagina() {
		String opcoes = parametrosSingletonBean
				.buscarValorParametroAsString(ParametrosEnum.OPCOES_REGISTROS_POR_PAGINA);
		return opcoes != null ? opcoes
				: Constantes.OPCOES_REGISTROS_POR_PAGINA_DEFAULT;
	}

	protected Long getParametroId() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String sId = (String) fc.getExternalContext().getRequestParameterMap()
				.get("id");
		if (sId != null && !sId.isEmpty()) {
			return Long.parseLong(sId);
		}
		return null;
	}

	protected Long getParametroLong(String param) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String sId = (String) fc.getExternalContext().getRequestParameterMap()
				.get(param);
		if (sId != null && !sId.isEmpty()) {
			try {
				return Long.parseLong(sId);
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
		return null;
	}

	// Tratamento de mensagens de erro

	protected void showInfoMessage(String clientId, String summary,
			String detail) {
		this.showMessage(FacesMessage.SEVERITY_INFO, clientId, summary, detail);
	}

	protected void showInfoMessage(String summary, String detail) {
		this.showInfoMessage(null, summary, detail);
	}

	protected void showWarningMessage(String clientId, String summary,
			String detail) {
		this.showMessage(FacesMessage.SEVERITY_WARN, clientId, summary, detail);
	}

	protected void showWarningMessage(String summary, String detail) {
		this.showWarningMessage(null, summary, detail);
	}

	protected void showErrorMessage(String clientId, String summary,
			String detail) {
		this.showMessage(FacesMessage.SEVERITY_ERROR, clientId, summary, detail);
	}

	protected void showErrorMessage(String summary, String detail) {
		this.showErrorMessage(null, summary, detail);
	}

	private void showMessage(Severity severity, String clientId,
			String summary, String detail) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				summary, detail);
		fc.addMessage(clientId, message);
	}

	protected boolean hasMessages() {
		FacesContext fc = FacesContext.getCurrentInstance();
		return !fc.getMessageList().isEmpty();
	}

	public void showConstraintError() {
		this.showErrorMessage(Constantes.ERRO, Constantes.MENSAGEM_CONSTRAINT);
	}

	// Fim tratamento mensagens de erro

	public String getContext() {
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		return servletContext.getContextPath();
	}

	public boolean isEmpresaPrincipal() {
		return isEmpresaPrincipal;
	}

	public Empresa getEmpresaLogada() {
		return empresaLogada;
	}

	public ParametrosSingletonBean getParametrosSingletonBean() {
		return parametrosSingletonBean;
	}

	public String getTranslation(String code) {
		String localeCode = this.getEmpresaLogada().getIdioma().getLocale();
		return getTranslation(localeCode, code);
	}

	public String getTranslation(String localeCode, String code) {

		String retorno = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		TranslationsResourceBundle resourceBundle = (TranslationsResourceBundle) facesContext
				.getApplication().getResourceBundle(facesContext,
						Constantes.MESSAGE_BUNDLE_NAME);

		resourceBundle.getTranslation(localeCode, code);

		try {
			retorno = resourceBundle.getString(code);
		} catch (Exception e) {
			retorno = code;
		}

		return retorno;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

}
