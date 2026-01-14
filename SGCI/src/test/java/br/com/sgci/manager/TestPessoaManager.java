package br.com.sgci.manager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.sgci.controller.schema.EnderecoReq;
import br.com.sgci.controller.schema.PessoaReq;
import br.com.sgci.factory.entity.EnderecoFactory;
import br.com.sgci.factory.entity.PessoaFactory;
import br.com.sgci.model.Pessoa;

@ActiveProfiles("test")
@SpringBootTest
public class TestPessoaManager {

	@Autowired
	private PessoaManager pessoaManager;
	
	@Test
	void Create_Pessoa()
	{
		EnderecoReq enderecoReq = EnderecoFactory.getEnderecoReq();
		PessoaReq req = PessoaFactory.getPessoaReq(enderecoReq);
		
		Pessoa pessoa = pessoaManager.createPessoa(req);
		
		assertTrue(pessoa != null && pessoa.getId() > 0);
	}
	
	
	//createPessoa
	
	//updatePessoa
	
	//deletePessoa
	
	//finById
	
	//findAllPaged
	
}
