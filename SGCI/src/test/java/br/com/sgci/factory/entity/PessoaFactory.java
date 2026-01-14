package br.com.sgci.factory.entity;

import br.com.sgci.controller.schema.EnderecoReq;
import br.com.sgci.controller.schema.PessoaReq;
import br.com.sgci.model.Endereco;
import br.com.sgci.model.EstadoCivilEnum;
import br.com.sgci.model.Pessoa;
import br.com.sgci.model.TipoPessoaEnum;

public class PessoaFactory {
	
	public static Pessoa getPessoa(Endereco endereco)
	{
		return new Pessoa(endereco, "Gabriel Renteiro Almeida",TipoPessoaEnum.PESSOA_FISICA, "12345678920", "Estudante", EstadoCivilEnum.SOLTEIRO);
	}

	public static PessoaReq getPessoaReq(EnderecoReq enderecoReq)
	{
		return new PessoaReq("Gabriel Renteiro Almeida",enderecoReq, TipoPessoaEnum.PESSOA_FISICA, "12345678920", "Estudante", EstadoCivilEnum.SOLTEIRO);
	}
}
