package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.ReceitaItem;
import java.util.List;

@Repository
public interface ReceitaItemRepository extends JpaRepository<ReceitaItem, Integer> {

    // Busca pelo nome do produto (medicamento)
    List<ReceitaItem> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

    // Busca todos os itens que pertencem a uma receita específica
    List<ReceitaItem> findByReceitaIdReceita(Integer idReceita);
}