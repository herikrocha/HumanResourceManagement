package br.com.hrm;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.hrm.controller.PessoaController;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class HumanResourceManagementTest {

    @Inject
    private PessoaController pessoaController;

    @Test
    void contextLoads() {

        assertNotNull(pessoaController);
    }

}
