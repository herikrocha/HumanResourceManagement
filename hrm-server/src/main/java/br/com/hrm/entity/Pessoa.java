package br.com.hrm.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "pessoa", uniqueConstraints = @UniqueConstraint(columnNames = {"cpf"}))
public class Pessoa extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_pessoa")
    @SequenceGenerator(name = "seq_pessoa", sequenceName = "sequence_pessoa",  allocationSize = 1)
    private Long id;
    private String cpf;
    private String nome;
    private String sexo;
    private String email;
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;

    public Pessoa() {
        super();
    }

}



