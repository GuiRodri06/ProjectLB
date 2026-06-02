package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder; // Injeção importante

    @Transactional
    public Recepcionista salvar(Recepcionista recepcionista) {
        if (repository.findByEmail(recepcionista.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado!");
        }
        // Criptografa a senha antes de salvar
        recepcionista.setPassword(passwordEncoder.encode(recepcionista.getPassword()));
        return repository.save(recepcionista);
    }

    // --- LISTAR TODOS ---
    public List<Recepcionista> listarTodos() {
        return repository.findAll();
    }

    public Optional<Recepcionista> buscarPorId(Integer id) {
        return repository.findById(id);
    }


    //DEPOIS ANALISAR ESSE BLOCO

    @Transactional
    public Recepcionista atualizar(Integer id, Recepcionista dadosAtualizados) {
        Recepcionista r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado."));

        r.setNome(dadosAtualizados.getNome());
        r.setEmail(dadosAtualizados.getEmail());
        r.setTelemovel(dadosAtualizados.getTelemovel());

        // Se uma nova senha foi enviada, criptografe-a antes de atualizar
        if (dadosAtualizados.getPassword() != null && !dadosAtualizados.getPassword().isEmpty()) {
            r.setPassword(passwordEncoder.encode(dadosAtualizados.getPassword()));
        }

        return repository.save(r);
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