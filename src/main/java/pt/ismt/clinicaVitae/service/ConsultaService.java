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

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // --- AGENDAR NOVA CONSULTA ---
    @Transactional
    public Consulta agendar(Consulta consulta) {
        // 1. Validar Paciente
        if (!pacienteRepository.existsById(consulta.getPaciente().getIdPaciente())) {
            throw new RuntimeException("Erro: O paciente informado não existe no sistema.");
        }

        // 2. Validar Médico
        if (!medicoRepository.existsById(consulta.getMedico().getIdMedico())) {
            throw new RuntimeException("Erro: O médico informado não existe no sistema.");
        }

        // 3. Validar Disponibilidade
        boolean medicoOcupado = consultaRepository.existsByMedicoIdMedicoAndDiaAndHora(
                consulta.getMedico().getIdMedico(),
                consulta.getDia(),
                consulta.getHora()
        );

        if (medicoOcupado) {
            throw new RuntimeException("Erro: Este médico já possui uma consulta agendada para este dia e hora!");
        }

        // Se passar por tudo, salvamos
        return consultaRepository.save(consulta);
    }

    // --- LISTAR AGENDA GERAL ---
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    // --- BUSCAR UMA CONSULTA POR ID ---
    public Consulta buscarPorId(Integer id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta com o ID " + id + " não foi encontrada."));
    }

    // --- CANCELAR (CORRIGIDO: Transforma a exclusão física em lógica) ---
    @Transactional
    public void cancelar(Integer id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada para cancelamento."));

        // Em vez de deleteById, mudamos o estado. O histórico fica salvo e os erros de FK somem!
        consulta.setEstadoConsultaEnum(EstadoConsultaEnum.CANCELADA);

        consultaRepository.save(consulta);
    }

    // --- DASHBOARD MÉDICO: CONSULTAS ATIVAS DO DIA (Problema 2 e 3) ---
    // Filtra apenas por AGENDADA (some com as REALIZADAS) e ordena por HoraAsc
    public List<Consulta> listarConsultasAtivasDoDiaPorMedico(Integer idMedico) {
        return consultaRepository.findByMedicoIdMedicoAndDiaAndEstadoConsultaEnumOrderByHoraAsc(
                idMedico,
                LocalDate.now(),
                EstadoConsultaEnum.AGENDADA
        );
    }

    // --- DASHBOARD RECEÇÃO: CONSULTAS ATIVAS DO DIA (Problema 2 e 3) ---
    // Traz todas as consultas AGENDADAS de hoje da clínica, ordenadas por hora
    public List<Consulta> listarConsultasAtivasDoDiaRecepcao() {
        return consultaRepository.findByDiaOrderByHoraAsc(
                LocalDate.now()
        );
    }

    // --- HISTÓRICO: LISTAR TODOS OS PACIENTES (Adicionado para o Problema 3) ---
    // Alimenta a tabela de pesquisa do Arquivo Geral
    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    // --- HISTÓRICO: CLINICO DO PACIENTE (Adicionado para o Problema 3) ---
    // Carrega a linha do tempo de consultas já REALIZADAS por ordem decrescente (da mais recente para a mais antiga)
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

        consulta.setNotasMedico(notas);
        consulta.setEstadoConsultaEnum(EstadoConsultaEnum.valueOf(estado));

        consultaRepository.save(consulta);
    }

    // --- SALVAR NOTAS TEMPORÁRIAS (Evita perder texto ao adicionar receita) ---
    @Transactional
    public void salvarNotasTemporarias(Integer id, String notas) {
        Consulta consulta = buscarPorId(id);
        consulta.setNotasMedico(notas);
        consultaRepository.save(consulta);
    }

}