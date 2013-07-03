package br.com.fiap.integracao.business;

import java.util.ArrayList;

import br.com.fiap.integracao.bean.ApoliceSeguro;
import br.com.fiap.integracao.bean.Proposta;
import br.com.fiap.integracao.consumer.JmsConsumer;
import br.com.fiap.integracao.producer.JmsProdutor;

public class TerminalSeguros {

	private ArrayList<Proposta> lstSeguro = new ArrayList<Proposta>();
	private ArrayList<ApoliceSeguro> lstApolice = new ArrayList<ApoliceSeguro>();
	private static RecuperaDadosSegurado recDadosSegurado = new RecuperaDadosSegurado();
	//private String xml;

	// Terminal de Seguros Imprimi Terminal
	private void imprimiTerminal() {

		System.out
				.println("**************TERMINAL SEGUROS FIAP*********************");
		System.out
				.println("* 1 - Preencher Propostas de Seguro                    *");
		System.out
				.println("* 2 - Lista Propostas de Seguro                        *");
		System.out
				.println("* 3 - Enviar Propostas para aprovação                  *");
		System.out
				.println("* 4 - Listar Propostas já avaliadas                    *");
		System.out
				.println("* 0 - Finalizar                                        *");
		System.out
				.println("********************************************************");
	}

	// Controla Ações do Terminal
	public void controleTerminal() {
		this.imprimiTerminal();

		int opcao;
		BussinessApolice bsApolice = new BussinessApolice();
		String xml = null;
		String xmlRetorno = null;

		do {
			opcao = recDadosSegurado.getDadosSeguroInt("");
			switch (opcao) {

			// Finaliza Terminal
			case 0:
				System.out.println("Terminal Finalizado!");
				break;

			// Preenche a Proposta
			case 1:
				this.lstSeguro.add(recDadosSegurado.populaDadosSeguro());
				this.imprimiTerminal();
				break;

			// Lista as Propostas Preenchidas
			case 2:
				for (Proposta seguros : lstSeguro) {
					System.out.println(seguros);
				}
				this.imprimiTerminal();
				break;

			// Envia as todas as Propostas para Fila QMS
			case 3:

				// Inicia a Thread Consumer
				JmsConsumer msgConsumer = new JmsConsumer();
				JmsProdutor msgProdutor = new JmsProdutor();
				ApoliceSeguro apoliceSeguro = new ApoliceSeguro();
				msgConsumer.start();

				for (Proposta proposta : lstSeguro) {
					System.out.println("**Terminal: Eviando propostas numero: " + proposta.getNumProposta());
					System.out.println("**Terminal: Gera XML Proposta  e envia o XML!!!");
					xml = bsApolice.geraXmlProposta(proposta);
					xmlRetorno = msgProdutor.enviaMensagemFila(xml);
					System.out.println("**Terminal: Efetuando o parse do XML de Retorno");
					apoliceSeguro = bsApolice.parseXmlApoliceSeguro(xmlRetorno);
					System.out.println(apoliceSeguro);
					lstApolice.add(apoliceSeguro);
				}
				xml = null;
				this.imprimiTerminal();
				break;

			// Lista todas as propostas de retorno da Fila QMS
			case 4:
				for (ApoliceSeguro apolice : lstApolice) {
					System.out.println(apolice);
					xml = bsApolice.geraXmlApolice(apolice);
				}
				this.imprimiTerminal();
				break;

			default:
				System.out.println("Opção Inválida!");
			}

		} while (opcao != 0);

		bsApolice = null;

	}

	public static void main(String args[]) {

		// Inicia o processo chamando o Terminal
		TerminalSeguros terminal = new TerminalSeguros();
		terminal.controleTerminal();

	}

}
