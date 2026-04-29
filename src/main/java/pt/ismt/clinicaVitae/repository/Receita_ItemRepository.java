package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

}
