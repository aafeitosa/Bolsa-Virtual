package br.ucs.lasis.core.view.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.ucs.lasis.core.enums.BaseEnum;
import br.ucs.lasis.core.util.CheckUtils;
import br.ucs.lasis.core.util.DateTimeUtils;
import br.ucs.lasis.core.view.enums.EnumTranslation;

public class GridLazyLoaderFilter extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public GridLazyLoaderFilter() {
		super();
	}

	public GridLazyLoaderFilter(int initialCapacity) {
		super(initialCapacity);
	}

	public GridLazyLoaderFilter(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public void toEndOfDay(String key) {

		Date date = getDate(key);

		if (null == date) {
			return;
		}

		put(key, DateTimeUtils.getEndOfDay(date));

	}

	public void toBeginingOfDay(String key) {

		Date date = getDate(key);

		if (null == date) {
			return;
		}

		put(key, DateTimeUtils.getBeginingOfDay(date));

	}

	public Date getDate(String key) {

		Object object = get(key);

		if (null == object) {
			return null;
		}

		if (!Date.class.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException("Is not a java.util.Date!");
		}

		return (Date) object;

	}

	public Long getLong(String key) {

		Object object = get(key);

		if (null == object) {
			return null;
		}

		if (!CheckUtils.isLong(object)) {
			throw new IllegalArgumentException("Is not a java.lang.Long!");
		}

		return (Long) object;

	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEnum> List<EnumTranslation<T>> getList(String key) {

		Object object = get(key);

		if (null == object) {
			return null;
		}

		if (!List.class.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException("Is not a java.util.List!");
		}

		return (List<EnumTranslation<T>>) object;

	}

	public <T> T getEnumId(String key) {

		Object object = get(key);

		if (null == object) {
			return null;
		}

		if (!BaseEnum.class.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException("Not implements BaseEnum!");
		}

		BaseEnum baseEnum = (BaseEnum) object;

		return baseEnum.getId();

	}

	public void removerKeysWithNullorEmptyValues() {

		Iterator<Entry<String, Object>> iterator = entrySet().iterator();

		Entry<String, Object> actual = null;

		while (iterator.hasNext()) {

			actual = iterator.next();

			if (null == actual.getValue()) {
				iterator.remove();
			} else {

				if (String.class.isAssignableFrom(actual.getValue().getClass()) && actual.getValue().toString().trim().isEmpty()) {
					iterator.remove();
				}

				if (List.class.isAssignableFrom(actual.getValue().getClass())) {

					List<?> lista = (List<?>) actual.getValue();

					if (lista.isEmpty()) {
						iterator.remove();
					}
				}
			}
		}
	}

	public <T extends Enum<T>> boolean hasEnumInClause(Class<T> enumClass, String key) {

		if (!containsKey(key)) {
			return false;
		}

		List<EnumTranslation<BaseEnum>> list = getList(key);

		if (null == list || list.isEmpty()) {
			return false;
		}

		return enumClass.getFields().length != list.size();

	}

	public GridLazyLoaderFilter cloneWithoutNullOrEmptyValues() {

		GridLazyLoaderFilter filter = new GridLazyLoaderFilter(this);
		filter.removerKeysWithNullorEmptyValues();
		return filter;

	}


	public boolean isList(String key) {

		Object object = get(key);

		if (null == object) {
			return false;
		}

		return List.class.isAssignableFrom(object.getClass());

	}

	public List<?> getAsList(String key) {

		if (null == get(key)) {
			return new ArrayList<>(0);
		}

		if (isList(key)) {
			return (List<?>) get(key);
		}

		List<Object> retorno = new ArrayList<>(1);
		retorno.add(get(key));

		return retorno;

	}

	// TODO testar
	public void toList(String key) {

		put(key, getAsList(key));

	}

//	// TODO testar
//	public void toEnumId(String key) {
//
//		Object object = get(key);
//
//		if (null == object) {
//			return;
//		}
//
//		if (EnumTranslation.class.isAssignableFrom(object.getClass())) {
//			object = ((EnumTranslation<?>) object).getEnumValue();
//		}
//
//		if (!Enum.class.isAssignableFrom(object.getClass())) {
//			throw new IllegalArgumentException("Is not a java.lang.Enum!");
//		}
//
//		put(key, EJBUtil.getEnumId((Enum<?>) object));
//
//	}
}
