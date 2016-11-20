package br.ucs.lasis.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.primefaces.model.UploadedFile;

public class FileUtils {

	public static String salvaArquivo(UploadedFile file, String directory)
			throws IOException {
		
		if (file == null) {
			throw new IOException("Arquivo nulo");
		}

		String internalName = getInternalName(file);

		InputStream inputStream = null;
		OutputStream outputStream = null;

		// read this file into InputStream
		inputStream = file.getInputstream();

		// write the inputStream to a FileOutputStream
		File f = new File(directory + File.separator + internalName);
		outputStream = new FileOutputStream(f);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		
		return internalName;
	}
	
	private static String getInternalName(UploadedFile uFile) {
		return new SimpleDateFormat("ddMMYYYYhhmmsss").format(new Date()) + "_" + uFile.getFileName();
	}

}
