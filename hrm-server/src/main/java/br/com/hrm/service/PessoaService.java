package br.com.hrm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.hrm.entity.Pessoa;
import br.com.hrm.entity.PessoaInput;
import br.com.hrm.exception.ResourceNotFoundException;
import br.com.hrm.dao.PessoaDAO;
import br.com.hrm.validation.PessoaValidator;

@Service
public class PessoaService {

    private static final String PESSOA_NOT_FOUND = "Pessoa não encontrada na base de dados com o id: ";
    private static final String PESSOAS_NOT_FOUND = "Nenhuma Pessoa foi encontrada na base de dados";
    private static final String PESSOA_CRIADA = "Pessoa criada com sucesso!";
    private static final String PESSOA_EXCLUIDA = "Pessoa excluída com sucesso!";
    private static final String PESSOA_ALTERADA = "Pessoa alterada com sucesso!";

    //@Autowired
    private PessoaDAO pessoaDao;

    @Autowired
    private PessoaValidator pessoaValidator;

    @Autowired
    public PessoaService(PessoaDAO pessoaDao) {
        this.pessoaDao = pessoaDao;
    }

    public Map<String, Object> getById(Long pessoaId) {
        Map<String, Object> mapPessoa = new HashMap<>();

        return pessoaDao.findById(pessoaId)
                .map(pessoa -> {
                    mapPessoa.put("Pessoa", pessoa);
                    return mapPessoa;
                })
                .orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));
    }

    public Map<String, Object> findAll2() {
        Map<String, Object> mapPessoas = new HashMap<>();
        mapPessoas.put("aaaa", "sadsad");
        return mapPessoas;
    }

    public Map<String, Object> findAll() {
        Map<String, Object> mapPessoas = new HashMap<>();

        List<Pessoa> pessoas = pessoaDao.findAllByOrderByNome();
        if (pessoas.isEmpty()) {
            throw new ResourceNotFoundException(PESSOAS_NOT_FOUND);
        }
        mapPessoas.put("Pessoas", pessoas);
        return mapPessoas;
    }

    public Map<String, Object> saveOrUpdate(PessoaInput pessoaInput){
        Map<String, Object> mapPessoa = new HashMap<>();

        Long pessoaId = pessoaInput.getId();
        if (pessoaId != null) {
            return pessoaDao.findById(pessoaId)
                    .map(pessoa -> {
                        pessoaValidator.validaEmail(pessoaInput.getEmail());
                        pessoa.setNome(pessoaInput.getNome());
                        pessoa.setSexo(pessoaInput.getSexo());
                        pessoa.setEmail(pessoaInput.getEmail());
                        pessoa.setDataNascimento(pessoaInput.getDataNascimento());
                        pessoa.setNaturalidade(pessoaInput.getNaturalidade());
                        pessoa.setNacionalidade(pessoaInput.getNacionalidade());
                        mapPessoa.put("message", PESSOA_ALTERADA);
                        mapPessoa.put("Pessoa", pessoaDao.saveAndFlush(pessoa));
                        return mapPessoa;
                    }).orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));

        } else {
            Pessoa pessoa = new Pessoa(null,
                    pessoaInput.getCpf(),
                    pessoaInput.getNome(),
                    pessoaInput.getSexo(),
                    pessoaInput.getEmail(),
                    pessoaInput.getDataNascimento(),
                    pessoaInput.getNaturalidade(),
                    pessoaInput.getNacionalidade());
            pessoaValidator.validaCampos(pessoa);
            mapPessoa.put("message", PESSOA_CRIADA);
            mapPessoa.put("Pessoa", pessoaDao.saveAndFlush(pessoa));
            return mapPessoa;
        }
    }

    public Map<String, Object> deleteById(Long pessoaId) {
        Map<String, Object> mapPessoa = new HashMap<>();

        return pessoaDao.findById(pessoaId)
                .map(pessoa -> {
            pessoaDao.delete(pessoa);
            mapPessoa.put("message", PESSOA_EXCLUIDA);
            return mapPessoa;
        }).orElseThrow(() -> new ResourceNotFoundException(PESSOA_NOT_FOUND + pessoaId));
    }

}
