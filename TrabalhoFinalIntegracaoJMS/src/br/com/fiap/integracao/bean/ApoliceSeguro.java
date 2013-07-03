package br.com.fiap.integracao.bean;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlRootElement;

//Pojo Apolice de Seguro

@XmlRootElement
public class ApoliceSeguro {
	
	private int numeroProposta;
	private String cpf;
	private int numApolice;
	private Calendar dataVigenciaIni;
	private Calendar dataVigenciaFim;
	private int qtdParcelas;
	private double vlrParcelas;
	private String descricaoApolice;
	
	
	public int getNumApolice() {
		return numApolice;
	}
	public void setNumApolice(int numApolice) {
		this.numApolice = numApolice;
	}
	public Calendar getDataVigenciaIni() {
		return dataVigenciaIni;
	}
	public void setDataVigenciaIni(Calendar dataVigenciaIni) {
		this.dataVigenciaIni = dataVigenciaIni;
	}
	public Calendar getDataVigenciaFim() {
		return dataVigenciaFim;
	}
	public void setDataVigenciaFim(Calendar dataVigenciaFim) {
		this.dataVigenciaFim = dataVigenciaFim;
	}
	public int getQtdParcelas() {
		return qtdParcelas;
	}
	public void setQtdParcelas(int qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}
	public double getVlrParcelas() {
		return vlrParcelas;
	}
	public void setVlrParcelas(double vlrParcelas) {
		this.vlrParcelas = vlrParcelas;
	}
	public String getDescricaoApolice() {
		return descricaoApolice;
	}
	public void setDescricaoApolice(String descricaoApolice) {
		this.descricaoApolice = descricaoApolice;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public int getNumeroProposta() {
		return numeroProposta;
	}
	public void setNumeroProposta(int numeroProposta) {
		this.numeroProposta = numeroProposta;
	}

	public String toString() {

		String dataVigenciaIniStr = new SimpleDateFormat("dd/MM/yyyy").format(this.dataVigenciaIni.getTime());
		String dataVigenciaFimStr = new SimpleDateFormat("dd/MM/yyyy").format(this.dataVigenciaFim.getTime());
		System.out.println("********************Aprovação de Seguro ***************************");
		
		return  " Numero da Proposta: " + numeroProposta 
				+ " \n Cpf: " + cpf 
				+ "\n Numero da Apolice: " + numApolice
				+ "\n Periodo de Vigência de: " + dataVigenciaIniStr 
				+ "\n Periodo de Vigência até: " + dataVigenciaFimStr 
				+ "\n Qte Parcelas: " + qtdParcelas
				+ "\n Valor Parcelas: " + vlrParcelas 
				+ "\n Descrição da Apolice: " + descricaoApolice;
	}

	

	
	
}
