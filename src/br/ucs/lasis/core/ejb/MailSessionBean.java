package br.ucs.lasis.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import br.ucs.lasis.core.enums.ParametrosEnum;
import br.ucs.lasis.core.exceptions.BusinessException;
import br.ucs.lasis.core.mail.SendMail;
import br.ucs.lasis.core.session.MailSession;


@Stateless
public class MailSessionBean implements MailSession {

	private SendMail mail;
	
	@Inject
	private ParametrosSingletonBean parametroSingletonBean;
	
	@Override
	public void sendTextMessage(String to, String subject, String textMessage) throws BusinessException {
		this.sendTextMessage(getRemetente(), to, subject, textMessage);
	}

	@Override
	public void sendTextMessage(String to, String subject, String textMessage, List<String> attachments) throws BusinessException {
		this.sendTextMessage(getRemetente(), to, subject, textMessage, attachments);

	}

	@Override
	public void sendHTMLMessage(String to, String subject, String htmlMessage) throws BusinessException {
		this.sendHTMLMessage(getRemetente(), to, subject, htmlMessage);
	}

	@Override
	public void sendHTMLMessage(String to, String subject, String htmlMessage, List<String> attachments) throws BusinessException {
		this.sendHTMLMessage(getRemetente(), to, subject, htmlMessage, attachments);
	}

	@Override
	public void sendTextMessage(String from, String to, String subject, String textMessage) throws BusinessException {
		mail = new SendMail(getEnderecoSMTP(), getPortaSMTP());
		try {
			mail.sendTextMessage(from, to, subject, textMessage);
		} catch (AddressException e) {
			throw new BusinessException("erro_enviando_email", e);
		} catch (MessagingException e) {
			throw new BusinessException("erro_enviando_email", e);
		}
	}

	@Override
	public void sendTextMessage(String from, String to, String subject, String textMessage, List<String> attachments) throws BusinessException {
		mail = new SendMail(getEnderecoSMTP(), getPortaSMTP());
		try {
			mail.sendTextMessage(from, to, subject, textMessage, attachments);
		} catch (AddressException e) {
			throw new BusinessException("erro_enviando_email", e);
		} catch (MessagingException e) {
			throw new BusinessException("erro_enviando_email", e);
		}

	}

	@Override
	public void sendHTMLMessage(String from, String to, String subject, String htmlMessage) throws BusinessException {
		mail = new SendMail(getEnderecoSMTP(), getPortaSMTP());
		try {
			mail.sendHTMLMessage(from, to, subject, htmlMessage);
		} catch (AddressException e) {
			e.printStackTrace();
			throw new BusinessException("erro_enviando_email", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new BusinessException("erro_enviando_email", e);
		}
	}

	@Override
	public void sendHTMLMessage(String from, String to, String subject, String htmlMessage, List<String> attachments) throws BusinessException {
		mail = new SendMail(getEnderecoSMTP(), getPortaSMTP());
		try {
			mail.sendHTMLMessage(from, to, subject, htmlMessage, attachments);
		} catch (AddressException e) {
			throw new BusinessException("erro_enviando_email", e);
		} catch (MessagingException e) {
			throw new BusinessException("erro_enviando_email", e);
		}
	}
	
	private String getEnderecoSMTP() {
		return parametroSingletonBean.buscarValorParametroAsString(ParametrosEnum.ENDERECO_SERVIDOR); 
	}
	
	private String getPortaSMTP() {
		return parametroSingletonBean.buscarValorParametroAsString(ParametrosEnum.PORTA_SERVIDOR); 
	}
	
	private String getRemetente() {
		return parametroSingletonBean.buscarValorParametroAsString(ParametrosEnum.REMETENTE_EMAIL); 

	}
}
