package br.com.fiap.integracao.business;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


	//clase que retorna uma autenticacao para ser enviada e verificada pelo servidor smtp  
	public	class EmailAutenticacao extends Authenticator {  
	    public String username = null;  
	    public String password = null;  
	  
	  
	    public EmailAutenticacao(String user, String pwd) {  
	        username = user;  
	        password = pwd;  
	    }  
	  
	    protected PasswordAuthentication getPasswordAuthentication() {  
	        return new PasswordAuthentication (username,password);  
	    }  
	}


