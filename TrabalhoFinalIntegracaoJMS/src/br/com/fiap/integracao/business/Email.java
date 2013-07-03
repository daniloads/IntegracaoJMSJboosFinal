package br.com.fiap.integracao.business;

import java.util.Properties;  
import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;   

public class Email {  
      
    private String mailSMTPServer;  
    private String mailSMTPServerPort;  
      
    /* 
     * quando instanciar um Objeto ja sera atribuido o servidor SMTP do GMAIL  
     * e a porta usada por ele 
     */  
    public Email() { //Para o GMAIL   
        mailSMTPServer = "smtp.gmail.com";  
        mailSMTPServerPort = "465";  
    }  

      
      
    public void enviaEmail(String from, String to, String subject, String message) {  
   
    	
    	System.out.println("**Email: Configurando Properties");
        
    	Properties props = new Properties();  
        props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP  
        props.put("mail.smtp.starttls.enable","true");   
        props.put("mail.smtp.host", mailSMTPServer); //server SMTP do GMAIL  
        props.put("mail.smtp.auth", "true"); //ativa autenticacao  
        props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)  
        props.put("mail.debug", "true");  
        props.put("mail.smtp.port", mailSMTPServerPort); //porta  
        props.put("mail.smtp.socketFactory.port", mailSMTPServerPort); //mesma porta para o socket  
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        props.put("mail.smtp.socketFactory.fallback", "false");  
          
        //Cria um autenticador que sera usado a seguir  
        EmailAutenticacao auth = new EmailAutenticacao ("fiapintegracao@gmail.com","javajms2013");  
                  
        //Session - objeto que ira realizar a conexão com o servidor  
        /*Como há necessidade de autenticação é criada uma autenticacao que 
         * é responsavel por solicitar e retornar o usuário e senha para  
         * autenticação */  
        Session session = Session.getDefaultInstance(props, auth);  
 //       session.setDebug(true); //Habilita o LOG das ações executadas durante o envio do email  
  
        //Objeto que contém a mensagem  
        Message msg = new MimeMessage(session); 
        
        System.out.println("**Email: Configurando Autenticacao Gmail");
  
        try {  
            //Setando o destinatário  
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));  
            //Setando a origem do email  
            msg.setFrom(new InternetAddress(from));  
            //Setando o assunto  
            msg.setSubject(subject);  
            //Setando o conteúdo/corpo do email  
            msg.setContent(message,"text/plain");  
  
        } catch (Exception e) {  
            System.out.println(">> Erro: Completar Mensagem");  
            e.printStackTrace();  
        }  

        //Objeto encarregado de enviar os dados para o email  
        Transport tr;  
        try {  
            tr = session.getTransport("smtp"); //define smtp para transporte  
            tr.connect(mailSMTPServer, "seuusuarioparalogin", "suasenhaparalogin");  
            msg.saveChanges(); 
            System.out.println("**Email: Envia o email para o destinatário: " + to);
            tr.sendMessage(msg, msg.getAllRecipients());  
            tr.close();  
        } catch (Exception e) {   
            System.out.println(">> Erro: Envio Mensagem");  
            e.printStackTrace();  
        }  

    }  
}  
  
 
