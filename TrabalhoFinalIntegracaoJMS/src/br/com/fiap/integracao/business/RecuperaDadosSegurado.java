package br.com.fiap.integracao.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import br.com.fiap.integracao.bean.Proposta;

//Classe de Interface de dados do usuário com o terminal
public class RecuperaDadosSegurado {

	private Scanner entrada;

	//recupera dados tipo Double
	public double getDadosSeguroDoub(String mensagem) {

		double valor = 0;
		boolean ok;

		do {
			try {
				if (!mensagem.equals("")) {
					System.out.println(mensagem);
				}
				entrada = new Scanner(System.in);
				ok = true;
				valor = entrada.nextDouble();
			} catch (java.util.InputMismatchException e) {
				ok = false;
			}

		} while (ok == false);

		return valor;

	}

	//recupera dados tipo int
	public int getDadosSeguroInt(String mensagem) {

		int valor = 0;

		if (!mensagem.equals("")) {
			System.out.println(mensagem);
		}
		entrada = new Scanner(System.in);
		try {
			valor = entrada.nextInt();
		} catch (java.util.InputMismatchException e) {
			valor = 0;
		}
		return valor;

	}

	//recupera dados tipo Float
	public float getDadosSeguroFloat(String mensagem) {

		float valor = 0;

		if (!mensagem.equals("")) {
			System.out.println(mensagem);
		}
		entrada = new Scanner(System.in);
		try {
			valor = entrada.nextInt();
		} catch (java.util.InputMismatchException e) {
			valor = 0;
		}
		return valor;

	}

	//recupera dados tipo String
	public String getDadosSeguroStr(String mensagem) {

		if (!mensagem.equals(null)) {
			System.out.println(mensagem);
		}
		entrada = new Scanner(System.in);
		return entrada.next();

	}

	//recupera dados tipo Calendar
	public Calendar getDadosSeguroCalendar(String mensagem) {

		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		String data = null;
		boolean erro = false;

		do {
			if (!mensagem.equals(null)) {
				System.out.println(mensagem);
			}
			entrada = new Scanner(System.in);
			data = entrada.next();

			try {
				calendar.setTime(formatoData.parse(data));
				erro = false;

			} catch (Exception e) {
				erro = true;
			}

		} while (erro);

		formatoData = null;
		data = null;
		return calendar;

	}

	//recupera cria a proposta conforme os dados interfaciado pelo usuário
	public Proposta populaDadosSeguro() {


		int inteiro = 0;
		String str = null;
		boolean erro = false;

		Proposta seguro = new Proposta();
		
		// =================No Proposta ===================================
		seguro.setNumProposta((int) (Math.random() * 9999999));

		// =================Recupera CPF ===================================
		do {
			str = this.getDadosSeguroStr("Digite o CPF (Apenas Número 11 digitos): ");


			if (str.length() == 11) {
				erro = false;
			} else {
				erro = true;
			}

		} while (erro);
		seguro.setCpf(str);

		// =================Recupera Data Nascto ============================
		seguro.setDataNascto(this
				.getDadosSeguroCalendar("Digite a data de Nascimento (dd/mm/yyyy):"));

		// =================Recupera Nome Beneficiário ============================
		seguro.setNomeBeneficiario(this.getDadosSeguroStr("Digite o Nome do Beneficiário: "));
		
		// =================Recupera o Email ===========================
		seguro.setEmail(this.getDadosSeguroStr("Digite o email: "));

		// =================Recupera o Sexo ============================
		erro = false;
		do {
			str = this.getDadosSeguroStr("Digite o Sexo (F) ou (M): ");
			if (!str.equals("M") && !str.equals("F")) {
				erro = true;
			} else {
				erro = false;
			}

		} while (erro);

		seguro.setSexo(str);

		// =================Recupera o Veiculo ============================ ;
		seguro.setVeiculo(this.getDadosSeguroStr("Digite o nome Veículo: "));
		

		// =================Recupera a Marca ============================ ;
		seguro.setMarca(this.getDadosSeguroStr("Digite a Marca do Veículo: "));
		
		
		// =================Recupera o Ano =============================== ;
		erro = false;
		do {
			inteiro = this.getDadosSeguroInt("Digite o ano do veículo > 1980");
			if (inteiro < 1980) {
				erro = true;
			} else {
				erro = false;
			}

		} while (erro);

		seguro.setAno(inteiro);

		// =================Recupera o Valor =============================== ;
		seguro.setValorVeiculo(this.getDadosSeguroDoub("Digite o valor do veículo: "));

		// =================Recupera a qtde de Parcelas =============================== ;
		erro = false;
		do {
			inteiro = this
					.getDadosSeguroInt("Digite a quantidade de parcelas de 0 à 12: ");
			if (inteiro < 0 || inteiro > 12) {
				erro = true;
			} else {
				erro = false;
			}

		} while (erro);
		seguro.setQtdParcelas(inteiro);

		str = null;

		
		
		return seguro;

	}

}
