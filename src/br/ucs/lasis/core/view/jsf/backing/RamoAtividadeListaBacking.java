package br.ucs.lasis.core.view.jsf.backing;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.jpa.PagedResult;
import br.ucs.lasis.core.model.RamoAtividade;
import br.ucs.lasis.core.session.RamoAtividadeSession;
import br.ucs.lasis.core.view.components.GridLazyLoader;
import br.ucs.lasis.core.view.components.GridLazyLoaderDTO;
import br.ucs.lasis.core.view.components.IGridLazyLoader;
import br.ucs.lasis.lasis1.model.entity.Grupo;

@Named("ramoAtividadeListaBack")
@ViewScoped
public class RamoAtividadeListaBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private String nome;

	private GridLazyLoader<RamoAtividade> gridLazyLoader;

	@Inject
	private RamoAtividadeSession ramoAtividadeSession;
	
	@PostConstruct
	public void init() {
		gridLazyLoader = atualizaGrid();
		this.definePrimeiraPagina();
	}

	private GridLazyLoader<RamoAtividade> atualizaGrid() {

		return new GridLazyLoader<RamoAtividade>(new IGridLazyLoader<RamoAtividade>() {

			@Override
			public PagedResult<RamoAtividade> load(GridLazyLoaderDTO gridLazyLoaderDTO) {

				Map<String, Object> filters = gridLazyLoaderDTO.getFilters();

				filters.put("nome", getNome());

				return ramoAtividadeSession.buscaTodosCom(gridLazyLoaderDTO);

			}
		});
	}

	// @ExceptionInterceptor
	public GridLazyLoader<RamoAtividade> search() {
		return gridLazyLoader;
	}

	public GridLazyLoader<RamoAtividade> getDataModel() {
		return this.gridLazyLoader;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String novo() {
		return "formRamosAtividades";
	}

	public String listagem() {
		return "listaRamosAtividades";
	}

	public void exclui(ActionEvent event) {
		try {
			ramoAtividadeSession.exclui(this.getParametroId());
		} catch (Exception e) {
			this.showConstraintError();
		}
	}

	public void pesquisar(ActionEvent event) {
		atualizaGrid();
	}

}
