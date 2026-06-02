package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Importante: Mudar para @Controller
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.service.RecepcionistaService;
import java.util.List;

@Controller // Agora é um controlador de páginas
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    // --- PÁGINA DASHBOARD (HTML) ---
    @GetMapping("/dashboard")
    public String exibirDashboard() {
        return "recepcionistas/dashboard"; // Deve existir em: src/main/resources/templates/recepcionistas/dashboard.html
    }

    // --- API REST (JSON) ---
    // Adicionamos @ResponseBody para dizer ao Spring: "Estes métodos retornam JSON, não HTML"

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public Recepcionista cadastrar(@RequestBody Recepcionista recepcionista) {
        return recepcionistaService.salvar(recepcionista);
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Recepcionista> listar() {
        return recepcionistaService.listarTodos();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void excluir(@PathVariable Integer id) {
        recepcionistaService.excluir(id);
    }
}