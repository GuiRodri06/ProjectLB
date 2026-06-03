package pt.ismt.clinicaVitae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Mudamos para @Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.model.enums.CategoriaEnum;
import pt.ismt.clinicaVitae.model.enums.EstadoConsultaEnum;
import pt.ismt.clinicaVitae.service.ConsultaService;
import pt.ismt.clinicaVitae.service.MedicoService;
import pt.ismt.clinicaVitae.service.PacienteService;

@Controller // Transforma em controlador híbrido (Telas HTML + API JSON)
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private pt.ismt.clinicaVitae.service.ReceitaService receitaService;

    @Autowired
    private pt.ismt.clinicaVitae.service.ReceitaItemService receitaItemService;

    // ==========================================
    // 📋 ROTAS DE TELA (HTML / THYMELEAF)
    // ==========================================

    // --- TELA: EXIBIR FORMULÁRIO DE NOVO AGENDAMENTO (GET) ---
    @GetMapping("/nova")
    public String exibirFormularioAgendamento(Model model) {
        Consulta consulta = new Consulta();
        // Inicializa os objetos internos para evitar NullPointerException no binding do Thymeleaf
        consulta.setPaciente(new Paciente());
        consulta.setMedico(new Medico());

        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteService.listarTodos()); // Alimenta o select de Pacientes
        model.addAttribute("medicos", medicoService.listarTodos());     // Alimenta o select de Médicos
        model.addAttribute("categorias", CategoriaEnum.values());
        return "recepcionistas/nova-consulta"; // Abre o arquivo nova-consulta.html
    }

    // --- AÇÃO: GRAVAR AGENDAMENTO DO FORMULÁRIO (POST) ---
    @PostMapping("/salvar")
    public String salvarConsulta(@ModelAttribute("consulta") Consulta consulta) {
        // Define o estado padrão inicial como AGENDADA
        consulta.setEstadoConsultaEnum(EstadoConsultaEnum.AGENDADA);

        // Regra de negócio: O service tratará de associar os IDs e persistir
        consultaService.agendar(consulta);

        return "redirect:/recepcionistas/dashboard";
    }

    // --- TELA: EXIBIR FORMULÁRIO DE EDIÇÃO (GET) ---
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        // Procura a consulta que se pretende alterar
        // Nota: Garanta que tem um método para buscar por ID no seu consultaService
        Consulta consulta = consultaService.listarTodas().stream()
                .filter(c -> c.getIdConsulta().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("estados", EstadoConsultaEnum.values()); // Permite alterar para CONCLUIDA ou DESMARCADA
        model.addAttribute("categorias", CategoriaEnum.values());
        return "recepcionistas/editar-consulta"; // Abre o arquivo editar-consulta.html
    }

    // --- AÇÃO: ATUALIZAR DETALHES/STATUS DA CONSULTA (POST) ---
    @PostMapping("/atualizar/{id}")
    public String atualizarConsulta(@PathVariable Integer id, @ModelAttribute("consulta") Consulta consultaForm) {
        // Envia para o service lidar com a atualização no banco (Merge/Save)
        consultaService.agendar(consultaForm);
        return "redirect:/recepcionistas/dashboard";
    }

    @GetMapping("/atender/{id}")
    public String exibirAtendimento(@PathVariable Integer id, Model model) {
        Consulta consulta = consultaService.buscarPorId(id);
        model.addAttribute("consulta", consulta);

        // Injetamos a receita associada à consulta se já existir
        if (consulta.getReceita() != null) {
            model.addAttribute("receita", consulta.getReceita());
            model.addAttribute("itens", consulta.getReceita().getItens());
        } else {
            model.addAttribute("receita", null);
            model.addAttribute("itens", java.util.Collections.emptyList());
        }

        return "medicos/atender-consulta";
    }

    // --- REGISTAR E FINALIZAR CONSULTA ---
    @PostMapping("/salvar-prontuario/{id}")
    public String salvarProntuario(@PathVariable Integer id,
                                   @RequestParam String notasMedico,
                                   @RequestParam String estadoConsulta) {

        // Dispara a lógica no Service que atualiza as notas e altera o Enum para REALIZADA
        consultaService.atualizarNotasEStatus(id, notasMedico, estadoConsulta);

        // Redireciona o médico em segurança de volta para a sua listagem diária
        return "redirect:/medicos/dashboard";
    }

    @PostMapping("/atender/{idConsulta}/criar-receita")
    public String iniciarReceitaParaConsulta(@PathVariable Integer idConsulta,
                                             @RequestParam String observacoes,
                                             @RequestParam(required = false) String notasTemporarias) { // <-- AQUI

        // Guarda o texto que o médico estava a escrever
        if (notasTemporarias != null) {
            consultaService.salvarNotasTemporarias(idConsulta, notasTemporarias);
        }

        pt.ismt.clinicaVitae.model.Receita novaReceita = new pt.ismt.clinicaVitae.model.Receita();
        novaReceita.setObservacoes(observacoes.isEmpty() ? "Receita emitida durante a consulta." : observacoes);
        receitaService.emitirReceita(idConsulta, novaReceita);

        return "redirect:/consultas/atender/" + idConsulta;
    }

    // Rota para Adicionar um Medicamento à Receita pelo ecrã
    @PostMapping("/atender/{idConsulta}/receita/{idReceita}/adicionar-item")
    public String adicionarMedicamentoEcra(@PathVariable Integer idConsulta,
                                           @PathVariable Integer idReceita,
                                           @RequestParam String nomeProduto,
                                           @RequestParam String dosagem,
                                           @RequestParam String instrucoes_consumo,
                                           @RequestParam(required = false) String notasTemporarias) { // <-- AQUI

        // Guarda o texto que o médico estava a escrever
        if (notasTemporarias != null) {
            consultaService.salvarNotasTemporarias(idConsulta, notasTemporarias);
        }

        pt.ismt.clinicaVitae.model.ReceitaItem item = new pt.ismt.clinicaVitae.model.ReceitaItem();
        item.setNomeProduto(nomeProduto);
        item.setDosagem(dosagem);
        item.setInstrucoes_consumo(instrucoes_consumo);

        receitaItemService.adicionarItem(idReceita, item);
        return "redirect:/consultas/atender/" + idConsulta;
    }

    // Rota para Remover um Medicamento da Receita pelo ecrã
    @GetMapping("/atender/{idConsulta}/receita/remover-item/{idItem}")
    public String removerMedicamentoEcra(@PathVariable Integer idConsulta, @PathVariable Integer idItem) {
        receitaItemService.removerItem(idItem);
        return "redirect:/consultas/atender/" + idConsulta;
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarConsulta(@PathVariable Integer id) {
        try {
            consultaService.cancelar(id);
        } catch (Exception e) {
            // Podes adicionar um logger ou mensagem de erro aqui se quiseres
            System.out.println("Erro ao cancelar: " + e.getMessage());
        }

        // Redireciona de volta para o painel da receção (ajusta a rota se o teu dashboard for diferente)
        return "redirect:/recepcionistas/dashboard";
    }

}