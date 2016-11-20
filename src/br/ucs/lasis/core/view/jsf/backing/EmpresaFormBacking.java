package br.ucs.lasis.core.view.jsf.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.model.Perfil;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.qualifier.PerfilCoordenador;
import br.ucs.lasis.core.qualifier.PerfilProfessor;
import br.ucs.lasis.core.qualifier.UsuarioLogado;
import br.ucs.lasis.core.session.EmpresaSession;
import br.ucs.lasis.core.session.IdiomaSession;

@Named("empresaFormBack")
@ViewScoped
public class EmpresaFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;

	@Inject
	private EmpresaSession empresaSession;
	
	@Inject
	private IdiomaSession idiomaSession;
	
	private List<Idioma> idiomas;
	
	private boolean criarUsuario;
	
	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;
	
	@Inject
	@PerfilCoordenador
	private Perfil perfilCoordenador;
	
	@Inject
	@PerfilProfessor
	private Perfil perfilProfessor;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.empresa = empresaSession.buscarPorId(id);
		} else {

			if (!isEmpresaPrincipal()) {
				this.empresa = getEmpresaLogada();
			} else {
				this.empresa = new Empresa();
			}
		}
		this.definePrimeiraPagina();
		
		this.idiomas = idiomaSession.buscaTodos();
			
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public boolean isCriarUsuario() {
		return criarUsuario;
	}

	public void setCriarUsuario(boolean criarUsuario) {
		this.criarUsuario = criarUsuario;
	}

	public List<Idioma> getIdiomas() {
		return idiomas;
	}

	public String listagem() {
		return "listaEmpresas";
	}

	public String salva() {
		
		try {
		
		empresaSession.salvar(this.empresa, this.criarUsuario);
		
		if(isEmpresaPrincipal()) {
			return listagem();
		} 
		
		} catch(BusinessException be) {
			this.showErrorMessage(getTranslation("acronimo_duplicado"), getTranslation("acronimo") + " " + this.empresa.getAcronimo() + " " + getTranslation("ja_utilizado") + " " + getTranslation("operacao_cancelada"));
		}
		
		return null;
	}

	public boolean isInsercao() {
		return null == this.empresa.getId();
	}
	
	public boolean isMostrarIndice() {
		return (usuarioLogado.isAdministrador() || usuarioLogado.getPerfis().contains(perfilCoordenador) || usuarioLogado.getPerfis().contains(perfilProfessor));
	}
}