package br.ucs.lasis.core.enums;

import java.sql.Date;

public enum DataTypeEnum {

	INTEGER("INTEGER", "Número Inteiro", Integer.class),
	ID("ID", "Identificador (Número Inteiro)",Long.class),
	STRING("STRING", "Texto", String.class),
	DOUBLE("DOUBLE", "Número Decimal", Double.class),
	DATE("DATE", "Data", Date.class),
	BOOLEAN("BOOLEAN", "Lógico (true ou false)", Boolean.class);

	private String name;
	private String description;
	private Class<?> type;

	private DataTypeEnum(String name, String description, Class<?> type) {
		this.name = name;
		this.description = description;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public Class<?> getType() {
		return this.type;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return name + " " + type;
	}

	public static DataTypeEnum getById(String id) {
		for (DataTypeEnum item : DataTypeEnum.values()) {
			if (item.getName().equals(id)) {
				return item;
			}
		}
		return null;
	}

}

