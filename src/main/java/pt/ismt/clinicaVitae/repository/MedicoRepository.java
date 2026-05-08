package pt.ismt.clinicaVitae.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Medico;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    List<Medico> findByNomeContainingIgnoreCase(String nome);

    List<Medico> findByEspecialidade(String especialidade);

    Optional<Medico> findByEmail(String email);

    Optional<Medico> findByTelemovel(String telemovel);
}
