package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.repository.ConsultaRepository;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import pt.ismt.clinicaVitae.repository.PacienteRepository;

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

    // --- LISTAR AGENDA ---
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    // --- CANCELAR ---
    @Transactional
    public void cancelar(Integer id) {
        if (!consultaRepository.existsById(id)) {
            throw new RuntimeException("Erro: Consulta não encontrada.");
        }
        consultaRepository.deleteById(id);
    }
}