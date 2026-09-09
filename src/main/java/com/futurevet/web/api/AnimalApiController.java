package com.futurevet.web.api;

import com.futurevet.model.Animal;
import com.futurevet.service.AnimalService;
import com.futurevet.web.forms.AnimalForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animais")
public class AnimalApiController {
    private final AnimalService service;

    public AnimalApiController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Animal> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Animal buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> criar(@Valid @RequestBody AnimalForm form) {
        long id = service.salvar(null, form);
        return ResponseEntity.status(201).body(Map.of("id", id));
    }

    @PutMapping("/{id}")
    public Animal atualizar(@PathVariable long id, @Valid @RequestBody AnimalForm form) {
        service.salvar(id, form);
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}