package br.com.fiap.integracao.producer;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueRequestor;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsProdutor {
	private static final String NOME_FILA = "queue/FilaFIAP";

	Context jndiContext;
	QueueConnectionFactory queueConnectionFactory;
	QueueConnection queueConnection;
	QueueSession queueSession;
	Queue queue;
	QueueSender queueSender;
	TextMessage message;
	QueueRequestor queueRequestor;

	private static InitialContext getInitial() throws Exception {
		/*
		 * Instancia o contexto JNDI com as propriedades do JBOSS
		 */
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		InitialContext context = new InitialContext(props);
		return context;
	}


	public String enviaMensagemFila(String mensagem) {
		/*
		 * Criando o objeto jndi
		 */
		String retorno = null;
		
		try {
			System.out.println("**Produtor: Iniciando InitialContext...");
			jndiContext = getInitial();
		} catch (Exception e) {
			System.out.println("**Produtor: Não foi possível criar o JNDI API"
					+ e.toString());
			System.exit(1);
		}

		try {
			/*
			 * Efetua o lookup da Connection Factory.
			 */
			System.out.println("**Produtor: Iniciando Queue Connection Factory");
			queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
			/*
			 * Efetua lookup da fila
			 */
			System.out.println("**Produtor: Iniciando Fila");
			queue = (Queue) jndiContext.lookup(NOME_FILA);

		} catch (NamingException e) {
			System.out
					.println("**Produtor: problemas no JNDI API lookup : " + e.toString());
			System.exit(1);
		}

		try {
			System.out.println("**Produtor: Criando conexão com Queue Connection Factory...");
			queueConnection = queueConnectionFactory.createQueueConnection();

			System.out.println("**Produtor: Criando conexão com Queue...");
			queueSession = queueConnection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			QueueRequestor queueRequestor = new QueueRequestor(queueSession, queue);

			System.out.println("**Produtor: Iniciando a conexão...");
			queueConnection.start();

			message = queueSession.createTextMessage();
			message.setText(mensagem);
            System.out.println("**Produtor: Enviando a Proposta -> " + mensagem + " e aguardando resposta..." );

            TextMessage reply = (TextMessage) queueRequestor.request(message);     
            
            System.out.println("**Produtor: Resposta recebida ->" + reply.getText());  
            
            retorno = reply.getText();
            
		} catch (JMSException e) {
			e.printStackTrace();
			
		}finally{
			System.out.println("**Produtor: Fechando conexão com o Queue Connection...");
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                    
                } catch (JMSException e) {
                	System.out.println("Exception: " + e.toString());
                }
            }
		}
				
		return retorno;

	}

}
