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
    private PasswordEncoder passwordEncoder; // Injeta o codificador de senhas (BCrypt)

    // --- REGISTAR RECEPCIONISTA ---
    @Transactional
    public Recepcionista salvar(Recepcionista recepcionista) {
        // Validação: Garante que o e-mail corporativo de login seja exclusivo
        if (repository.findByEmail(recepcionista.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado!");
        }
        // Encripta a credencial de segurança antes do armazenamento
        recepcionista.setPassword(passwordEncoder.encode(recepcionista.getPassword()));
        return repository.save(recepcionista);
    }

    // --- LISTAR TODOS ---
    public List<Recepcionista> listarTodos() {
        return repository.findAll();
    }

    // --- BUSCAR POR ID ---
    public Optional<Recepcionista> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    // --- ATUALIZAR PERFIL ---
    @Transactional
    public Recepcionista atualizar(Integer id, Recepcionista dadosAtualizados) {
        Recepcionista r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado."));

        // Copia os novos dados básicos enviados pela interface
        r.setNome(dadosAtualizados.getNome());
        r.setEmail(dadosAtualizados.getEmail());
        r.setTelemovel(dadosAtualizados.getTelemovel());

        // Regra Opcional: Se o utilizador preencheu o campo de senha no formulário,
        // significa que quer alterá-la. Então encripta-se a nova senha.
        if (dadosAtualizados.getPassword() != null && !dadosAtualizados.getPassword().isEmpty()) {
            r.setPassword(passwordEncoder.encode(dadosAtualizados.getPassword()));
        }

        return repository.save(r);
    }

    // --- REMOVER COLABORADOR ---
    @Transactional
    public void excluir(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Impossível excluir: Recepcionista não encontrado.");
        }
        repository.deleteById(id);
    }
}