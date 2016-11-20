package br.ucs.lasis.core.view.enums;

public enum PermissionTypeEnum {

	PAGINA("PAGINA", "Pagina"), MODULO("MODULO", "Modulo"), CAMPO("CAMPO", "Campo"), LINK("LINK", "Link"), BOTAO("BOTAO", "Botão"), ABA("ABA", "Aba"), ACESSO("ACESSO", "Acesso");

	private final String id;
	private final String nome;

	private PermissionTypeEnum(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

}
