package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Receita;
import pt.ismt.clinicaVitae.model.ReceitaItem;
import pt.ismt.clinicaVitae.repository.ReceitaItemRepository;
import pt.ismt.clinicaVitae.repository.ReceitaRepository;

import java.util.List;

@Service
public class ReceitaItemService {

    @Autowired
    private ReceitaItemRepository repository;

    @Autowired
    private ReceitaRepository receitaRepository;

    // --- ADICIONAR UM ITEM A UMA RECEITA JÁ EXISTENTE ---
    @Transactional
    public ReceitaItem adicionarItem(Integer idReceita, ReceitaItem novoItem) {
        Receita receita = receitaRepository.findById(idReceita)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        // Vincula o item à receita "mãe"
        novoItem.setReceita(receita);

        return repository.save(novoItem);
    }

    // --- LISTAR TODOS OS MEDICAMENTOS DE UMA RECEITA ---
    public List<ReceitaItem> listarPorReceita(Integer idReceita) {
        return repository.findByReceitaIdReceita(idReceita);
    }

    // --- ATUALIZAR DOSAGEM OU INSTRUÇÕES ---
    @Transactional
    public ReceitaItem atualizarItem(Integer idItem, ReceitaItem dadosNovos) {
        ReceitaItem itemExistente = repository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado na receita."));

        // Atualiza apenas o que é permitido mudar na prescrição
        itemExistente.setDosagem(dadosNovos.getDosagem());
        itemExistente.setInstrucoes(dadosNovos.getInstrucoes());

        return repository.save(itemExistente);
    }

    // --- REMOVER UM MEDICAMENTO ESPECÍFICO DA RECEITA ---
    @Transactional
    public void removerItem(Integer idItem) {
        if (!repository.existsById(idItem)) {
            throw new RuntimeException("Erro: Medicamento não encontrado.");
        }
        repository.deleteById(idItem);
    }
}