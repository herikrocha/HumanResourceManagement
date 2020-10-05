package br.com.hrm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hrm.entity.Pessoa;

@Repository
public interface PessoaDAO extends JpaRepository<Pessoa, Long> {

    boolean existsPessoaByCpf(String cpf);

    List<Pessoa> findAllByOrderByNome();
}
