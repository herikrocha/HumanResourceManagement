package br.com.hrm.entity;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaInput extends EntityBase {

    private Long id;

    @Size(min = 11, max = 11, message = "Tamanho de CPF deve ser de 11 dígitos!")
    @Pattern(regexp = "[0-9]+", message = "CPF deve conter apenas números!")
    @NotBlank(message = "CPF deve ser informado!")
    private String cpf;

    @NotBlank(message = "Nome deve ser informado!")
    private String nome;

    private String sexo;

    private String email;

    @NotNull(message = "Data de nascimento deve ser informada!")
    @Past(message = "Data de nascimento deve ser estar no passado!")
    private LocalDate dataNascimento;

    private String naturalidade;

    private String nacionalidade;
}



