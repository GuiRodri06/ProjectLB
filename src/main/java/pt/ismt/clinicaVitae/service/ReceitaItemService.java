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
        // Carrega a receita principal (mãe)
        Receita receita = receitaRepository.findById(idReceita)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        // Cria o vínculo de Chave Estrangeira: faz o item apontar para a receita correta
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

        // Altera apenas os campos textuais de posologia permitidos pelo sistema
        itemExistente.setDosagem(dadosNovos.getDosagem());
        itemExistente.setInstrucoes_consumo(dadosNovos.getInstrucoes_consumo());

        return repository.save(itemExistente);
    }

    // --- REMOVER UM MEDICAMENTO ESPECÍFICO DA RECEITA ---
    @Transactional
    public void removerItem(Integer idItem) {
        if (!repository.existsById(idItem)) {
            throw new RuntimeException("Erro: Medicamento não encontrado.");
        }
        // Apaga apenas a linha deste remédio específico sem afetar o resto da receita
        repository.deleteById(idItem);
    }
}