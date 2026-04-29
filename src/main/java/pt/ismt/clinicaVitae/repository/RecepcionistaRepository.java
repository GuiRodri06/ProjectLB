package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.model.Recepcionista;

import java.util.List;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<Recepcionista> findById(int id);
    // Procurar pelo nome
    List<Recepcionista> findByName(String nome);
    // Procurar pelo email
    List<Recepcionista> findByEmail(String email);
    // Procurar pelo telemovel
    List<Recepcionista> findByPhoneNumber(String telemovel);
    // Procurar pela password   --Nao acho que seja para usar--
    List<Recepcionista> findByPassword(String password);
    //----------------------------------------------------------------------//
}
