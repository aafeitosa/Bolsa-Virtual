package br.ucs.lasis.core.enums;

public enum ParametrosEnum {
	
	ID_EMPRESA_PRINCIPAL("Sistema", "ID_EMPRESA_PRINCIPAL", "Identificador da empresa principal", DataTypeEnum.ID),
	ID_IDIOMA_PADRAO("Sistema", "ID_IDIOMA_PADRAO", "Identificador do idioma padrão", DataTypeEnum.ID),
	NRO_REGISTROS_POR_PAGINA("Sistema", "NRO_REGISTROS_POR_PAGINA", "Número de registros por página", DataTypeEnum.INTEGER),
	OPCOES_REGISTROS_POR_PAGINA("Sistema", "OPCOES_REGISTROS_POR_PAGINA", "Opções de números de registros por página", DataTypeEnum.STRING),
	DIRETORIO_UPLOAD("Sistema", "DIRETORIO_UPLOAD", "Diretório de gravação de arquivos enviados por upload", DataTypeEnum.STRING),
	ENDERECO_SERVIDOR("Sistema", "ENDERECO_SERVIDOR", "Endereço do Servidor SMTP", DataTypeEnum.STRING),
	PORTA_SERVIDOR("Sistema", "PORTA_SERVIDOR", "Porta do Servidor SMTP", DataTypeEnum.STRING),
	NOME_USUARIO("Sistema", "NOME_USUARIO", "Nome do Usuário SMTP", DataTypeEnum.STRING),
	SENHA_USUARIO("Sistema", "SENHA_USUARIO", "Senha do Usuário SMTP", DataTypeEnum.STRING),
	REMETENTE_EMAIL("Sistema", "REMETENTE_EMAIL", "Remetente para envio de email", DataTypeEnum.STRING),
	TITULO_EMAIL("Sistema", "TITULO_EMAIL", "Título para email de recuperação de senha", DataTypeEnum.STRING),
	MENSAGEM_EMAIL("Sistema", "MENSAGEM_EMAIL", "Mensagem para email de recuperação de senha", DataTypeEnum.STRING),
	ID_PERFIL_ADMINISTRADOR("Sistema", "ID_PERFIL_ADMINISTRADOR", "ID do perfil do Administrador no cadastro de perfis", DataTypeEnum.INTEGER),
	ID_PERFIL_COORDENADOR("Empreendedorismo", "ID_PERFIL_COORDENADOR", "ID do perfil do Coordenador no cadastro de perfis", DataTypeEnum.INTEGER),
	ID_PERFIL_PROFESSOR("Empreendedorismo", "ID_PERFIL_ALUNO", "ID do perfil do professor no cadastro de perfis", DataTypeEnum.INTEGER),
	ID_PERFIL_ALUNO("Empreendedorismo", "ID_PERFIL_ALUNO", "ID do perfil do aluno no cadastro de perfis", DataTypeEnum.INTEGER),
	BONUS_PERCENTUAL_PARTICIPACAO("Empreendedorismo", "BONUS_PERCENTUAL_PARTICIPACAO", "Bônus do percentual de participação (0-100%)", DataTypeEnum.INTEGER)
	;
	
	private String modulo;
	private String nome;
	private String descricao;
	private DataTypeEnum tipo;

	private ParametrosEnum(String modulo, String nome, String descricao, DataTypeEnum tipo) {
		this.modulo = modulo;
		this.nome = nome;
		this.descricao = descricao;
		this.tipo = tipo;
	}

	public String getModulo() {
		return this.modulo;
	}
	
	public String getNome() {
		return this.nome;
	}

	public DataTypeEnum getTipo() {
		return tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return nome + " " + descricao;
	}

	public static ParametrosEnum getById(String id) {
		for (ParametrosEnum item : ParametrosEnum.values()) {
			if (item.getNome().equals(id)) {
				return item;
			}
		}
		return null;
	}

}
