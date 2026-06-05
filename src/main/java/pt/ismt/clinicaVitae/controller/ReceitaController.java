package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.service.ReceitaService;

@RestController // Define um controlador puramente REST (manipulação direta de JSON, sem templates HTML)
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    // --- EMITIR RECEITA PURA VIA API (POST) ---
    @PostMapping("/emitir/{idConsulta}")
    public ResponseEntity<?> emitirReceita(@PathVariable Integer idConsulta, @RequestBody Receita receita) {
        try {
            Receita novaReceita = receitaService.emitirReceita(idConsulta, receita);
            return new ResponseEntity<>(novaReceita, HttpStatus.CREATED); // Resposta 201 Created com o JSON gerado
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Erro 400 se violar alguma regra
        }
    }

    // --- PROCURAR RECEITA ASSOCIADA A UMA CONSULTA (GET) ---
    @GetMapping("/consulta/{idConsulta}")
    public ResponseEntity<?> buscarPorConsulta(@PathVariable Integer idConsulta) {
        try {
            Receita receita = receitaService.buscarPorConsulta(idConsulta);
            return ResponseEntity.ok(receita); // Resposta 200 OK com os dados da receita encontrada
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Erro 404 se não houver receita
        }
    }
}