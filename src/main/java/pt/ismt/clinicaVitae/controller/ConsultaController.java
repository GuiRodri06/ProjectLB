package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.service.ConsultaService;

import java.util.List;

@RestController
@RequestMapping("/consultas") // URL Base: {{url}}/consultas
public class ConsultaController {

    @Autowired
    private ConsultaService service;

    // --- AGENDAR CONSULTA (POST) ---
    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@RequestBody Consulta consulta) {
        try {
            Consulta novaConsulta = service.agendar(consulta);
            return new ResponseEntity<>(novaConsulta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Se o service lançar um erro (ex: médico ocupado), pegamos a mensagem aqui
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- LISTAR TODAS (GET) ---
    @GetMapping
    public ResponseEntity<List<Consulta>> listarTodas() {
        List<Consulta> lista = service.listarTodas();
        return ResponseEntity.ok(lista);
    }

    // --- CANCELAR CONSULTA (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelar(@PathVariable Integer id) {
        try {
            service.cancelar(id);
            return ResponseEntity.ok("Consulta cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}