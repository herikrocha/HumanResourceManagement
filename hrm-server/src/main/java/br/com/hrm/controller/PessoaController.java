package br.com.hrm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.hrm.entity.PessoaInput;
import br.com.hrm.service.PessoaService;

@RestController
@RequestMapping("api/pessoas")
public class PessoaController {

    //@Autowired
    private PessoaService pessoaService;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> listAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable("pessoaId") Long pessoaId) {
        return ResponseEntity.ok(pessoaService.getById(pessoaId));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> save(@RequestBody @Valid PessoaInput pessoaInput) {
        return ResponseEntity.ok(pessoaService.saveOrUpdate(pessoaInput));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> update(@RequestBody @Valid PessoaInput pessoaInput) {
        return ResponseEntity.ok(pessoaService.saveOrUpdate(pessoaInput));
    }

    @DeleteMapping("/{pessoaId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaService.deleteById(pessoaId));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> map = new HashMap<>();

        map.put("message", ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).findFirst());
        return map;
    }

}