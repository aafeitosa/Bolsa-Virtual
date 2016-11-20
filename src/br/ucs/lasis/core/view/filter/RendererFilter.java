package br.ucs.lasis.core.view.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

public class RendererFilter implements Filter {

	FilterConfig config;
	private DocumentBuilder documentBuilder;
	
	public static final String PDF_PARAMETER_NAME = "output";
	public static final String PDF_PARAMETER_VALUE = "pdf";

	public void init(FilterConfig config) throws ServletException {
		try {
			this.config = config;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ServletException(e);
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Check to see if this filter should apply.
		String renderType = request.getParameter(PDF_PARAMETER_NAME);
		if (renderType != null) {

			// Capture the content for this request
			ContentCaptureServletResponse capContent = new ContentCaptureServletResponse(response);
			filterChain.doFilter(request, capContent);
			String content = null;
	
			try {
				// Parse the XHTML content to a document that is readable by the XHTML renderer.
				content = capContent.getContent();
				
				// Eliminar se desnecessário

//				// Substitui as quebras de linha por parágrafos para diminuir sua altura
//				content = content.replaceAll("<br />", "<p/>");
				
//				// Elimina headers de tabelas
//				content = content.replaceAll("<thead>|</thead>|<tbody>|</tbody>","");

				// Elimina os brancos
				content = content.replaceAll("&nbsp;","");

				content = removeAllFacesViewState(content);
				
				// Fim eliminar se desnecessário
				
				// Substitui os caracters especiais do HTML por caracteres normais para renderizar o PDF com acentuação correta.
				//content = StringEscapeUtils.unescapeHtml4(content);
				
				StringReader contentReader = new StringReader(content);
				InputSource source = new InputSource(contentReader);
				Document xhtmlContent = documentBuilder.parse(source);
				
				
//				NodeList nodeList = xhtmlContent.getElementsByTagName("javax.faces.ViewState");
//				for(int i = 0; i< nodeList.getLength(); i++) {
//					Node node = nodeList.item(i);
//					System.out.println(node.getNodeName());
//					System.out.println(node.getNodeValue());
//				}

				if (renderType.equals(PDF_PARAMETER_VALUE)) {
					ITextRenderer renderer = new ITextRenderer();
				
					renderer.setDocument(xhtmlContent, "");
					renderer.getSharedContext().getTextRenderer().setSmoothingThreshold(-1f);
					renderer.layout();

					response.setContentType("application/pdf");
					OutputStream browserStream = response.getOutputStream();
					renderer.createPDF(browserStream);
					return;
				}

			} catch (SAXException e) {
//				System.out.println("-------------------- SAXException --------------------------------");
//				System.out.println(content);
				throw new ServletException(e);
			} catch (DocumentException e) {
//				System.out.println("-------------------- DocumentException ---------------------------");
//				System.out.println(content);
				throw new ServletException(e);
			}

		} else {
			// Normal processing
			filterChain.doFilter(request, response);
		}
	}

	public void destroy() {
	}
	
	
	private String removeAllFacesViewState(String content) {
		while(content.indexOf("javax.faces.ViewState")>0) {
			content = removeFacesViewState(content);
		}
		return content;
	}
		
	
	private String removeFacesViewState(String content) {
		
		
		
		int indiceInicial = content.indexOf("javax.faces.ViewState");
//		System.out.println("indiceInicial " + indiceInicial);
		int i = indiceInicial;
		int indiceFinal = indiceInicial;
		while(i>0) {
			if(content.charAt(i)!='<') {
				i--;
			} else {
				break;
			}
		}
		indiceInicial = i;
//		System.out.println("indiceInicial " + indiceInicial);
//		System.out.println("indiceFinal " + indiceFinal);
		while(indiceFinal<content.length()) {
			if(content.charAt(indiceFinal)!='>') {
				indiceFinal++;
			} else {
				break;
			}
		}
//		System.out.println("indiceFinal " + indiceFinal);
		
//		System.out.println("---------------------------------------------------");
//		System.out.println(content.substring(0,indiceInicial));
//		System.out.println("---------------------------------------------------");
//		System.out.println(content.substring(indiceFinal+1));
		
		return content.substring(0,indiceInicial) + content.substring(indiceFinal+1);
	}
}

class ContentCaptureServletResponse extends HttpServletResponseWrapper {
	
	private ByteArrayOutputStream contentBuffer;
	private PrintWriter writer;
	
	public ContentCaptureServletResponse(HttpServletResponse originalResponse) {
		super(originalResponse); 
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if(writer == null){
			contentBuffer = new ByteArrayOutputStream();
			writer = new PrintWriter(contentBuffer);
		}
		return writer;
	}
	
	public String getContent(){
		writer.flush();
		String xhtmlContent = new String(contentBuffer.toByteArray());
		return xhtmlContent; 
	}
}

