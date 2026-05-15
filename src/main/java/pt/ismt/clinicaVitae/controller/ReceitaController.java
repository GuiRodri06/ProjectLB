package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.service.ReceitaService;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    // --- EMITIR RECEITA PARA UMA CONSULTA (POST) ---
    // O {idConsulta} vem na URL, ex:/receitas/emitir/1
    @PostMapping("/emitir/{idConsulta}")
    public ResponseEntity<?> emitirReceita(@PathVariable Integer idConsulta, @RequestBody Receita receita) {
        try {
            Receita novaReceita = receitaService.emitirReceita(idConsulta, receita);
            return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- BUSCAR RECEITA PELO ID DA CONSULTA (GET) ---
    @GetMapping("/consulta/{idConsulta}")
    public ResponseEntity<?> buscarPorConsulta(@PathVariable Integer idConsulta) {
        try {
            Receita receita = receitaService.buscarPorConsulta(idConsulta);
            return ResponseEntity.ok(receita);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}