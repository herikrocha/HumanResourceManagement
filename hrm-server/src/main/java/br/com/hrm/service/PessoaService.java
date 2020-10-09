package br.com.hrm.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import br.com.hrm.dao.PessoaDAO;
import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.entity.Pessoa;
import br.com.hrm.exception.InvalidArgumentException;
import br.com.hrm.exception.ResourceNotFoundException;

@Service
public class PessoaService {

    private static EmailValidator emailValidator;

    static {
        emailValidator = EmailValidator.getInstance();
    }

    private static final String EMAIL_INVALIDO = "O conteúdo do campo email está inválido.";
    private static final String CPF_INVALIDO = "O conteúdo do campo CPF está inválido.";
    private static final String CPF_EXISTENTE = "O CPF informado já existe na base de dados.";
    private static final String CPF_NAO_INFORMADO = "O CPF deve ser informado!.";
    private static final String PESSOA_NOT_FOUND = "Pessoa não encontrada na base de dados com o id: ";
    private static final String PESSOAS_NOT_FOUND = "Nenhuma Pessoa foi encontrada na base de dados";
    private static final String PESSOA_EXCLUIDA = "Pessoa excluída com sucesso!";

    @Inject
    private PessoaDAO pessoaDao;

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
                        validaEmail(pessoaDTO.getEmail());
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
            validaCampos(pessoaDTO);
            Pessoa pessoa = new Pessoa(null,
                    pessoaDTO.getCpf(),
                    pessoaDTO.getNome(),
                    pessoaDTO.getSexo(),
                    pessoaDTO.getEmail(),
                    pessoaDTO.getDataNascimento(),
                    pessoaDTO.getNaturalidade(),
                    pessoaDTO.getNacionalidade());
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


    public void validaCampos(PessoaDTO pessoa) {
        if (StringUtils.isNotBlank(pessoa.getEmail())) {
            validaEmail(pessoa.getEmail());
        }
        validaCpf(pessoa.getCpf());
    }

    public void validaEmail(String email) {
        if (StringUtils.isNotBlank(email) && !emailValidator.isValid(email)) {
            throw new InvalidArgumentException(EMAIL_INVALIDO);
        }
    }

    public void validaCpf(String cpf) {

        if (StringUtils.isBlank(cpf)) {
            throw new InvalidArgumentException(CPF_NAO_INFORMADO);
        }
        if (StringUtils.isNotBlank(cpf) && cpf.length() != 11) {
            throw new InvalidArgumentException(CPF_INVALIDO);
        }
        if (pessoaDao.existsPessoaByCpf(cpf)) {
            throw new InvalidArgumentException(CPF_EXISTENTE);
        }
    }

}
