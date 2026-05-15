package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.service.RecepcionistaService;

import java.util.List;

@RestController
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @PostMapping
    public ResponseEntity<Recepcionista> cadastrarRecepcionista(@RequestBody Recepcionista recepcionista) {
        Recepcionista novaRecepcionista = recepcionistaService.salvar(recepcionista);
        return new ResponseEntity<>(novaRecepcionista, HttpStatus.CREATED); //Cria a nova recepcionista e renorna um status de criado
    }

    @GetMapping
    public ResponseEntity<List<Recepcionista>> listarRecepcionistas() {
        List<Recepcionista> listaRecepcionistas = recepcionistaService.listarTodos();
        return ResponseEntity.ok(listaRecepcionistas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecepcionista(@PathVariable Integer id) {
        try {
            recepcionistaService.excluir(id);
            return ResponseEntity.noContent().build(); // Status 204 (Sucesso, sem conteúdo)
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Status 404 (Não encontrado)
        }
    }
}
