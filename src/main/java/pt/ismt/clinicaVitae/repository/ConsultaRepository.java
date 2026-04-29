package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Medico;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<Consulta> findById(int id);
    // Procurar pelo categoria
    List<Consulta> findByCategory(String categoria);
    // Procurar pela descricao
    List<Consulta> findByDescription(String descricao);
    // Procurar pelo dia
    List<Consulta> findByDay(Date dia);
    // Procurar pela hora
    List<Consulta> findByHour(Time hora);
    // Procurar pelo dia e hora
    List<Consulta> findByDayAndHour(Date dia, Time hora);
    // Procurar pelas notas do medico
    List<Consulta> findByDoctorNotes(String notasMedico);
    // Procurar pelo ID do medico
    List<Consulta> findByDoctorID(int idMedico);
    // Procurar pelo ID do paciente
    List<Consulta> findByPatientID(int idPaciente);
    // Procurar pelo ID da recepcionista
    List<Consulta> findByReceptionistID(int idRecepcionista);
    //----------------------------------------------------------------------//
}
