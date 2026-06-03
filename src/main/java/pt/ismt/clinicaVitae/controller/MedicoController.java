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

    // --- DASHBOARD INTEGRADO COM SPRING SECURITY ---
    @GetMapping("/dashboard")
    public String exibirDashboard(Model model, @AuthenticationPrincipal Medico medicoLogado) {
        // Segurança: Pegamos o ID diretamente do token/sessão do utilizador autenticado
        Integer idMedico = medicoLogado.getIdMedico();

        // Filtramos as consultas do dia APENAS para este médico
        List<Consulta> consultasDoDia = consultaService.listarConsultasAtivasDoDiaPorMedico(idMedico);

        model.addAttribute("medico", medicoLogado);
        model.addAttribute("consultas", consultasDoDia);

        return "medicos/dashboard";
    }

    // --- ROTAS DE API (REST) CONTINUAM IGUAIS ---
    @PostMapping(consumes = "application/json")
    @ResponseBody
    public Medico cadastrar(@RequestBody Medico medico) {
        return serviceMedico.salvar(medico);
    }
}