package br.ucs.lasis.core.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import br.ucs.lasis.core.Constantes;
import br.ucs.lasis.core.view.resourcebundle.TranslationsResourceBundle;

import com.google.common.base.Strings;

public final class FacesMessageUtils {

	private FacesMessageUtils() {
	}

	public static void addErrorMessage(String formattedMessageCode, String[] fieldLabelCodes) {
		FacesContext.getCurrentInstance().addMessage(Constantes.CLIENT_MESSAGE_WIDGET_ID,
						getErrorMessage(formattedMessageCode, fieldLabelCodes));
	}

	public static FacesMessage getErrorMessage(String formattedMessageCode, String... fieldLabelCodes) {

		String[] args = new String[fieldLabelCodes.length];

		int index = 0;
		for (String code : fieldLabelCodes) {
			args[index++] = getTranslation(code);
		}

		MessageFormat messageFormat = new MessageFormat(formattedMessageCode);
		String message = messageFormat.format(args);

		FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");

		return fmsg;

	}

	public static void addErrorMessageElement(String messageCode, String... componentsId) {
		addErrorMessageElements(null, messageCode, componentsId);
	}

	public static void addErrorMessageElements(String clientMessageWidgetId, String messageCode, String... componentsId) {

		if (Strings.isNullOrEmpty(clientMessageWidgetId)) {
			addErrorMessage(messageCode);
		} else {
			addErrorMessage(clientMessageWidgetId, messageCode);
		}

		for (int i = 0; i < componentsId.length; i++) {
			UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(componentsId[i]);
			if (comp != null && comp instanceof UIInput) {
				((UIInput) comp).setValid(false);
			}
		}

	}

	public static void addErrorMessageAppendedMessage(String messageCode, String appendedMessage) {
		addErrorMessageImpl(Constantes.CLIENT_MESSAGE_WIDGET_ID,
						createFacesMessage(FacesMessage.SEVERITY_ERROR, messageCode, appendedMessage));
	}

	public static void addErrorMessage(String messageCode) {
		addErrorMessage(Constantes.CLIENT_MESSAGE_WIDGET_ID, messageCode);
	}

	public static void addErrorMessage(String clientMessageWidgetId, String messageCode) {
		addErrorMessageImpl(clientMessageWidgetId, createFacesMessage(FacesMessage.SEVERITY_ERROR, messageCode));
	}

	public static void addInfoMessage(String clientMessageWidgetId, String messageCode) {
		addInfoMessageImpl(clientMessageWidgetId, messageCode);
	}

	public static void addInfoMessageGlobal(String messageCode) {
		addInfoMessageImpl(null, messageCode);
	}

	public static void addInfoMessage(String messageCode) {
		addInfoMessageImpl(Constantes.CLIENT_MESSAGE_WIDGET_ID, messageCode);
	}

	private static void addInfoMessageImpl(String clientMessageWidgetId, String messageCode) {
		FacesContext.getCurrentInstance().addMessage(clientMessageWidgetId, createFacesMessage(FacesMessage.SEVERITY_INFO, messageCode));
	}

	private static void addErrorMessageImpl(String clientMessageWidgetId, FacesMessage facesMessage) {
		FacesContext.getCurrentInstance().addMessage(clientMessageWidgetId, facesMessage);
	}

	public static FacesMessage createFacesMessage(Severity severity, String messageCode, String appendedMessage) {
		return new FacesMessage(severity, getTranslation(messageCode) + ": " + appendedMessage, "");
	}

	public static FacesMessage createFacesMessage(Severity severity, String messageCode) {
		return new FacesMessage(severity, getTranslation(messageCode), "");
	}

	public static String getTranslation(String code) {

		String retorno = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, Constantes.MESSAGE_BUNDLE_NAME);

		try {
			retorno = resourceBundle.getString(code);
		} catch (Exception e) {
			retorno = code;
		}

		return retorno;
	}

	public static String getTranslation(String localeCode, String code) {

		String retorno = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		TranslationsResourceBundle resourceBundle = (TranslationsResourceBundle) facesContext.getApplication().getResourceBundle(facesContext,
						Constantes.MESSAGE_BUNDLE_NAME);
		resourceBundle.getTranslation(localeCode, code);

		try {
			retorno = resourceBundle.getString(code);
		} catch (Exception e) {
			retorno = code;
		}

		return retorno;
	}

}
