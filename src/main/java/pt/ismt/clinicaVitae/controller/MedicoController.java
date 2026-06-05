package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.service.ConsultaService;
import pt.ismt.clinicaVitae.service.MedicoService;

import java.util.List;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService serviceMedico;

    @Autowired
    private ConsultaService consultaService;

    // --- PAINEL CLÍNICO / DASHBOARD MÉDICO ---
    @GetMapping("/dashboard")
    public String exibirDashboard(Model model, @AuthenticationPrincipal Medico medicoLogado) {
        // Extrai com segurança o ID do utilizador autenticado diretamente do contexto da sessão/token JWT
        Integer idMedico = medicoLogado.getIdMedico();

        // Filtra a agenda trazendo unicamente as consultas ativas marcadas para HOJE destinadas a este clínico
        List<Consulta> consultasDoDia = consultaService.listarConsultasAtivasDoDiaPorMedico(idMedico);

        // Disponibiliza as listas no ecrã para renderização no Thymeleaf
        model.addAttribute("medico", medicoLogado);
        model.addAttribute("consultas", consultasDoDia);

        return "medicos/dashboard"; // templates/medicos/dashboard.html
    }

    // --- ENPOINTS DA API REST DE SUPORTE (JSON) ---

    @PostMapping(consumes = "application/json")
    @ResponseBody // Indica que o retorno será JSON puro e não um ficheiro HTML
    public Medico salvar(@RequestBody Medico medico) {
        return serviceMedico.salvar(medico);
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Medico> listarTodos() {
        return serviceMedico.listarTodos();
    }
}