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

    boolean existsByMedicoIdMedicoAndDiaAndHora(Integer idMedico, LocalDate dia, LocalTime hora);

    // Para o Dashboard do Médico
    List<Consulta> findByMedicoIdMedicoAndDiaAndEstadoConsultaEnumOrderByHoraAsc(Integer idMedico, LocalDate dia, EstadoConsultaEnum estado);

    // Para o Dashboard da Rececionista
    List<Consulta> findByDiaOrderByHoraAsc(LocalDate dia);

    List<Consulta> findByPacienteIdPacienteAndEstadoConsultaEnumOrderByDiaDescHoraDesc(Integer idPaciente, EstadoConsultaEnum estado);

    List<Consulta> findByPacienteIdPacienteAndEstadoConsultaEnum(Integer idPaciente, EstadoConsultaEnum estado);
}