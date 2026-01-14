package br.com.sgci.factory.entity;

import br.com.sgci.controller.schema.EnderecoReq;
import br.com.sgci.model.Endereco;

public class EnderecoFactory {
	
	public static Endereco getEndereco()
	{
		return new Endereco("66080460","PA","Belém","Vila Santa Terezinha","Pedreira",5000);
	}

	public static EnderecoReq getEnderecoReq() 
	{
		return new EnderecoReq("66080460","PA","Belém","Vila Santa Terezinha","Pedreira",5000);
	}
}
