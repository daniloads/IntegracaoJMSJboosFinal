package br.com.fiap.integracao.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlRootElement;

//Pojo Proposta de Seguro

/**
 * @author DANILO
 *
 */
@XmlRootElement
public class Proposta {
	
	private int numProposta;
	private String cpf;
	private Calendar dataNascto;
	private String nomeBeneficiario;
	private String sexo;
	private String veiculo;
	private String marca;
	private int ano;
	private double valorVeiculo;
	private int qtdParcelas;
	private String email;
	
	

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getNumProposta() {
		return numProposta;
	}
	public void setNumProposta(int noProposta) {
		this.numProposta = noProposta;
	}
	public Calendar getDataNascto() {
		return dataNascto;
	}
	public void setDataNascto(Calendar dataNascto) {
		this.dataNascto = dataNascto;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}
	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}
	public String getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public double getValorVeiculo() {
		return valorVeiculo;
	}
	public void setValorVeiculo(double valorVeiculo) {
		this.valorVeiculo = valorVeiculo;
	}
	public int getQtdParcelas() {
		return qtdParcelas;
	}
	public void setQtdParcelas(int qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	@Override
	public String toString() {
		
		String dataNascFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataNascto.getTime());
		System.out.println("********************Proposta***************************");
		return  " Numero Proposta: " + numProposta  
				+ "\n Cpf: " + cpf 
				+ "\n Data Nascimeto: " + dataNascFormatada
				+ "\n Beneficiário: " + nomeBeneficiario 
				+ "\n email: " + email 
				+ "\n Sexo: " + sexo
				+ "\n Vaículo: " + veiculo 
				+ "\n Marca: " + marca 
				+ "\n Ano: " + ano
				+ "\n Valor: " + valorVeiculo 
				+ "\n Qtde Parcelas: " + qtdParcelas
				+ "\n";
		
	}

	

}
