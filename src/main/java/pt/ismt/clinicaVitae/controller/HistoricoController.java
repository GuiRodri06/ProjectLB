package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.repository.PacienteRepository;
import pt.ismt.clinicaVitae.service.ConsultaService;

import java.util.List;

@Controller
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Método auxiliar para determinar a URL de volta conforme o role do utilizador
     */
    private String determinarBackUrl(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEDICO"))
                ? "/medicos/dashboard"
                : "/recepcionistas/dashboard";
    }

    /**
     * Ecrã 1: Lista todos os pacientes do sistema
     */
    @GetMapping("/pacientes")
    public String listaPacientes(Model model, Authentication authentication) {
        model.addAttribute("pacientes", consultaService.listarTodosPacientes());

        // Adicionar URL de volta conforme o role
        String backUrl = determinarBackUrl(authentication);
        model.addAttribute("backUrl", backUrl);

        return "historico/lista-pacientes";
    }

    /**
     * Ecrã 2: Ficha Clínica com a linha do tempo de consultas do paciente
     */
    @GetMapping("/detalhes/{id}")
    public String verDetalhesPaciente(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        // 1. Procura o paciente
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("paciente", paciente);

        // 2. Procura as consultas deste paciente
        // Garante que usas o método do teu ConsultaRepository que filtra pelo ID do paciente
        List<Consulta> listaDeConsultas = consultaService.listarHistoricoPaciente(id);
        model.addAttribute("consultasDoPaciente", listaDeConsultas);

        // 3. Adicionar URL de volta (volta sempre para o arquivo/lista de pacientes)
        model.addAttribute("backUrl", "/historico/pacientes");

        return "historico/detalhes-paciente";
    }
}