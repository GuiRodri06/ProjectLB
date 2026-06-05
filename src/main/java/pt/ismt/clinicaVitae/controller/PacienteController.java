package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.model.enums.GeneroEnum;
import pt.ismt.clinicaVitae.service.PacienteService;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // --- INTERFACE: EXIBIR FORMULÁRIO DE INGRESSO (GET) ---
    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        // Disponibiliza uma entidade Paciente vazia para mapear as caixas de texto do formulário
        model.addAttribute("paciente", new Paciente());
        // Envia as opções de género estruturadas (MASCULINO, FEMININO) para o seletor HTML
        model.addAttribute("generos", GeneroEnum.values());

        return "recepcionistas/novo-paciente"; // templates/recepcionistas/novo-paciente.html
    }

    // --- INTERFACE: PERSISTIR DADOS VIA FORMULÁRIO (POST) ---
    @PostMapping("/salvar")
    public String salvarPaciente(@ModelAttribute("paciente") Paciente paciente) {
        // @ModelAttribute extrai e monta o objeto Paciente a partir de todos os inputs enviados pelo ecrã
        service.salvar(paciente);

        // Redireciona para o painel principal da secretaria após o sucesso da gravação
        return "redirect:/recepcionistas/dashboard";
    }

    // --- ENDPOINTS DA API REST DE SUPORTE (JSON) ---

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Paciente> listarTodosAPI() {
        return service.listarTodos();
    }
}