package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.service.ConsultaService;
import pt.ismt.clinicaVitae.service.RecepcionistaService;
import java.util.List;

@Controller
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @Autowired
    private ConsultaService consultaService;

    // --- PAINEL OPERACIONAL / DASHBOARD DE ATENDIMENTO DA RECEÇÃO ---
    @GetMapping("/dashboard")
    public String exibirDashboard(Model model) {
        // Carrega a listagem global de todas as consultas agendadas para o dia de hoje
        List<Consulta> consultasDeHoje = consultaService.listarConsultasAtivasDoDiaRecepcao();

        // Transmite o lote de consultas para exibição e controlo de fluxo (Ex: botão de Cancelamento)
        model.addAttribute("consultas", consultasDeHoje);

        return "recepcionistas/dashboard"; // templates/recepcionistas/dashboard.html
    }

    // --- ENDPOINTS DA API REST DE SUPORTE (JSON) ---

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public Recepcionista salvar(@RequestBody Recepcionista recepcionista) {
        return recepcionistaService.salvar(recepcionista);
    }
}