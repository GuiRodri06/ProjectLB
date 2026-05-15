package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.service.PacienteService;

import java.util.List;

@RestController // Define que esta classe é uma API REST
@RequestMapping("/pacientes") // Define a URL base: http://localhost:8080/pacientes
public class PacienteController {

    @Autowired
    private PacienteService service;

    // --- CADASTRAR (POST) ---
    @PostMapping
    public ResponseEntity<Paciente> cadastrar(@RequestBody Paciente paciente) {
        // O @RequestBody transforma o JSON do Postman em um objeto Paciente
        Paciente novoPaciente = service.salvar(paciente);
        return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
    }

    // --- LISTAR TODOS (GET) ---
    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        List<Paciente> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // --- BUSCAR POR ID (GET) ---
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- EXCLUIR (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build(); // Status 204 (Sucesso, sem conteúdo)
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}