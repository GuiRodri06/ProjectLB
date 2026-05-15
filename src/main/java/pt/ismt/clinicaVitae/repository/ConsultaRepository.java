package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.enums.EstadoConsultaEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    // 1. Verificação de conflito de horário (A peça chave do seu Service)
    boolean existsByMedicoIdMedicoAndDiaAndHora(Integer idMedico, LocalDate dia, LocalTime hora);

    // 2. Buscar a agenda de um médico para um dia específico
    List<Consulta> findByMedicoIdMedicoAndDiaOrderByHoraAsc(Integer idMedico, LocalDate dia);

    // 3. Buscar todo o histórico de um paciente
    List<Consulta> findByPacienteIdPacienteOrderByDiaDesc(Integer idPaciente);

    // 4. Buscar consultas por estado (Ex: AGENDADA, REALIZADA, CANCELADA)
    List<Consulta> findByEstadoConsultaEnum(EstadoConsultaEnum estado);
}