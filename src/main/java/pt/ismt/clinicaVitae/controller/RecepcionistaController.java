package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 1. IMPORTAÇÃO DO MODEL ADICIONADA
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.service.ConsultaService; // IMPORTAÇÃO DO SERVICE DE CONSULTAS
import pt.ismt.clinicaVitae.service.RecepcionistaService;
import java.util.List;

@Controller // Controlador híbrido (Páginas HTML + Endpoints API)
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @Autowired
    private ConsultaService consultaService; // 2. INJEÇÃO DO SERVICE DE CONSULTAS ADICIONADA


    @GetMapping("/dashboard")
    public String exibirDashboard(Model model) {
        List<Consulta> consultasDeHoje = consultaService.listarConsultasAtivasDoDiaRecepcao();
        model.addAttribute("consultas", consultasDeHoje);
        return "recepcionistas/dashboard";
    }
}