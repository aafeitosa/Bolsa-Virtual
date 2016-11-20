package br.ucs.lasis.core.session;

import java.util.List;

import javax.ejb.Local;

import br.ucs.lasis.core.exceptions.BusinessException;

@Local
public interface MailSession {

	void sendTextMessage(String to, String subject, String textMessage) throws BusinessException;

	void sendTextMessage(String to, String subject, String textMessage, List<String> attachments) throws BusinessException;

	void sendHTMLMessage(String to, String subject, String htmlMessage) throws BusinessException;

	void sendHTMLMessage(String to, String subject, String htmlMessage, List<String> attachments) throws BusinessException;
	
	void sendTextMessage(String from, String to, String subject, String textMessage) throws BusinessException;

	void sendTextMessage(String from, String to, String subject, String textMessage, List<String> attachments) throws BusinessException;

	void sendHTMLMessage(String from, String to, String subject, String htmlMessage) throws BusinessException;

	void sendHTMLMessage(String from, String to, String subject, String htmlMessage, List<String> attachments) throws BusinessException;
}

