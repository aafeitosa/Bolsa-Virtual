package br.ucs.lasis.core.util;

import java.util.Collection;
import java.util.List;

public final class CheckUtils {

	private CheckUtils() {
	}

	public static boolean collectionIsNullOrEmpty(Collection<?> c) {
		return null == c || c.isEmpty();
	}

	public static void assertType(Class<?> clazzType, Class<?> clazzCheck) {
		if (!clazzType.isAssignableFrom(clazzCheck)) {
			throw new ClassCastException(clazzType + " is not assignable from " + clazzCheck);
		}
	}

	public static boolean isList(Object object) {
		return null == object ? false : List.class.isAssignableFrom(object.getClass());
	}

	public static boolean isLong(Object object) {
		return null == object ? false : Long.class.isAssignableFrom(object.getClass());
	}

	public static boolean checkBoolean(Boolean bool) {
		return null == bool ? false : bool.booleanValue();
	}
	
	public static boolean checkNotNull(Boolean bool) {
		return bool != null;
	}
}
