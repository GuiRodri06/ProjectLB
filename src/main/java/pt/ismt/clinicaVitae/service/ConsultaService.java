package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.model.enums.EstadoConsultaEnum;
import pt.ismt.clinicaVitae.repository.ConsultaRepository;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import pt.ismt.clinicaVitae.repository.PacienteRepository;

import java.time.LocalDate;
import java.util.List;

@Service // Indica ao Spring que esta classe contém a lógica de negócio e deve ser gerida pelo Container
public class ConsultaService {

    @Autowired // Injeta automaticamente a dependência do repositório de Consultas
    private ConsultaRepository consultaRepository;

    @Autowired // Injeta automaticamente o repositório de Médicos para validações
    private MedicoRepository medicoRepository;

    @Autowired // Injeta automaticamente o repositório de Pacientes para validações
    private PacienteRepository pacienteRepository;

    // --- AGENDAR NOVA CONSULTA ---
    @Transactional // Garante a atomicidade: se uma validação falhar, nenhuma alteração é guardada na Base de Dados
    public Consulta agendar(Consulta consulta) {
        // 1. Valida se o paciente informado realmente existe na base de dados
        if (!pacienteRepository.existsById(consulta.getPaciente().getIdPaciente())) {
            throw new RuntimeException("Erro: O paciente informado não existe no sistema.");
        }

        // 2. Valida se o médico informado realmente existe na base de dados
        if (!medicoRepository.existsById(consulta.getMedico().getIdMedico())) {
            throw new RuntimeException("Erro: O médico informado não existe no sistema.");
        }

        // 3. Regra de Negócio: Verifica se o médico já tem outra consulta marcada para o mesmo dia e hora
        boolean medicoOcupado = consultaRepository.existsByMedicoIdMedicoAndDiaAndHora(
                consulta.getMedico().getIdMedico(),
                consulta.getDia(),
                consulta.getHora()
        );

        if (medicoOcupado) {
            throw new RuntimeException("Erro: Este médico já possui uma consulta agendada para este dia e hora!");
        }

        // Se passar em todas as regras, guarda o agendamento no banco
        return consultaRepository.save(consulta);
    }

    // --- LISTAR TODAS AS CONSULTAS ---
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    // --- BUSCAR UMA CONSULTA POR ID ---
    public Consulta buscarPorId(Integer id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta com o ID " + id + " não foi encontrada."));
    }

    // --- CANCELAR CONSULTA (EXCLUSÃO LÓGICA) ---
    @Transactional
    public void cancelar(Integer id) {
        // Procura a consulta existente
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada para cancelamento."));

        // Altera o estado para CANCELADA (mantém o registo no banco para histórico clínico e auditoria)
        consulta.setEstadoConsultaEnum(EstadoConsultaEnum.CANCELADA);

        // Atualiza o registo
        consultaRepository.save(consulta);
    }

    // --- DASHBOARD MÉDICO: CONSULTAS ATIVAS DO DIA ---
    // Procura as consultas agendadas para o dia de hoje de um médico específico, ordenando por horário cronológico
    public List<Consulta> listarConsultasAtivasDoDiaPorMedico(Integer idMedico) {
        return consultaRepository.findByMedicoIdMedicoAndDiaAndEstadoConsultaEnumOrderByHoraAsc(
                idMedico,
                LocalDate.now(),
                EstadoConsultaEnum.AGENDADA
        );
    }

    // --- DASHBOARD RECEÇÃO: CONSULTAS ATIVAS DO DIA ---
    // Lista absolutamente todas as consultas do dia atual para controlo do fluxo da receção
    public List<Consulta> listarConsultasAtivasDoDiaRecepcao() {
        return consultaRepository.findByDiaOrderByHoraAsc(LocalDate.now());
    }

    // --- HISTÓRICO: LISTAR TODOS OS PACIENTES ---
    // Alimenta a tabela de pesquisa do Arquivo Geral de fichas de pacientes
    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    // --- HISTÓRICO: CLÍNICO DO PACIENTE ---
    // Filtra e retorna apenas as consultas que já foram REALIZADAS (concluídas) para montar a linha temporal do paciente
    public List<Consulta> listarHistoricoPaciente(Integer idPaciente) {
        return consultaRepository.findByPacienteIdPacienteAndEstadoConsultaEnum(
                idPaciente,
                EstadoConsultaEnum.REALIZADA
        );
    }

    // --- ATUALIZAR PRONTUÁRIO E STATUS ---
    @Transactional
    public void atualizarNotasEStatus(Integer id, String notas, String estado) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada para atualização."));

        consulta.setNotasMedico(notas); // Adiciona o diagnóstico ou notas clínicas do médico
        consulta.setEstadoConsultaEnum(EstadoConsultaEnum.valueOf(estado)); // Atualiza o estado da consulta (ex: REALIZADA)

        consultaRepository.save(consulta);
    }

    // --- SALVAR NOTAS TEMPORÁRIAS ---
    // Guarda o texto do prontuário em tempo real para evitar perdas se o médico navegar no ecrã para criar uma receita
    @Transactional
    public void salvarNotasTemporarias(Integer id, String notas) {
        Consulta consulta = buscarPorId(id);
        consulta.setNotasMedico(notas);
        consultaRepository.save(consulta);
    }
}