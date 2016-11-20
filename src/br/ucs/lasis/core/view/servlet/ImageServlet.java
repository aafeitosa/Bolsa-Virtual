package br.ucs.lasis.core.view.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ucs.lasis.core.ejb.ParametrosSingletonBean;
import br.ucs.lasis.core.enums.ParametrosEnum;

import com.google.common.io.ByteStreams;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private ParametrosSingletonBean parametros;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getPathInfo().substring(1);

		if (filename == null || filename.isEmpty()) {
			imageNotFound(response);
		}

		String directoryUpload = parametros.buscarValorParametroAsString(ParametrosEnum.DIRETORIO_UPLOAD);

		File file = new File(directoryUpload, filename);

		if (file.exists()) {

			String type;
			try {
				type = Files.probeContentType(file.toPath());
				response.setHeader("Content-Type", type);
				response.setHeader("Content-Length", String.valueOf(file.length()));
				response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
				Files.copy(file.toPath(), response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			imageNotFound(response);
		}
	}

	private void imageNotFound(HttpServletResponse response) throws IOException {
		InputStream input = getServletContext().getResourceAsStream("/resources/imagens/image_not_found.jpg");
		int length = input.available();
		response.setHeader("Content-Type", "image/jpeg");
		response.setHeader("Content-Length", String.valueOf(length));
		response.setHeader("Content-Disposition", "inline; filename=\"image_not_found.jpg\"");
		ByteStreams.copy(input, response.getOutputStream());
	}

}