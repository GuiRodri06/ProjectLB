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
    private PasswordEncoder passwordEncoder; // Injetando o encoder

    public Medico salvar(Medico medico) {
        // Regra: Não permitir emails duplicados
        if (medicoRepository.findByEmail(medico.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está sendo usado por outro médico.");
        }

        // CRÍTICO: Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(medico.getSenha());
        medico.setSenha(senhaCriptografada);

        return medicoRepository.save(medico);
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    @Transactional
    public void excluir(Integer id) {
        if (!medicoRepository.existsById(id)) {
            // Nota: Corrigi a mensagem de "Paciente" para "Médico"
            throw new RuntimeException("Médico não encontrado para exclusão!");
        }
        medicoRepository.deleteById(id);
    }
}