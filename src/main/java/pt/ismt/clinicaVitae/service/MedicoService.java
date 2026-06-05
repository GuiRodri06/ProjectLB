package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o codificador BCrypt configurado na segurança da aplicação

    // --- CADASTRAR MÉDICO ---
    @Transactional
    public Medico salvar(Medico medico) {
        // Regra de Negócio: Impede a duplicação de e-mails para garantir logins únicos
        if (medicoRepository.findByEmail(medico.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está sendo usado por outro médico.");
        }

        // SEGURANÇA: Transforma a senha em texto limpo num Hash criptográfico BCrypt antes de salvar
        String senhaCriptografada = passwordEncoder.encode(medico.getSenha());
        medico.setSenha(senhaCriptografada);

        return medicoRepository.save(medico);
    }

    // --- LISTAR TODOS ---
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    // --- BUSCAR POR ID ---
    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    // --- REMOVER MÉDICO ---
    @Transactional
    public void excluir(Integer id) {
        // Validação preventiva para lançar uma exceção amigável caso o registo não exista
        if (!medicoRepository.existsById(id)) {
            throw new RuntimeException("Médico não encontrado para exclusão!");
        }
        medicoRepository.deleteById(id);
    }
}