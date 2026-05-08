package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.repository.RecepcionistaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecepcionistaService {

    @Autowired
    private RecepcionistaRepository repository;

    // --- CRIAR / SALVAR ---
    @Transactional
    public Recepcionista salvar(Recepcionista recepcionista) {
        // Validação: Não permitir dois funcionários com o mesmo e-mail
        Optional<Recepcionista> existente = repository.findByEmail(recepcionista.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado para outro recepcionista!");
        }
        return repository.save(recepcionista);
    }

    // --- LISTAR TODOS ---
    public List<Recepcionista> listarTodos() {
        return repository.findAll();
    }

    // --- BUSCAR POR ID ---
    public Recepcionista buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista com ID " + id + " não encontrado."));
    }


    //DEPOIS ANALISAR ESSE BLOCO

    // --- ATUALIZAR ---
    @Transactional
    public Recepcionista atualizar(Integer id, Recepcionista dadosAtualizados) {
        Recepcionista recepcionistaExistente = buscarPorId(id);

        // Atualiza apenas os campos necessários
        recepcionistaExistente.setNome(dadosAtualizados.getNome());
        recepcionistaExistente.setEmail(dadosAtualizados.getEmail());
        recepcionistaExistente.setTelemovel(dadosAtualizados.getTelemovel());

        // Se a senha não for nula nem vazia, atualiza também
        if (dadosAtualizados.getPassword() != null && !dadosAtualizados.getPassword().isEmpty()) {
            recepcionistaExistente.setPassword(dadosAtualizados.getPassword());
        }

        return repository.save(recepcionistaExistente);
    }

    // --- EXCLUIR ---
    @Transactional
    public void excluir(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Impossível excluir: Recepcionista não encontrado.");
        }
        repository.deleteById(id);
    }
}