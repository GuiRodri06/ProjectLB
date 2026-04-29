package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Receita;

import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<Receita> findById(int id);
    // Procurar pelas observacoes
    List<Receita> findByObservations(String observacoes);
    // Procurar pelo id da consulta
    List<Receita> findByAppointmentID(int idConsulta);
    //----------------------------------------------------------------------//
}
