package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.model.Recepcionista;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer> {

    // Email deve ser único, por isso usamos Optional
    Optional<Recepcionista> findByEmail(String email);

    // List para nomes, pois pode haver recepcionistas com o mesmo nome
    List<Recepcionista> findByNomeContainingIgnoreCase(String nome);

    Optional<Recepcionista> findByTelemovel(String telemovel);

}
