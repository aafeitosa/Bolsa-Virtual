package br.ucs.lasis.core.view.jsf.backing;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.model.Arquivo;
import br.ucs.lasis.core.model.Idioma;
import br.ucs.lasis.core.session.IdiomaSession;
import br.ucs.lasis.core.util.FileUtils;
import br.ucs.lasis.core.util.LocaleUtils;

@Named("idiomaFormBack")
@ViewScoped
public class IdiomaFormBacking extends AbstractBacking {

	private static final long serialVersionUID = 1L;

	private Idioma idioma;

	@Inject
	private IdiomaSession idiomaSession;

	@PostConstruct
	public void init() {

		Long id = this.getParametroId();

		if (id != null) {
			this.idioma = idiomaSession.buscarPorId(id);
		} else {
			this.idioma = new Idioma();
		}
		this.definePrimeiraPagina();
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	public String listagem() {
//		FacesContext.getCurrentInstance().renderResponse();
		return "listaIdiomas";
	}

	public String salva() {
		idiomaSession.salvar(this.idioma);
		if (isEmpresaPrincipal()) {
			return listagem();
		}
		return null;
	}

	public boolean isInsercao() {
		return null == this.idioma.getId();
	}

	public List<String> getLocales() {
		return LocaleUtils.getAvailableLocalesAsString();
	}

	public void handleFileUpload(FileUploadEvent event) {

//		System.out.println("UPLOAD!!");

//		System.out.println(event.getFile().getFileName());

		UploadedFile uFile = event.getFile();

		String nomeInterno = null;
		
		try {
			nomeInterno = FileUtils.salvaArquivo(uFile,
					this.getUploadDirectory());
		} catch (IOException e) {
			showErrorMessage("Falha ao gravar arquivo", e.getLocalizedMessage());
			return;
		}
		
		Arquivo arquivo = new Arquivo();
		arquivo.setNomeOriginal(uFile.getFileName());
		arquivo.setNomeInterno(nomeInterno);
		arquivo.setTamanho(uFile.getSize());
		this.idioma.setArquivo(arquivo);
	}

	private String getUploadDirectory() {
		return this.getParametrosSingletonBean().buscarValorParametroAsString(
				ParametrosEnum.DIRETORIO_UPLOAD);
	}
}