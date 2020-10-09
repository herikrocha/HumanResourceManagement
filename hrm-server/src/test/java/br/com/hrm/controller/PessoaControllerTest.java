package br.com.hrm.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import br.com.hrm.HumanResourceManagement;
import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.utils.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = { HumanResourceManagement.class, RestUtils.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PessoaControllerTest {

    private static final String PESSOA_URI = "api/pessoas";

    private static final String NAT = "Salvador/BA";
    private static final String NAC = "Brasileiro(a)";
    private static final String MASC = "Masculino";
    private static final String MAIL = "mail@gmail.com";
    private static final String MAIL_EDIT = "newmail@gmail.com";
    private static final String MAIL_INV = "mailgmail.com";
    private static final String JOAO = "João da Silva";
    private static final String JOAO_CPF = "88289095911";
    private static final String JOAO_INV = "04874560006654";
    private static final LocalDate DTNASC_JOAO = LocalDate.of(1992, 1, 30);
    private static final LocalDate DTNASC_JOAO_FUTURO = LocalDate.of(2022, 1, 30);
    private static final String MARIO = "Mário de Souza";
    private static final String MARIO_CPF = "89080095911";
    private static final LocalDate DTNASC_MARIO = LocalDate.of(1990, 1, 30);

    PessoaDTO joaoFull = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoEditFull = PessoaDTO.builder().id(10L).cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL_EDIT).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoSemCpf = PessoaDTO.builder().nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoCpfInv = PessoaDTO.builder().cpf(JOAO_INV).nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoMailInv = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL_INV).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoDtnascFuturo = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL).dataNascimento(DTNASC_JOAO_FUTURO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO joaoSemDtnasc = PessoaDTO.builder().cpf(JOAO_CPF).nome(JOAO).sexo(MASC).email(MAIL).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO pessoSemNome = PessoaDTO.builder().cpf(JOAO_CPF).sexo(MASC).email(MAIL).dataNascimento(DTNASC_JOAO).naturalidade(NAT).nacionalidade(NAC).build();
    PessoaDTO marioFull = PessoaDTO.builder().cpf(MARIO_CPF).nome(MARIO).sexo(MASC).email(MAIL).dataNascimento(DTNASC_MARIO).naturalidade(NAT).nacionalidade(NAC).build();

    @LocalServerPort
    private int port;

    @Inject
    private RestUtils restUtils;

    @BeforeAll
    void configure() {
        restUtils.setPort(port);
    }

    @Test
    void deveRetornarExcecaoSemAutorizacaoAoCriarEntidade() throws JsonProcessingException {
        assertThrows(ResourceAccessException.class, () -> restUtils.post(PESSOA_URI, joaoFull, false));
    }

    @Test
    void deveRetornarExcecaoSemAutorizacaoAoPesquisarEntidadePorId() throws JsonProcessingException {
        assertEquals(HttpStatus.UNAUTHORIZED, restUtils.getById(PESSOA_URI, 5L, false).getStatusCode());
    }

    @Test
    void deveRetornarExcecaoSemAutorizacaoAoExcluirEntidade() throws JsonProcessingException {
        assertThrows(ResourceAccessException.class, () -> restUtils.deleteById(PESSOA_URI, 5L, false));
    }

    @Test
    void deveRetornarExcecaoSemAutorizacaoAoPesquisarEntidade() throws JsonProcessingException {
        assertThrows(RestClientException.class, () -> restUtils.getAll(PESSOA_URI, false, PessoaDTO.class));
    }

    @Test
    void deveRetornarExcecaoSemAutorizacaoAoEditarEntidade() throws JsonProcessingException {
        assertThrows(RestClientException.class, () -> restUtils.put(PESSOA_URI, joaoEditFull, false));
    }

    @Test
    void deveRetornarErroSemCPFAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoSemCpf, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(response.getBody(), Map.class);
        assertEquals("CPF deve ser informado!", map.get("message"));
    }

    @Test
    void deveRetornarErroCPFInvalidoAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoCpfInv, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(response.getBody(), Map.class);
        assertEquals("Tamanho de CPF deve ser de 11 dígitos!", map.get("message"));
    }

    @Test
    void deveRetornarErroEmailInvalidoAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoMailInv, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("O conteúdo do campo email está inválido.", response.getBody());
    }

    @Test
    void deveRetornarErroDtNascFuturoAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoDtnascFuturo, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(response.getBody(), Map.class);
        assertEquals("Data de nascimento deve ser estar no passado!", map.get("message"));
    }

    @Test
    void deveRetornarErroSemDtNascAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoSemDtnasc, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(response.getBody(), Map.class);
        assertEquals("Data de nascimento deve ser informada!", map.get("message"));
    }

    @Test
    void deveRetornarErroSemNomeAoCriarEntidade() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, pessoSemNome, true);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(response.getBody(), Map.class);
        assertEquals("Nome deve ser informado!", map.get("message"));
    }

    @Test
    void deveRetornarEntidadeCriadaEExcluir() throws JsonProcessingException {
        ResponseEntity<String> response = restUtils.post(PESSOA_URI, joaoFull, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Long joaoId = restUtils.convert(response.getBody(), PessoaDTO.class).getId();
        assertAll(response::getBody, () -> restUtils.convert(response.getBody(), PessoaDTO.class).getId());
        assertEquals(HttpStatus.OK, restUtils.deleteById(PESSOA_URI, joaoId, true).getStatusCode());
    }

    @Test
    void deveCriarEntidadesRetornalasNaBuscaEExcluir() throws JsonProcessingException {
        ResponseEntity<String> responseCreateJoao = restUtils.post(PESSOA_URI, joaoFull, true);
        assertEquals(HttpStatus.OK, responseCreateJoao.getStatusCode());
        Long joaoId = restUtils.convert(responseCreateJoao.getBody(), PessoaDTO.class).getId();

        ResponseEntity<String> responseCreateMario = restUtils.post(PESSOA_URI, marioFull, true);
        assertEquals(HttpStatus.OK, responseCreateMario.getStatusCode());
        Long marioId = restUtils.convert(responseCreateMario.getBody(), PessoaDTO.class).getId();

        ResponseEntity<Collection<PessoaDTO>> responseGetAll = restUtils.getAll(PESSOA_URI, true, PessoaDTO.class);
        assertEquals( HttpStatus.OK, responseGetAll.getStatusCode());

        responseGetAll.getBody().stream().anyMatch(pessoaDTO -> pessoaDTO.getCpf().equals(joaoFull.getCpf()));
        responseGetAll.getBody().stream().anyMatch(pessoaDTO -> pessoaDTO.getCpf().equals(marioFull.getCpf()));

        assertEquals(HttpStatus.OK, restUtils.deleteById(PESSOA_URI, joaoId, true).getStatusCode());
        assertEquals(HttpStatus.OK, restUtils.deleteById(PESSOA_URI, marioId, true).getStatusCode());
    }

    @Test
    void deveCriarEntidadeERetornarRegistroCriadoNaBuscaPorIdEExcluir() throws JsonProcessingException {
        ResponseEntity<String> responseCreate = restUtils.post(PESSOA_URI, joaoFull, true);
        assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

        Long joaoId = restUtils.convert(responseCreate.getBody(), PessoaDTO.class).getId();
        ResponseEntity<String> responseGetById = restUtils.getById(PESSOA_URI, joaoId, true);

        assertEquals(HttpStatus.OK, responseGetById.getStatusCode());
        assertEquals(joaoId, restUtils.convert(responseGetById.getBody(), PessoaDTO.class).getId());
        assertEquals(HttpStatus.OK, restUtils.deleteById(PESSOA_URI, joaoId, true).getStatusCode());

    }

    @Test
    void deveCriarEntidadeEditalaRetornalaNaBuscaPorIdEExcluir() throws JsonProcessingException {

        ResponseEntity<String> responseCreate = restUtils.post(PESSOA_URI, joaoFull, true);
        assertEquals(HttpStatus.OK, responseCreate.getStatusCode());
        Long joaoId = restUtils.convert(responseCreate.getBody(), PessoaDTO.class).getId();

        joaoEditFull.setId(joaoId);
        ResponseEntity<String> responseEditJoao = restUtils.put(PESSOA_URI, joaoEditFull, true);
        assertEquals(HttpStatus.OK, responseEditJoao.getStatusCode());

        ResponseEntity<String> responseGetById = restUtils.getById(PESSOA_URI, joaoId, true);
        assertEquals(HttpStatus.OK, responseGetById.getStatusCode());
        assertEquals(joaoEditFull.getEmail(), restUtils.convert(responseGetById.getBody(), PessoaDTO.class).getEmail());
        assertEquals(HttpStatus.OK, restUtils.deleteById(PESSOA_URI, joaoId, true).getStatusCode());
    }
}