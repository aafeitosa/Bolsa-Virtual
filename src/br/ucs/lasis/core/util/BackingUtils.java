package br.ucs.lasis.core.util;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

public class BackingUtils {

	public static Object getBackingBean(String name) {
		return getBackingBean(FacesContext.getCurrentInstance(), name);
	}

	public static Object getBackingBean(FacesContext facesContext, String name) {
		ELContext elContext = facesContext.getELContext();
		ELResolver elResolver = elContext.getELResolver();
		return elResolver.getValue(elContext, null, name);
	}
	
}
