package br.com.hrm.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hrm.dao.PessoaDAO;
import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.entity.Pessoa;
import br.com.hrm.exception.InvalidArgumentException;
import br.com.hrm.mapper.PessoaMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PessoaServiceTest {

    private static final String NAT = "Salvador/BA";
    private static final String NAC = "Brasileiro(a)";
    private static final String MASC = "Masculino";
    private static final String MAIL = "mail@gmail.com";
    private static final String MAIL_EDIT = "newmail@gmail.com";
    private static final String JOAO = "João da Silva";
    private static final String JOAO_CPF = "96289095911";
    private static final String CPF_INV = "96289095911654465";
    private static final LocalDate DTNASC = LocalDate.of(1992, 1, 30);
    private static final String MAIL_INV = "mailgmail.com";

    PessoaDTO joaoFull = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoSemCpf = PessoaDTO.builder().nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoCpfInv = PessoaDTO.builder().cpf(CPF_INV).nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoMailInv = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL_INV).dataNascimento(DTNASC).naturalidade(NAT).nacionalidade(NAC).build();

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private Pessoa entity;

    @Mock
    private PessoaMapper mapper;

    @Mock
    private PessoaDAO pessoaDAO;

    @Test
    void deveCriarPessoaComSucesso() {

        when(mapper.toEntity(joaoFull)).thenReturn(entity);
        when(pessoaDAO.save(entity)).thenReturn(entity);

        joaoFull = pessoaService.saveOrUpdate(joaoFull);

        assertEquals(JOAO, joaoFull.getNome());
        assertEquals(JOAO_CPF, joaoFull.getCpf());
        assertEquals(MASC, joaoFull.getSexo());
        assertEquals(MAIL, joaoFull.getEmail());
        assertEquals(DTNASC, joaoFull.getDataNascimento());
        assertEquals(NAT, joaoFull.getNaturalidade());
        assertEquals(NAC, joaoFull.getNacionalidade());
    }

    @Test
    void deveCriarEEditarPessoaComSucesso() {

        when(mapper.toEntity(joaoFull)).thenReturn(entity);
        when(pessoaDAO.save(entity)).thenReturn(entity);

        joaoFull = pessoaService.saveOrUpdate(joaoFull);
        joaoFull.setEmail(MAIL_EDIT);

        joaoFull = pessoaService.saveOrUpdate(joaoFull);

        assertEquals(JOAO, joaoFull.getNome());
        assertEquals(JOAO_CPF, joaoFull.getCpf());
        assertEquals(MASC, joaoFull.getSexo());
        assertEquals(MAIL_EDIT, joaoFull.getEmail());
        assertEquals(DTNASC, joaoFull.getDataNascimento());
        assertEquals(NAT, joaoFull.getNaturalidade());
        assertEquals(NAC, joaoFull.getNacionalidade());
    }

    @Test
    void deveLancarExcecaoSeAlgumaValidacaoFalhar() {

        assertThrows(InvalidArgumentException.class, () -> pessoaService.validaCampos(joaoSemCpf));
        assertThrows(InvalidArgumentException.class, () -> pessoaService.validaCampos(joaoCpfInv));
        assertThrows(InvalidArgumentException.class, () -> pessoaService.validaCampos(joaoMailInv));
    }

}
