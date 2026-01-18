package br.com.sgci.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.sgci.controller.schema.EnderecoMapper;
import br.com.sgci.controller.schema.EnderecoResponse;
import br.com.sgci.controller.schema.PessoaFilter;
import br.com.sgci.controller.schema.PessoaMapper;
import br.com.sgci.controller.schema.PessoaReq;
import br.com.sgci.controller.schema.PessoaResponse;
import br.com.sgci.controller.schema.PessoaUpd;
import br.com.sgci.controller.schema.ResponsePagedCommon;
import br.com.sgci.model.Endereco;
import br.com.sgci.model.Pessoa;
import br.com.sgci.repository.PessoaRepository;
import jakarta.persistence.criteria.Predicate;
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
		pessoa.setProfissao(upd.profissao());
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

	public ResponsePagedCommon<PessoaResponse> findAll(@Valid PessoaFilter filtros)
	{
		//Filtros Dinâmicos
		Specification<Pessoa> filtrosCustomizados = (root, query, cb) ->
		{
			List<Predicate> condicoes = new ArrayList<>();

			if (filtros.getId() != null)
			{
				condicoes.add(cb.equal(root.get("id"), filtros.getId()));
			}
			if (filtros.getNome() != null)
			{
				condicoes.add(cb.like(root.get("nome"), "%" + filtros.getNome() + "%"));
			}
			if (filtros.getCep() != null)
			{
				condicoes.add(cb.equal(root.get("endereco").get("cep"), filtros.getCep()));
			}
			if (filtros.getEstado() != null)
			{
				condicoes.add(cb.equal(root.get("endereco").get("estado"), filtros.getEstado()));
			}
			if (filtros.getCidade() != null)
			{
				condicoes.add(cb.equal(root.get("endereco").get("cidade"), filtros.getCidade()));
			}
			if (filtros.getTipo() != null)
			{
				condicoes.add(cb.equal(root.get("pessoa"), filtros.getTipo()));
			}
			if (filtros.getDocumento() != null)
			{
				condicoes.add(cb.equal(root.get("pessoa"), filtros.getDocumento()));
			}
			if (filtros.getProfissao() != null)
			{
				condicoes.add(cb.equal(root.get("profissao"), filtros.getProfissao()));
			}
			if (filtros.getEstadoCivil() != null)
			{
				condicoes.add(cb.equal(root.get("estadoCivil"), filtros.getEstadoCivil()));
			}

			return cb.and(condicoes.toArray(Predicate[]::new));
		};
		
		// -------- ORDENAÇÃO SEGURA --------
        String ordenarPor = filtros.getOrdenarPor();

        if (ordenarPor == null || ordenarPor.isBlank()) {
            ordenarPor = "id";
        }

        Direction direction = filtros.getDirection() != null
                ? filtros.getDirection()
                : Direction.ASC;

        PageRequest pageRequest = PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                Sort.by(direction, ordenarPor)
        );
		
		Page<Pessoa> listPessoaBD = pessoaRepository.findAll(filtrosCustomizados, pageRequest);

		List<PessoaResponse> listResponse = new ArrayList<PessoaResponse>();
		
		listPessoaBD.forEach(item ->
		{
			EnderecoResponse enderecoResponse = EnderecoMapper.INSTANCE.toEnderecoResponse(item.getEndereco()); 
			PessoaResponse pessoaResponse = PessoaMapper.INSTANCE.toPessoaResponse(item, enderecoResponse);
			listResponse.add(pessoaResponse);
		});
		return new ResponsePagedCommon<PessoaResponse>(listResponse, listPessoaBD.getTotalElements(), listPessoaBD.getTotalPages(), listPessoaBD.getSize(), 0);
	}

	public PessoaResponse findById(Long idPessoa)
	{
		Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow();
		EnderecoResponse enderecoResponse = EnderecoMapper.INSTANCE.toEnderecoResponse(pessoa.getEndereco()); 
		return PessoaMapper.INSTANCE.toPessoaResponse(pessoa, enderecoResponse);
	}
}
