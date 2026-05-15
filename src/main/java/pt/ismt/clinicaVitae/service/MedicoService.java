package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Medico;
import pt.ismt.clinicaVitae.model.Recepcionista;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public Medico salvar(Medico medico) {
        // Regra simples: não permitir emails duplicados para médicos
        if (medicoRepository.findByEmail(medico.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está sendo usado por outro médico.");
        }
        return medicoRepository.save(medico);
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    // --- FUNÇÃO DE DELETE (APAGAR) ---
    @Transactional
    public void excluir(Integer id) {
        if (!medicoRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado para exclusão!");
        }
        medicoRepository.deleteById(id);
    }
}