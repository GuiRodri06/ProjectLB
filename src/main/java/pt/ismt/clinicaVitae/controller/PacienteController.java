package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // Mudamos para @Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.model.enums.GeneroEnum;
import pt.ismt.clinicaVitae.service.PacienteService;

import java.util.List;

@Controller // Transforma em controlador híbrido (Telas + API)
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // --- TELA: EXIBIR FORMULÁRIO DE CADASTRO (GET) ---
    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        // Envia um objeto Paciente vazio para o Thymeleaf preencher os campos
        model.addAttribute("paciente", new Paciente());
        // Envia a lista de Enums do Género para o select do HTML
        model.addAttribute("generos", GeneroEnum.values());
        return "recepcionistas/novo-paciente"; // Abre o arquivo novo-paciente.html
    }

    // --- AÇÃO: SALVAR PACIENTE DO FORMULÁRIO (POST) ---
    @PostMapping("/salvar")
    public String salvarPaciente(@ModelAttribute("paciente") Paciente paciente) {
        // @ModelAttribute captura os dados enviados pelos inputs do HTML
        service.salvar(paciente);
        // Após salvar, redireciona a recepcionista de volta para o dashboard dela
        return "redirect:/recepcionistas/dashboard";
    }


}