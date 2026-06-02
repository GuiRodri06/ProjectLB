package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.service.MedicoService;

import java.util.List;

@Controller // Mude de @RestController para @Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService serviceMedico;

    // --- ROTA DE PÁGINA (HTML) ---
    @GetMapping("/dashboard")
    public String exibirDashboard() {
        return "medicos/dashboard"; // Retorna o arquivo: templates/medicos/dashboard.html
    }

    // --- ROTAS DE API (REST) ---
    // Adicione @ResponseBody para que estes métodos continuem retornando JSON
    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Medico> listar() {
        return serviceMedico.listarTodos();
    }

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public Medico cadastrar(@RequestBody Medico medico) {
        return serviceMedico.salvar(medico);
    }
}
