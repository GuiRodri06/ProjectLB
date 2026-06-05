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

    // --- EMITIR NOVA RECEITA ---
    @Transactional // Se a inserção de algum item falhar, a receita completa sofre Rollback
    public Receita emitirReceita(Integer idConsulta, Receita receita) {

        // 1. Validação: Uma receita precisa obrigatoriamente estar atrelada a uma consulta real
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada. Não é possível emitir receita sem uma consulta."));

        // 2. Regra de Negócio (1 para 1): Uma consulta não pode ter duas receitas diferentes emitidas
        if (receitaRepository.findByConsultaIdConsulta(idConsulta).isPresent()) {
            throw new RuntimeException("Erro: Esta consulta já possui uma receita emitida!");
        }

        // 3. Vincula a entidade Receita à Consulta recuperada
        receita.setConsulta(consulta);

        // 4. Mecanismo de Bidirecionalidade do JPA/Hibernate:
        // Percorre a lista de medicamentos e faz com que cada item conheça a receita "mãe".
        // Isto é fundamental para que as chaves estrangeiras não fiquem nulas na tabela ReceitaItem.
        if (receita.getItens() != null && !receita.getItens().isEmpty()) {
            for (ReceitaItem item : receita.getItens()) {
                item.setReceita(receita); // Vincula o filho ao pai
            }
        }

        // 5. Salva a Receita. O CascadeType.ALL na entidade tratará de salvar a lista de itens em simultâneo.
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

    // --- REMOVER RECEITA ---
    @Transactional
    public void excluir(Integer id) {
        if (!receitaRepository.existsById(id)) {
            throw new RuntimeException("Impossível excluir: Receita não encontrada.");
        }
        // Devido à propriedade 'cascade = CascadeType.ALL', ao apagar a receita pai,
        // todos os itens/medicamentos associados na base de dados são automaticamente removidos em cascata.
        receitaRepository.deleteById(id);
    }
}