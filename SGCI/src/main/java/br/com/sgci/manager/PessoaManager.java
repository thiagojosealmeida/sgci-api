package br.com.sgci.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.sgci.controller.schema.EnderecoMapper;
import br.com.sgci.controller.schema.EnderecoResponse;
import br.com.sgci.controller.schema.PessoaMapper;
import br.com.sgci.controller.schema.PessoaReq;
import br.com.sgci.controller.schema.PessoaResponse;
import br.com.sgci.controller.schema.PessoaUpd;
import br.com.sgci.model.Endereco;
import br.com.sgci.model.Pessoa;
import br.com.sgci.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Validated
public class PessoaManager {

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Transactional
	public Pessoa createPessoa(PessoaReq req) {
	
		// validações
		// devem ser inseridas aqui 
		
		Endereco endereco = new Endereco(
				req.endereco().cep(),
				req.endereco().estado(),
				req.endereco().cidade(),
				req.endereco().rua(),
				req.endereco().bairro(),
				req.endereco().numero());
		
		Pessoa pessoa = new Pessoa(
				endereco,
				req.nome(), 
				req.tipo(), 
				req.documento(), 
				req.profissao(), 
				req.estadoCivil());
		
		return pessoaRepository.save(pessoa);
	}
	
	@Transactional
	public Pessoa updatePessoa(@Valid Long idPessoa, PessoaUpd upd)
	{
		Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow();
		
		//Atualização de dados de pessoa
		pessoa.setNome(upd.nome());
		pessoa.setTipo(upd.tipo());
		pessoa.setDocumento(upd.documento());
		pessoa.setTx_profissao(upd.profissao());
		pessoa.setEstadoCivil(upd.estadoCivil());
		
		//Atualização de dados de endereco
		pessoa.getEndereco().setCep(upd.endereco().cep());
		pessoa.getEndereco().setRua(upd.endereco().rua());
		pessoa.getEndereco().setNumero(upd.endereco().numero());
		pessoa.getEndereco().setBairro(upd.endereco().bairro());
		pessoa.getEndereco().setCidade(upd.endereco().cidade());
		pessoa.getEndereco().setEstado(upd.endereco().estado());
		
		pessoaRepository.save(pessoa);
		
		return pessoa;
	}

	@Transactional
	public void deletePessoa(Long idPessoa)
	{
		Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow();
		pessoaRepository.delete(pessoa);
	}

	public List<PessoaResponse> findAll()
	{
		List<PessoaResponse> listResponse = new ArrayList<PessoaResponse>();
		List<Pessoa> listPessoaBD = pessoaRepository.findAll();
		listPessoaBD.forEach(item ->
		{
			EnderecoResponse enderecoResponse = EnderecoMapper.INSTANCE.toEnderecoResponse(item.getEndereco()); 
			PessoaResponse pessoaResponse = PessoaMapper.INSTANCE.toPessoaResponse(item, enderecoResponse);
			listResponse.add(pessoaResponse);
		});
		return listResponse;
	}

	public PessoaResponse findById(Long idPessoa)
	{
		Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow();
		EnderecoResponse enderecoResponse = EnderecoMapper.INSTANCE.toEnderecoResponse(pessoa.getEndereco()); 
		return PessoaMapper.INSTANCE.toPessoaResponse(pessoa, enderecoResponse);
	}
}
