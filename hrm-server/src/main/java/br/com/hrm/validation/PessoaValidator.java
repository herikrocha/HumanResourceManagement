package br.com.hrm.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hrm.dao.PessoaDAO;
import br.com.hrm.entity.Pessoa;
import br.com.hrm.exception.InvalidArgumentException;

@Component
public class PessoaValidator {

    @Autowired
    private PessoaDAO pessoaDao;

    private static EmailValidator emailValidator;

    static {
        emailValidator = EmailValidator.getInstance();
    }

    private static final String EMAIL_INVALIDO = "O conteúdo do campo email está inválido.";
    private static final String CPF_INVALIDO = "O conteúdo do campo CPF está inválido.";
    private static final String CPF_EXISTENTE = "O CPF informado já existe na base de dados.";


    public void validaCampos (Pessoa pessoa) {
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

        if (StringUtils.isNotBlank(cpf) && cpf.length() != 11) {
            throw new InvalidArgumentException(CPF_INVALIDO);
        }
        if (pessoaDao.existsPessoaByCpf(cpf)) {
            throw new InvalidArgumentException(CPF_EXISTENTE);
        }
    }

}
