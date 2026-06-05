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
@RequestMapping("/historico") // Concentra o módulo do Arquivo Clínico Geral da clínica
public class HistoricoController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * MÉTODOS AUXILIARES (Lógica Dinâmica de Interface):
     * Identifica se quem está a navegar é um MÉDICO ou RECEPCIONISTA para renderizar o botão "Voltar" correto.
     */
    private String determinarBackUrl(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEDICO"))
                ? "/medicos/dashboard"
                : "/recepcionistas/dashboard";
    }

    /**
     * Ecrã 1: Arquivo de Fichas - Lista todos os utentes registados na aplicação
     */
    @GetMapping("/pacientes")
    public String listaPacientes(Model model, Authentication authentication) {
        // Popula a tabela com todos os dados dos pacientes
        model.addAttribute("pacientes", consultaService.listarTodosPacientes());

        // Injeta a rota dinâmica de retorno baseado nas permissões de segurança
        String backUrl = determinarBackUrl(authentication);
        model.addAttribute("backUrl", backUrl);

        return "historico/lista-pacientes"; // templates/historico/lista-pacientes.html
    }

    /**
     * Ecrã 2: Linha Temporal do Paciente - Mostra dados vitais e todas as consultas REALIZADAS (passadas)
     */
    @GetMapping("/detalhes/{id}")
    public String verDetalhesPaciente(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        // 1. Localiza a ficha cadastral do paciente ou falha se não existir
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("paciente", paciente);

        // 2. Extrai unicamente o histórico clínico (consultas passadas, diagnósticos e receitas antigas)
        List<Consulta> listaDeConsultas = consultaService.listarHistoricoPaciente(id);
        model.addAttribute("consultasDoPaciente", listaDeConsultas);

        // 3. Define a navegação de retorno (volta sempre para a listagem do arquivo geral)
        model.addAttribute("backUrl", "/historico/pacientes");

        return "historico/detalhes-paciente"; // templates/historico/detalhes-paciente.html
    }
}