package br.ucs.lasis.lasis1.view.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.ucs.lasis.core.model.Empresa;
import br.ucs.lasis.core.model.Usuario;
import br.ucs.lasis.core.view.jsf.backing.AbstractBacking;
import br.ucs.lasis.lasis1.model.entity.Grupo;
import br.ucs.lasis.lasis1.session.GrupoSession;

@Named("grupoFormBack")
@ViewScoped
public class GrupoFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Grupo grupo;
	
	private DualListModel<Empresa> empresaDualList;
	private List<Empresa> empresaSource;
	private List<Empresa> empresaTarget;

	private DualListModel<Usuario> professorDualList;
	private List<Usuario> professorSource;
	private List<Usuario> professorTarget;
	
	@Inject
	private GrupoSession grupoSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.grupo = grupoSession.buscaPorIdComEmpresas(id);
		} else {
			this.grupo = new Grupo();
		}
		
		this.editar();
		this.definePrimeiraPagina();
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String listagem() {
		return "listaGrupos";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String salva() {
		
		this.grupo.setEmpresas(new HashSet(empresaDualList.getTarget()));
		this.grupo.setProfessores(new HashSet(professorDualList.getTarget()));
		
		grupoSession.salva(this.grupo);
			return listagem();
	}

	public boolean isInsercao() {
		return null == this.grupo.getId();
	}
	
	public DualListModel<Empresa> getEmpresaDualList() {
		return empresaDualList;
	}

	public void setEmpresaDualList(DualListModel<Empresa> empresaDualList) {
		this.empresaDualList = empresaDualList;
	}
	
	public DualListModel<Usuario> getProfessorDualList() {
		return professorDualList;
	}

	public void setProfessorDualList(DualListModel<Usuario> professorDualList) {
		this.professorDualList = professorDualList;
	}

	public void configDualList() {
		empresaDualList = new DualListModel<Empresa>(empresaSource, empresaTarget);
		professorDualList = new DualListModel<Usuario>(professorSource, professorTarget);
	}
	
	public void editar() {

		empresaSource = new ArrayList<Empresa>();
		empresaSource = buscarEmpresas();
		empresaTarget = new ArrayList<Empresa>();

		List<Empresa> empresasGrupo = new ArrayList<Empresa>();
		empresasGrupo = new ArrayList<Empresa>(grupo.getEmpresas());
    	empresaSource.removeAll(empresasGrupo);
		empresaTarget.addAll(empresasGrupo);
		
		professorSource = new ArrayList<Usuario>();
		professorSource = buscarProfessores();
		professorTarget = new ArrayList<Usuario>();
		List<Usuario> professoresGrupo = new ArrayList<Usuario>();
		if(grupo.getProfessores()!=null) {
			professoresGrupo = new ArrayList<Usuario>(grupo.getProfessores());
		} else {
			professoresGrupo = new ArrayList<Usuario>();
		}
    	professorSource.removeAll(professoresGrupo);
		professorTarget.addAll(professoresGrupo);

		configDualList();
	}
	
	private List<Empresa> buscarEmpresas() {
//		return empresaSession.buscaTodasAtivas();
		return grupoSession.buscaEmpresasSemGrupo();
	}

	private List<Usuario> buscarProfessores() {
//		return empresaSession.buscaTodasAtivas();
		return grupoSession.buscaProfessores();
	}
	
	private void carregaEmpresas() {
//		this.grupo 
	}
}