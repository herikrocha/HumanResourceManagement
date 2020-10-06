package br.com.hrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("source")
public class SourceController {

    private static final String URL_SOURCE = "https://github.com/herikrocha/HumanResourceManagement";

    @GetMapping
    public ResponseEntity<?> source() {
        return new ResponseEntity<>(URL_SOURCE, HttpStatus.OK);

    }
}
