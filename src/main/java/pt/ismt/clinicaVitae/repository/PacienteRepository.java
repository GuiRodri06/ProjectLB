package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Paciente;

import java.util.Date;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<Paciente> findById(int id);
    // Procurar pelo nome
    List<Paciente> findByName(String nome);
    // Procurar pelo CC
    List<Paciente> findByCC(String cartaoCidadao);
    // Procurar pela data de nascimento
    List<Paciente> findByDOB(Date dataNascimento);
    // Procurar pelo genero
    List<Paciente> findByGender(String genero);
    // Procurar pela nacionalidade
    List<Paciente> findByNationality(String nacionalidade);
    // Procurar pela morada
    List<Paciente> findByAddress(String morada);
    // Procurar pelo email
    List<Paciente> findByEmail(String email);
    // Procurar pelo telemovel
    List<Paciente> findByPhoneNumber(String telemovel);
    // Procurar pela altura
    List<Paciente> findbyHeight(double altura);
    // Procurar pelo peso
    List<Paciente> findbyWeight(double peso);
    //----------------------------------------------------------------------//
}
