package br.ucs.lasis.core.view.enums;

import br.ucs.lasis.core.enums.BaseEnum;
import br.ucs.lasis.core.util.FacesMessageUtils;

public class EnumTranslation<E extends BaseEnum> {

	private E enumValue;
	private String translation;

	public EnumTranslation() {
	}

	public EnumTranslation(E enumValue) {
		this(enumValue, FacesMessageUtils.getTranslation(enumValue.getTranslationKey()));
	}

	public EnumTranslation(E enumValue, String translation) {
		this.enumValue = enumValue;
		this.translation = translation;
	}

	public E getEnumValue() {
		return enumValue;
	} 

	public void setEnumValue(E enumValue) {
		this.enumValue = enumValue;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((enumValue == null) ? 0 : enumValue.hashCode());
		result = (prime * result) + ((translation == null) ? 0 : translation.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EnumTranslation<E> other = (EnumTranslation<E>) obj;
		if (enumValue == null) {
			if (other.enumValue != null) {
				return false;
			}
		} else
			if (!enumValue.equals(other.enumValue)) {
				return false;
			}
		if (translation == null) {
			if (other.translation != null) {
				return false;
			}
		} else
			if (!translation.equals(other.translation)) {
				return false;
			}
		return true;
	}

	@Override
	public String toString() {
		return "EnumTranslation [enumValue=" + enumValue + ", translation=" + translation + "]";
	}

	public String getId() {
		return enumValue.getId();
	}

	@SuppressWarnings("unchecked")
	public static <E extends BaseEnum> EnumTranslation<E> translate(BaseEnum baseEnum) {
		if (null == baseEnum) {
			return null;
		}
		return new EnumTranslation<E>((E) baseEnum, FacesMessageUtils.getTranslation(baseEnum.getTranslationKey()));
	}

}