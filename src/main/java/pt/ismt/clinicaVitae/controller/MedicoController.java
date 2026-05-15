package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.service.MedicoService;

import java.util.List;

@RestController // Define que esta classe é uma API REST
@RequestMapping("/medicos") // Define a URL base: http://localhost:8080/pacientes
public class MedicoController {

    @Autowired
    private MedicoService serviceMedico;

    // --- CADASTRAR (POST) ---
    @PostMapping
    public ResponseEntity<Medico> cadastrar(@RequestBody Medico medico) {
        // O @RequestBody transforma o JSON do Postman em um objeto Paciente
        Medico novoMedico = serviceMedico.salvar(medico);
        return new ResponseEntity<Medico>(novoMedico, HttpStatus.CREATED);
    }

    // --- LISTAR TODOS (GET) ---
    @GetMapping
    public ResponseEntity<List<Medico>> listar() {
        List<Medico> lista = serviceMedico.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // --- BUSCAR POR ID (GET) ---
    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Integer id) {
        return serviceMedico.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- EXCLUIR (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            serviceMedico.excluir(id);
            return ResponseEntity.noContent().build(); // Status 204 (Sucesso, sem conteúdo)
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
