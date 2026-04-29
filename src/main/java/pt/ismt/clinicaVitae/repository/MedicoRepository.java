package pt.ismt.clinicaVitae.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Medico;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<Medico> findById(int id);
    // Procurar pelo nome
    List<Medico> findByName(String nome);
    // Procurar pela especialidade
    List<Medico> findBySpeciality(String especialidade);
    // Procurar pelo email
    List<Medico> findByEmail(String email);
    // Procurar pelo telemovel
    List<Medico> findByPhoneNumber(String telemovel);
    //----------------------------------------------------------------------//
}
