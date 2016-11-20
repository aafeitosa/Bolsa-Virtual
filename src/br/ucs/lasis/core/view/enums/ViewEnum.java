package br.ucs.lasis.core.view.enums;

import br.ucs.lasis.core.enums.BaseEnum;

public enum ViewEnum implements BaseEnum {

	HOME("HOME","home.xhtml",PermissionEnum.HOME),
	
	USUARIO_LISTAR("USUARIO_LISTAR", "core/listaUsuarios.xhtml", PermissionEnum.USUARIO_LISTAR_ACESSAR),
	USUARIO_EDITAR("USUARIO_EDITAR", "core/formUsuarios.xhtml", PermissionEnum.USUARIO_EDITAR_ACESSAR),

	BOOTSFACES("BOOTSFACES", "responsive.xhtml", PermissionEnum.USUARIO_LISTAR_ACESSAR),
	
	
	PERFIL_LISTAR("PERFIL_LISTAR", "core/listaPerfis.xhtml", PermissionEnum.PERFIL_LISTAR_ACESSAR),
	PERFIL_EDITAR("PERFIL_EDITAR", "core/formPerfis.xhtml", PermissionEnum.PERFIL_EDITAR_ACESSAR),

	IDIOMA_LISTAR("IDIOMA_LISTAR", "core/listaIdiomas.xhtml", PermissionEnum.IDIOMA_LISTAR_ACESSAR),
	IDIOMA_EDITAR("IDIOMA_EDITAR", "core/formIdiomas.xhtml", PermissionEnum.IDIOMA_EDITAR_ACESSAR),
	
	TRADUCAO_LISTAR("TRADUCAO_LISTAR", "core/listaTraducoes.xhtml", PermissionEnum.TRADUCAO_LISTAR_ACESSAR),
	TRADUCAO_IMPRIMIR("TRADUCAO_LISTAR", "core/printTraducoes.xhtml", PermissionEnum.TRADUCAO_IMPRIMIR_ACESSAR),
	
	EMPRESA_LISTAR("EMPRESA_LISTAR", "core/listaEmpresas.xhtml", PermissionEnum.EMPRESA_LISTAR_ACESSAR),
	EMPRESA_EDITAR("EMPRESA_EDITAR", "core/formEmpresas.xhtml", PermissionEnum.EMPRESA_EDITAR_ACESSAR),

	RAMO_ATIVIDADE_LISTAR("RAMO_ATIVIDADE_LISTAR", "core/listaRamosAtividades.xhtml", PermissionEnum.RAMO_ATIVIDADE_ACESSAR),
	RAMO_ATIVIDADE_EDITAR("RAMO_ATIVIDADE_EDITAR", "core/formRamosAtividades.xhtml", PermissionEnum.RAMO_ATIVIDADE_EDITAR_ACESSAR),
	
	PARAMETRO_LISTAR("PARAMETRO_LISTAR", "core/listaParametros.xhtml", PermissionEnum.PARAMETRO_LISTAR_ACESSAR),
	
	GRUPO_LISTAR("GRUPO_LISTAR", "listaGrupos.xhtml", PermissionEnum.GRUPO_LISTAR_ACESSAR),
	GRUPO_EDITAR("GRUPO_EDITAR", "formGrupos.xhtml", PermissionEnum.GRUPO_EDITAR_ACESSAR),

	PERIODO_LISTAR("GRUPO_LISTAR", "listaPeriodos.xhtml", PermissionEnum.PERIODO_LISTAR_ACESSAR),
	PERIODO_EDITAR("GRUPO_EDITAR", "formPeriodos.xhtml", PermissionEnum.PERIODO_EDITAR_ACESSAR),

	RODADA_LISTAR("RODADA_LISTAR", "listaRodadas.xhtml", PermissionEnum.RODADA_LISTAR_ACESSAR),
	RODADA_EDITAR("RODADA_EDITAR", "formRodadas.xhtml", PermissionEnum.RODADA_EDITAR_ACESSAR),

	PROFESSOR_LISTAR("PROFESSOR_LISTAR", "listaProfessores.xhtml", PermissionEnum.PROFESSOR_LISTAR_ACESSAR),
	PROFESSOR_EDITAR("PROFESSOR_EDITAR", "formProfessores.xhtml", PermissionEnum.PROFESSOR_EDITAR_ACESSAR),
	
	ALUNO_LISTAR("ALUNO_LISTAR", "listaAlunos.xhtml", PermissionEnum.ALUNO_LISTAR_ACESSAR),
	ALUNO_EDITAR("ALUNO_EDITAR", "formAlunos.xhtml", PermissionEnum.ALUNO_EDITAR_ACESSAR),
	
	INVESTIMENTO_LISTAR("INVESTIMENTO_LISTAR", "listaInvestimentos.xhtml", PermissionEnum.INVESTIMENTO_LISTAR_ACESSAR),
	INVESTIMENTO_EDITAR("INVESTIMENTO_EDITAR", "formInvestimentos.xhtml", PermissionEnum.INVESTIMENTO_EDITAR_ACESSAR),

	RESULTADO_LISTAR("RESULTADO_VISUALIZAR", "listaResultados.xhtml", PermissionEnum.RESULTADO_LISTAR_ACESSAR),
	RESULTADO_VISUALIZAR("RESULTADO_VISUALIZAR", "formResultados.xhtml", PermissionEnum.RESULTADO_VISUALIZAR_ACESSAR),
	
	DASHBOARD_LISTAR("DASHBOARD_LISTAR", "dashboard.xhtml", PermissionEnum.DASHBOARD_LISTAR_ACESSAR),
	

	;
	
	private static final String ROOT_DIR = "/secured/";

	private final String id;
	private final String url;
	private final PermissionEnum permission;

	private ViewEnum(String id, String url, PermissionEnum permission) {
		this.id = id;
		this.permission = permission;
		this.url = ROOT_DIR + url;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public PermissionEnum getPermission() {
		return permission;
	}

	public String getPagina() {
		return url.substring(url.lastIndexOf("/") + 1).replace(".xhtml", "");
	}

	public String getPaginaRedirect() {
		return this.getPaginaRedirect(false);
	}

	public String getPaginaRedirect(boolean includeViewParams) {
		if (includeViewParams) {
			return getPagina() + "?faces-redirect=true" + "&includeViewParams=true";
		}
		return getPagina() + "?faces-redirect=true";
	}

	public static ViewEnum getById(String id) {
		for (ViewEnum item : ViewEnum.values()) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	public static ViewEnum getByURL(String url) {
		
//		System.out.println(">>> " + url);
		
		if (url.contains("?")) {
			String[] split = url.split("\\?");
			url = split[0];
		}

		for (ViewEnum item : ViewEnum.values()) {
			
			if (item.getUrl().equals(url)) {
				return item;
			}
		}
		return null;
	}

	public String getUrlWithoutType() {
		return url.replace(".xhtml", "");
	}

	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public String getTranslationKey() {
		return this.id;
	}

}
