package pt.ismt.clinicaVitae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.ReceitaItem;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface Receita_ItemRepository extends JpaRepository<ReceitaItem, Integer>
{
    //------------------------------Procurar--------------------------------//
    // Procurar pelo ID
    List<ReceitaItem> findById(int id);
    // Procurar pelo nome do produto
    List<ReceitaItem> findByProductName(String nomeProduto);
    // Procurar pela dosagem
    List<ReceitaItem> findByDosage(String dosagem);
    // Procurar pelas instrucoes
    List<ReceitaItem> findByInstructions(String instrucoes);
    // Procurar pelo id da receita
    List<ReceitaItem> findByReceiptID(int idReceita);
    //----------------------------------------------------------------------//
}
