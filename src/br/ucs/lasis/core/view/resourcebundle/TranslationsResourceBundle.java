package br.ucs.lasis.core.view.resourcebundle;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import sun.util.ResourceBundleEnumeration;
import br.ucs.lasis.core.Constantes;
import br.ucs.lasis.core.ejb.ParametrosSingletonBean;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Traducao;
import br.ucs.lasis.core.session.TraducaoSession;
import br.ucs.lasis.core.util.BackingUtils;
import br.ucs.lasis.core.view.jsf.backing.LoginBacking;

import com.google.common.base.Strings;

@SuppressWarnings("restriction")
/**
 * Implements a Resource bundle which get it's data from the database, regarding user language
 * 
 * @author alexandre.krohn
 * @since Dec 2013 - Atualizado Fev 2015
 *
 */
public class TranslationsResourceBundle extends ResourceBundle {

	private TraducaoSession traducaoSession;
	
	private ParametrosSingletonBean parametrosSingletonBean;

	private final Map<String, Map<String, String>> resources = new HashMap<String, Map<String, String>>();

	public TranslationsResourceBundle() {
		lookupTranslationDao();
	}

	/*
	 * Do the JNDI lookup of the translations bean. CDI seems not to work in this context.
	 */
	private void lookupTranslationDao() {
		
		String jndiName = "java:global/" + Constantes.ARTIFACT_NAME + "/TraducaoSessionBean";
		
		if (traducaoSession == null) {
			try {
				traducaoSession = (TraducaoSession) new InitialContext().lookup(jndiName);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the contents of the translations, based on the language from the logged user
	 * 
	 * @return An Object[][], where the first dimension represents the code and the second represents the translation
	 *         for this code in the user language
	 */
	protected Object[][] getContents() {

		Idioma idioma = getUserLanguage();
		String localeString = idioma.getLocale();

		Map<String, String> map = null;
		map = resources.get(localeString);
		if (map == null) {
			if (traducaoSession != null) {
				map = traducaoSession.findMapaTraducoes(idioma);
				resources.put(localeString, map);
			}
		}

		return toObjectArray(resources.get(localeString));
	}

	/*
	 * Convert a Map<String,String> to an bidimensional Object array
	 */
	private Object[][] toObjectArray(Map<String, String> map) {

		Object[][] twoDarray;

		if (map != null) {
			twoDarray = new String[map.size()][2];
			Object[] keys = map.keySet().toArray();
			for (int row = 0; row < twoDarray.length; row++) {
				twoDarray[row][0] = keys[row];
				twoDarray[row][1] = map.get(keys[row]);
			}
		} else {
			twoDarray = new String[0][];
		}
		return twoDarray;
	}

	/**
	 * Get the language from the logged user
	 * 
	 * @return the Language object from the logged user
	 */
	private Idioma getUserLanguage() {
		LoginBacking loginBack = (LoginBacking) BackingUtils.getBackingBean("loginBack");
		Empresa empresa = loginBack.getEmpresaUsuarioLogado();
		if(empresa!=null) {
			
			Idioma idiomaSelecionado = loginBack.getIdioma();
			Idioma idiomaEmpresa = empresa.getIdioma(); 
			
			if (idiomaSelecionado != null) {
				return (!idiomaSelecionado.equals(idiomaEmpresa)?idiomaSelecionado:idiomaEmpresa);
			}
			
			return idiomaEmpresa;
			
		} else {
			return this.getIdiomaPadrao();
		}
	}

	private Idioma getIdiomaPadrao() {
		return traducaoSession.getIdiomaPadrao();
	}
	
	/**
	 * Get the locale String from the logged user
	 * 
	 * @return a String representing a locale
	 */
	private String getUserLocaleString() {
		Idioma idioma = getUserLanguage();
		if (idioma != null) {
			return idioma.getLocale();
		} else {
			return null;
		}
	}

	/**
	 * Returns an <code>Enumeration</code> of the keys contained in
	 * this <code>ResourceBundle</code> and its parent bundles.
	 * 
	 * @return an <code>Enumeration</code> of the keys contained in
	 *         this <code>ResourceBundle</code> and its parent bundles.
	 * @see #keySet()
	 */
	@Override
	public Enumeration<String> getKeys() {
		// lazily load the lookup hashtable.

		Map<String, String> map = getMapByUserLocale();

		if (map == null) {
			loadMap();
		}

		ResourceBundle parent = this.parent;
		return new ResourceBundleEnumeration(map.keySet(), (parent != null) ? parent.getKeys() : null);
	}

	/*
	 * Get the translation map based on user language
	 */
	private Map<String, String> getMapByUserLocale() {
		String localeString = getUserLocaleString();
		Map<String, String> map = resources.get(localeString);
		return map;
	}

	/**
	 * Check if a code provided is present on resource bundle
	 * 
	 * @param key
	 *            A code to be tested
	 * @return true if it is present
	 */
	protected boolean hasKey(String key) {
		Map<String, String> map = getMapByUserLocale();
		return map != null && map.containsKey(key);
	}

	/*
	 * We lazily load the map hashtable. This function does the loading.
	 */
	private synchronized void loadMap() {

		Map<String, String> map = getMapByUserLocale();

		if (map != null) {
			return;
		}

		Object[][] contents = getContents();

		HashMap<String, String> temp = new HashMap<>(contents.length);
		for (int i = 0; i < contents.length; ++i) {
			// key must be non-null String, value must be non-null
			String key = (String) contents[i][0];
			String value = (String) contents[i][1];
			if (key == null || value == null) {
				throw new NullPointerException();
			}
			temp.put(key, value);
		}
		map = temp;

		resources.put(getUserLocaleString(), map);
	}

	// Implements java.util.ResourceBundle.handleGetObject; inherits javadoc specification.
	/** {@inheritDoc} */
	@Override
	public final Object handleGetObject(String key) {
		// lazily load the map hashtable for user locale. If the key is not found, do the insertion of it and get the
		// data from
		// database again.

		Map<String, String> map = getMapByUserLocale();

		if (map == null) {
			loadMap();
			map = getMapByUserLocale();
		}

		if (!this.hasKey(key)) {
			insertKey(key);
			updateBundle();
		}

		if (key == null) {
			throw new NullPointerException();
		}

		return map.get(key);

	}

	/**
	 * Update the resource bundle when a translation is provided for some code, for immediate use of the translation
	 */
	public void updateBundle() {
		resources.remove(getUserLocaleString());
		loadMap();
	}

	/*
	 * Insert a key in database with the code that wasn't found and the user language. After this, is possible to
	 * provide a translation for this code.
	 */
	private void insertKey(String key) {

		try {

			Traducao traducao = new Traducao(key, key, this.getUserLanguage());
			lookupTranslationDao();
			traducaoSession.update(traducao);

		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	public String getTranslation(String localeCode, String translationCode) {

		Map<String, String> translations = resources.get(localeCode);

		if (null == translations) {
			lookupTranslationDao();
			translations = traducaoSession.findMapaTraducoes(localeCode);
			resources.put(localeCode, translations);
			return null;
		}

		String translation = translations.get(translationCode);

		if (Strings.isNullOrEmpty(translation)) {

			lookupTranslationDao();
			insertKey(translationCode);

			resources.get(localeCode).put(translationCode, translationCode);

		}

		return translation;
	}

}
