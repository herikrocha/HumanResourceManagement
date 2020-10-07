package br.com.hrm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.hrm.dao.PessoaDAO;
import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.entity.Pessoa;
import br.com.hrm.exception.ResourceNotFoundException;
import br.com.hrm.validation.PessoaValidator;

@Service
public class PessoaService {

    private static final String PESSOA_NOT_FOUND = "Pessoa não encontrada na base de dados com o id: ";
    private static final String PESSOAS_NOT_FOUND = "Nenhuma Pessoa foi encontrada na base de dados";
    private static final String PESSOA_EXCLUIDA = "Pessoa excluída com sucesso!";

    private PessoaDAO pessoaDao;

    @Autowired
    private PessoaValidator pessoaValidator;

    @Autowired
    public PessoaService(PessoaDAO pessoaDao) {
        this.pessoaDao = pessoaDao;
    }

    public PessoaDTO getById(Long pessoaId) {

        return pessoaDao.findById(pessoaId)
                .map(pessoa -> new PessoaDTO(
                        pessoa.getId(), pessoa.getCpf(), pessoa.getNome(), pessoa.getSexo(), pessoa.getEmail(), pessoa.getDataNascimento(),
                        pessoa.getNaturalidade(), pessoa.getNacionalidade()
                ))
                .orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));
    }

    public List<PessoaDTO> findAll() {
        List<PessoaDTO> pessoas = pessoaDao.findAllByOrderByNome().stream().map(pessoa -> new PessoaDTO(
                pessoa.getId(), pessoa.getCpf(), pessoa.getNome(), pessoa.getSexo(), pessoa.getEmail(), pessoa.getDataNascimento(),
                pessoa.getNaturalidade(), pessoa.getNacionalidade()
        )).collect(Collectors.toList());

        if (pessoas.isEmpty()) {
            throw new ResourceNotFoundException(PESSOAS_NOT_FOUND);
        }

        return pessoas;
    }

    public PessoaDTO saveOrUpdate(PessoaDTO pessoaDTO){

        Long pessoaId = pessoaDTO.getId();
        if (pessoaId != null) {
            return pessoaDao.findById(pessoaId)
                    .map(pessoa -> {
                        pessoaValidator.validaEmail(pessoaDTO.getEmail());
                        pessoa.setNome(pessoaDTO.getNome());
                        pessoa.setSexo(pessoaDTO.getSexo());
                        pessoa.setEmail(pessoaDTO.getEmail());
                        pessoa.setDataNascimento(pessoaDTO.getDataNascimento());
                        pessoa.setNaturalidade(pessoaDTO.getNaturalidade());
                        pessoa.setNacionalidade(pessoaDTO.getNacionalidade());
                        pessoaDao.saveAndFlush(pessoa);
                        return pessoaDTO;
                    }).orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));
        } else {
            Pessoa pessoa = new Pessoa(null,
                    pessoaDTO.getCpf(),
                    pessoaDTO.getNome(),
                    pessoaDTO.getSexo(),
                    pessoaDTO.getEmail(),
                    pessoaDTO.getDataNascimento(),
                    pessoaDTO.getNaturalidade(),
                    pessoaDTO.getNacionalidade());
            pessoaValidator.validaCampos(pessoa);
            pessoaDao.saveAndFlush(pessoa);
            pessoaDTO.setId(pessoa.getId());
            return pessoaDTO;
        }
    }

    public String deleteById(Long pessoaId) {

        return pessoaDao.findById(pessoaId)
                .map(pessoa -> {
            pessoaDao.delete(pessoa);
            return PESSOA_EXCLUIDA;
        }).orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));
    }

}
