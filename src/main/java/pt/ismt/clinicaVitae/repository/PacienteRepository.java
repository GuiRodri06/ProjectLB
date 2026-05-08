package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.model.enums.GeneroEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    // 1. Campos ÚNICOS (Usamos Optional para facilitar a lógica no Service)
    Optional<Paciente> findByCartaoCidadao(String cartaoCidadao);

    Optional<Paciente> findByEmail(String email);

    // 2. Buscas por Texto (Usamos ContainingIgnoreCase para ser mais flexível)
    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    // 3. Outros filtros (Devem bater com o nome da variável no Model)
    List<Paciente> findByTelemovel(String telemovel);

    List<Paciente> findByDataNascimento(LocalDate dataNascimento);

    List<Paciente> findByGenero(GeneroEnum genero);

    List<Paciente> findByNacionalidade(String nacionalidade);
}