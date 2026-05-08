package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Receita;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer> {

    // Busca por observações (usando Like para facilitar a pesquisa)
    List<Receita> findByObservacoesContainingIgnoreCase(String observacoes);

    // busca a receita pelo ID da consulta vinculada
    Optional<Receita> findByConsultaIdConsulta(Integer idConsulta);
}