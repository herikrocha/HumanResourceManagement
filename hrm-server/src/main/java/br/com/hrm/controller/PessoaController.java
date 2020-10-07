package br.com.hrm.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.service.PessoaService;

@CrossOrigin
@RestController
@RequestMapping("api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping()
    public ResponseEntity<List<PessoaDTO>> listAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<PessoaDTO> getById(@PathVariable("pessoaId") Long pessoaId) {
        return ResponseEntity.ok(pessoaService.getById(pessoaId));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PessoaDTO> save(@RequestBody @Valid PessoaDTO pessoaDTO) {
        return ResponseEntity.ok(pessoaService.saveOrUpdate(pessoaDTO));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<PessoaDTO> update(@RequestBody @Valid PessoaDTO pessoaDTO) {
        return ResponseEntity.ok(pessoaService.saveOrUpdate(pessoaDTO));
    }

    @DeleteMapping("/{pessoaId}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaService.deleteById(pessoaId));
    }

}