package br.ucs.lasis.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Strings;

public class LocaleUtils {

	private static List<Locale> availableLocales;
	private static List<String> availableLocalesString;

	static {
		availableLocales = getOrderedAvailableLocales();
		availableLocalesString = getOrderedAvailableLocalesAsString();
	}

	private static List<String> getOrderedAvailableLocalesAsString() {
		List<String> localesStringList = new ArrayList<>();
		for (Locale locale : getOrderedAvailableLocales()) {
			localesStringList.add(locale.toString());
		}
		return localesStringList;
	}

	@SuppressWarnings("unchecked")
	private static List<Locale> getOrderedAvailableLocales() {
		Locale[] locales = Locale.getAvailableLocales();
		List<Locale> localesList = new ArrayList<>();
		for (Locale locale : locales) {
			localesList.add(locale);
		}
		Collections.sort(localesList, new LocaleComparator());
		return localesList;
	}

	@SuppressWarnings("rawtypes")
	static class LocaleComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			Locale lhs = (Locale) o1;
			Locale rhs = (Locale) o2;
			return lhs.toString().compareTo(rhs.toString());
		}
	}

	public static List<Locale> getAvailableLocales() {
		return availableLocales;
	}

	public static List<String> getAvailableLocalesAsString() {
		return availableLocalesString;
	}

	public static Locale getLocale(String localeString) {
		if (localeString == null) {
			return null;
		}
		String[] parts = localeString.split("\\_");
		switch (parts.length) {
		case 0:
			return null;
		case 1:
			return new Locale(parts[0]);
		case 2:
			return new Locale(parts[0], parts[1]);
		case 3:
			return new Locale(parts[0], parts[1], parts[2]);
		}
		return null;
	}

	public static String getFormattedLocaleDate(Date date) {
		return getFormattedLocaleDate(date, null);
	}

	public static String getFormattedLocaleDate(Date date, String dateFormat) {

		if (null == date) {
			return "";
		}

		if (Strings.isNullOrEmpty(dateFormat)) {
			dateFormat = "dd/MM/yyyy";
		}

//		SessionUserBacking sessionUser = (SessionUserBacking) BackingUtils
//				.getBackingBean(FrameworkConstants.SESSION_USER_BACKING_NAME);
//
//		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
//				getLocale(sessionUser.getLocale()));

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		
		return sdf.format(date);

	}

}
