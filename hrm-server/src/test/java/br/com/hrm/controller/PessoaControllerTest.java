package br.com.hrm.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.hrm.entity.PessoaInput;
import br.com.hrm.service.PessoaService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class PessoaControllerTest {

    private static final String NOME = "João da Silva";
    private static final String SEXO = "M";
    private static final String EMAIL = "joao.silva@gmail.com";
    private static final LocalDate DATA_NASC = LocalDate.of(1992, 1, 30);
    private static final String NATURALIDADE = "Salvador/BA";
    private static final String NACIONALIDADE = "Brasileiro(a)";
    private static final String NOME2 = "Mário da Silva";
    private static final String SEXO2 = "M";
    private static final String EMAIL2 = "mario.silva@gmail.com";
    private static final LocalDate DATA_NASC2 = LocalDate.of(1990, 2, 20);
    private static final String NATURALIDADE2 = "São Paulo/SP";
    private static final String NACIONALIDADE2 = "Brasileiro(a)";

    private PessoaInput pessoa;

    private PessoaService pessoaService;

    private PessoaController pessoaController;


    @Before
    public void setUp() throws Exception {
        pessoaService = mock(PessoaService.class);
        pessoaController = new PessoaController(pessoaService);

        pessoa = new PessoaInput();
        pessoa.setNome(NOME);
        pessoa.setSexo(SEXO);
        pessoa.setEmail(EMAIL);
        pessoa.setDataNascimento(DATA_NASC);
        pessoa.setNaturalidade(NATURALIDADE);
        pessoa.setNacionalidade(NACIONALIDADE);

    }

    @Test
    public void deveRetornarPessoasCadastradas() {
        Map<String, Object> pessoas = new HashMap<>();
        pessoas.put("Pessoas", pessoa);

        pessoaService.saveOrUpdate(pessoa);
        ResponseEntity<Map<String, Object>> retorno = pessoaController.listAll();
        assertThat(retorno.getStatusCode(), is(HttpStatus.OK));
    }
}