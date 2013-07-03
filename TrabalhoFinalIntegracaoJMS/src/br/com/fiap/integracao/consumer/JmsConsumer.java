package br.com.fiap.integracao.consumer;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.fiap.integracao.bean.ApoliceSeguro;
import br.com.fiap.integracao.bean.Proposta;
import br.com.fiap.integracao.business.BussinessApolice;
import br.com.fiap.integracao.business.Email;

public class JmsConsumer extends Thread {

	private static final String NOME_FILA = "queue/FilaFIAP";
	Context jndiContext;
	QueueConnectionFactory queueConnectionFactory;
	QueueConnection queueConnection;
	QueueSession queueSession;
	Queue queue;
	QueueReceiver queueReceiver;
	TextMessage message;
	QueueSender replySender;
	
	
	public void run(){
		
		try{
			System.out.println("-------------Inicia a Thread Consumer-------------------");
			this.processaMensagem();		
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	

	private static InitialContext getInitial() throws Exception {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		InitialContext context = new InitialContext(props);
		return context;
	}
	
	
	public void processaMensagem(){
		
		try {
			// Obtem o InitialContext
			System.out.println("**Consumidor: Obtendo InitialContext...");
			jndiContext = getInitial();
		} catch (Exception e) {
			System.out.println("**Consumidor: Não foi possível criar o JNDI API"
					+ e.toString());
			System.exit(1);
		}
		
		try {
			System.out.println("**Consumidor: Obtendo Queue Connection Factory");
			queueConnectionFactory = (QueueConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			System.out.println("**Consumidor: Obtendo Queue");
			queue = (Queue) jndiContext.lookup(NOME_FILA);
		} catch (NamingException e) {
			System.out
					.println("**Consumidor: problemas no JNDI API lookup : " + e.toString());
			System.exit(1);
		}
		
		try {
			System.out.println("**Consumidor: Criando conexão com Queue Connection Factory...");
			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("**Consumidor: Criando conexão com Queue...");
			queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
			System.out.println("**Consumidor: Criando o Receiver");
			queueReceiver = queueSession.createReceiver(queue);
			System.out.println("**Consumidor: Iniciando a conexão...");
			queueConnection.start();
			System.out.println("**Consumidor: Aguarda a mensagem por tempo indeterminado ...");
			
			BussinessApolice bsApolice = new BussinessApolice();
			Email email = new Email();
			
			
			while (true) { //aqui ele fica aguardando entrar a mensagem na fila
				// Aguarda a mensagem por tempo indeterminado
				Message m = queueReceiver.receive(); /// recupera a mensagem
				if (m != null) {
					if (m instanceof TextMessage) {
						message = (TextMessage) m; //
						System.out.println("**Consumidor: XML da Proposta recebida: " +  message.getText());

						//Efetua o parse do XML da Proposta e Imprimindo os valores enviado pelo XML
						System.out.println("**Consumidor: Efetua o parse do XML Recebido");
						Proposta proposta = bsApolice.parseXmlProposta(message.getText()); //Transforma em Proposta
						
						System.out.println("**Consumidor: Proposta Recebida para aprovação");
						System.out.println(proposta.toString());
						
						System.out.println("**Consumidor: Valida a Proposta Recebida para aprovação");
						ApoliceSeguro apolice = bsApolice.validaSeguro(proposta); // Envia para aprovação
						
						email.enviaEmail("FiapSeguro@JMSSeguros.com.br",proposta.getEmail(), 
										"Aprovação de Proposta de Seguro no: " + proposta.getNumProposta(), 
										apolice.getDescricaoApolice()); // Envia o email
						
						System.out.println("**Consumidor: Envia o email para o Segurado");
						
						
						System.out.println("**Consumidor: Cria o XML da Apolice para o retorno");
						String xmlApolice = bsApolice.geraXmlApolice(apolice);				
						
						/*
						 * Enviando a resposta
						 */
						// Obtem a fila de resposta embutida na mensagem
						// recebida
						Queue tempQueue = (Queue) message.getJMSReplyTo();
						
						// Cria um objeto sender para a fila de resposta
						replySender = queueSession.createSender(tempQueue);

						// Cria a mensagem de resposta
						TextMessage resposta = queueSession.createTextMessage();
						
						//Envia o XML da Apolice para o Produtor
						System.out.println("**Consumidor: Retorna o XML da Apolice para o produtor");
						resposta.setText(xmlApolice);

						resposta.setJMSCorrelationID(message.getJMSMessageID());
						replySender.send(resposta);
						System.out
								.println("**Consumidor: Resposta enviada à fila de resposta.");
						if ("fim".equalsIgnoreCase(message.getText())) {
							break;
						}
					} else {
						break;
					}
				}
			}
		} catch (JMSException e) {
			System.out.println("Exception : " + e.toString());
		} finally {
			System.out.println("**Consumidor: Fechando conexão " + "com a Queue...");
			if (queueConnection != null) {
				try {
					System.out.println("**Consumidor: Fechando conexão com o Queue Connection.");
					queueConnection.close();
				} catch (JMSException e) {
					System.out.println("Exception : " + e.toString());
				}
			}
		}

	}
		

}
