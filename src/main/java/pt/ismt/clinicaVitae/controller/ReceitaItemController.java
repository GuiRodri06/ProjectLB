package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.ReceitaItem;
import pt.ismt.clinicaVitae.service.ReceitaItemService;

import java.util.List;

@RestController
@RequestMapping("/receita-itens") // URL Base: {{url}}/receita-itens
public class ReceitaItemController {

    @Autowired
    private ReceitaItemService receitaItemService;

    // --- ADICIONAR MAIS UM REMÉDIO A UMA RECEITA JÁ EXISTENTE (POST) ---
    // Ex: {{url}}/receita-itens/receita/1
    @PostMapping("/receita/{idReceita}")
    public ResponseEntity<?> adicionarItem(@PathVariable Integer idReceita, @RequestBody ReceitaItem novoItem) {
        try {
            ReceitaItem itemSalvo = receitaItemService.adicionarItem(idReceita, novoItem);
            return new ResponseEntity<>(itemSalvo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- LISTAR TODOS OS REMÉDIOS DE UMA RECEITA (GET) ---
    @GetMapping("/receita/{idReceita}")
    public ResponseEntity<List<ReceitaItem>> listarPorReceita(@PathVariable Integer idReceita) {
        List<ReceitaItem> itens = receitaItemService.listarPorReceita(idReceita);
        return ResponseEntity.ok(itens);
    }

    // --- ALTERAR A DOSAGEM DE UM REMÉDIO ESPECÍFICO (PUT) ---
    // Ex: {{url}}/receita-itens/1
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarItem(@PathVariable Integer id, @RequestBody ReceitaItem dadosNovos) {
        try {
            ReceitaItem itemAtualizado = receitaItemService.atualizarItem(id, dadosNovos);
            return ResponseEntity.ok(itemAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- DELETAR APENAS UM REMÉDIO DA RECEITA (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerItem(@PathVariable Integer id) {
        try {
            receitaItemService.removerItem(id);
            return ResponseEntity.ok("Medicamento removido da receita com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}