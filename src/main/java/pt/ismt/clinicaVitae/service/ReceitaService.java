package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Consulta;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.model.ReceitaItem;
import pt.ismt.clinicaVitae.repository.ConsultaRepository;
import pt.ismt.clinicaVitae.repository.ReceitaRepository;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    // A Injeção do ReceitaItemRepository não é obrigatória aqui para salvar,
    // pois o CascadeType.ALL na entidade Receita fará o salvamento automático dos itens.

    // --- EMITIR NOVA RECEITA ---
    @Transactional // Se der erro em um medicamento, nada da receita é salvo.
    public Receita emitirReceita(Integer idConsulta, Receita receita) {

        // 1. Validar se a consulta existe
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada. Não é possível emitir receita sem uma consulta."));

        // 2. Validar se a consulta já possui uma receita (1 para 1)
        if (receitaRepository.findByConsultaIdConsulta(idConsulta).isPresent()) {
            throw new RuntimeException("Erro: Esta consulta já possui uma receita emitida!");
        }

        // 3. Vincular a Receita à Consulta
        receita.setConsulta(consulta);

        // 4. O Pulo do Gato (Bidirecionalidade):
        // Garantir que CADA medicamento da lista saiba a qual receita pertence
        if (receita.getItens() != null && !receita.getItens().isEmpty()) {
            for (ReceitaItem item : receita.getItens()) {
                item.setReceita(receita); // O item "aponta" para a mãe (Receita)
            }
        }

        // 5. Salvar (Isso salva a Receita E os ReceitaItems automaticamente por causa do Cascade)
        return receitaRepository.save(receita);
    }

    // --- BUSCAR RECEITA DA CONSULTA ---
    public Receita buscarPorConsulta(Integer idConsulta) {
        return receitaRepository.findByConsultaIdConsulta(idConsulta)
                .orElseThrow(() -> new RuntimeException("Nenhuma receita encontrada para a consulta " + idConsulta));
    }

    // --- BUSCAR POR ID ---
    public Receita buscarPorId(Integer id) {
        return receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));
    }

    // --- DELETAR ---
    @Transactional
    public void excluir(Integer id) {
        if (!receitaRepository.existsById(id)) {
            throw new RuntimeException("Impossível excluir: Receita não encontrada.");
        }
        // Ao deletar a Receita, o JPA também deletará todos os ReceitaItems vinculados a ela!
        receitaRepository.deleteById(id);
    }
}