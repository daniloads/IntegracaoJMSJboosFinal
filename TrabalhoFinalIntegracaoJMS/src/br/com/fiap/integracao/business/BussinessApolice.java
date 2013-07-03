package br.com.fiap.integracao.business;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.fiap.integracao.bean.ApoliceSeguro;
import br.com.fiap.integracao.bean.Proposta;


//Regra de Negócio para criação da apólice de seguro
public class BussinessApolice {

	//Método interno para calculo de Idade
	private int calculaIdade(Calendar dataNascto){
		 
		int anoNascto =  dataNascto.get(Calendar.YEAR);
		dataNascto = Calendar.getInstance();
		int anoAtual = dataNascto.get(Calendar.YEAR);
		return anoAtual - anoNascto;
	
	}
	
	
	//*** Algoritimo para Criação da Apólice de Seguro  **************************
	public ApoliceSeguro validaSeguro(Proposta seguro){
		
		ApoliceSeguro apolice = new ApoliceSeguro();
		
		int risco = 0;
		double desconto = 0;
		double vlorSeguro = 0;
		double porcentagem = 0;
		
		//** Analise de Risco CPF  **************************
		apolice.setNumeroProposta(seguro.getNumProposta());
		
		//** Analise de Risco CPF  **************************
		apolice.setCpf(seguro.getCpf());
		
		
		//*** Analise de Risco Sexo  **************************
		if(seguro.getSexo().equals("M")){
			risco = 10;
		}else if(seguro.getSexo().equals("F")){
			risco = 0;
		}
		
		
		
		//*** Analise de Risco Idade  **************************
		int idade = calculaIdade(seguro.getDataNascto());
		
		if(idade < 18 ){
			risco += 50;
		}else if(idade < 21){
			risco += 45;
		}else if(idade < 24){
			risco += 40;
		}else if(idade < 27){
			risco += 35;
		}else if(idade < 30){
			risco += 30;
		}else if(idade < 35){
			risco += 25;
		}else if(idade < 40){
			risco += 20;
		}else if(idade < 50){
			risco += 15;
		}else if(idade < 60){
			risco += 10;
		}else{
			risco += 5;
		}
		
				
		//*** Analise de Risco ano Veiculo  **************************
		if(seguro.getAno() < 1990){
			risco += 40;
		}else if(seguro.getAno() <= 1995){
			risco += 35;
		}else if(seguro.getAno()<= 2000){
			risco += 30;
		}else if(seguro.getAno()<= 2005){
			risco += 20;
		}else if(seguro.getAno()<= 2010){
			risco += 15;
		}else{
			risco += 10;
		}
		
		//*** Desconto pela forma de pagamento **************************
		if(seguro.getQtdParcelas()== 1){
			desconto = 0.15;
		}else if(seguro.getQtdParcelas()<= 3){
			desconto = 0.10 ;
		}else if(seguro.getQtdParcelas()<= 6){
			desconto = 0.05 ;
		}else if(seguro.getQtdParcelas()<= 9){
			desconto = 0 ;			
		}
		
		//*** Analise de Risco para Valor da apólice de Seguro************	
		if(risco <= 10){
			porcentagem = 0.20;
		}else if(risco <= 30){
			porcentagem = 0.25;
		}else if(risco <= 50){
			porcentagem = 0.30;
		}else if(risco <= 75){
			porcentagem = 0.35;
		}else if(risco <= 100){
			porcentagem = 0.40;
		}else{
			risco = 100;
		}
		
		if(risco!=100){
			vlorSeguro = seguro.getValorVeiculo() * porcentagem;
			if(desconto > 0 ){
				vlorSeguro = vlorSeguro * desconto;
			}
			
			if(seguro.getQtdParcelas()> 1){
				vlorSeguro = vlorSeguro / seguro.getQtdParcelas();
			}
			
			// Cria numero da apolice
			apolice.setNumApolice((int) (Math.random() * 9999999));
			
			// Seta data de Vigencia  
			Date data = new Date();  
			Calendar dtVigenciaIni = Calendar.getInstance();
			dtVigenciaIni.setTime(data);  
			apolice.setDataVigenciaIni(dtVigenciaIni);
			  
			// Incrementa data para 1 ano   
			Calendar dtVigenciaFim = Calendar.getInstance();
			dtVigenciaFim.setTime(data);
			dtVigenciaFim.add(Calendar.YEAR, 1);
			apolice.setDataVigenciaFim(dtVigenciaFim);
			
			// Seta Qtde Parcelas
			apolice.setQtdParcelas(seguro.getQtdParcelas());			
			
			// Seta Valor da parcela do Seguro
			apolice.setVlrParcelas(vlorSeguro);
			
			// Seta Observação
			apolice.setDescricaoApolice(" A proposta foi aprovada. Apolice de No.: " + apolice.getNumApolice() + 
										" foi criado com sucesso valor é de : " + apolice.getVlrParcelas() 
										+ " parcelado em  " + apolice.getQtdParcelas() + " parcelas"); 			
			data = null;
			dtVigenciaIni = null;
			dtVigenciaFim = null;

		}else {
			
			
			// Cria numero da apolice
			apolice.setNumApolice(0);
			
			// Seta data de Vigencia  
			Date data = new Date();  
			Calendar dtVigencia = Calendar.getInstance();
			dtVigencia.setTime(data);  
			apolice.setDataVigenciaIni(dtVigencia);
			apolice.setDataVigenciaFim(dtVigencia);
			
			// Seta Qtde Parcelas
			apolice.setQtdParcelas(0);			
			
			// Seta Valor da parcela do Seguro
			apolice.setVlrParcelas(0);
			
			// Seta Observação
			apolice.setDescricaoApolice("**BussinessApolice: A Proposta não foi aprovado devido aos critérios de riscos do segurado"); 
			
			data = null;
			dtVigencia = null;
		}
 					
		return apolice;
	}
	
	
	//*** Método para gerar o XML da Proposta  **************************
	public String geraXmlProposta(Proposta proposta){
		String xml = null;
		
		try{
			StringWriter stw = new StringWriter();
			JAXBContext ctx = JAXBContext.newInstance(Proposta.class);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.marshal(proposta, stw);
			xml = stw.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xml;
	}
	
	//*** Método para gerar o XML da Apolice  **************************
	public String geraXmlApolice(ApoliceSeguro apolice){
		String xml = null;
		
		try{
			StringWriter stw = new StringWriter();
			JAXBContext ctx = JAXBContext.newInstance(ApoliceSeguro.class);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.marshal(apolice, stw);
			xml = stw.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xml;
	}
	
	//*** Método para leitura o XML da Proposta  **************************
	public Proposta parseXmlProposta(String xmlProposta){
		
		Proposta proposta = new Proposta();
		try {
			StringReader str = new StringReader(xmlProposta);
			JAXBContext ctx = JAXBContext.newInstance(Proposta.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			proposta = (Proposta)unmarshaller.unmarshal(str);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return proposta;
		
	}
	
	//*** Método para leitura o XML da Apolice  **************************
	public ApoliceSeguro parseXmlApoliceSeguro(String xmlApolice){
		
		ApoliceSeguro apolice = new ApoliceSeguro();
		try {
			StringReader str = new StringReader(xmlApolice);
			JAXBContext ctx = JAXBContext.newInstance(ApoliceSeguro.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			apolice = (ApoliceSeguro)unmarshaller.unmarshal(str);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return apolice;
		
	}
	
}
