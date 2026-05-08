package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

@Service // Avisa ao Spring que esta é a camada de lógica
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // --- FUNÇÃO DE CREATE (CRIAR) ---
    @Transactional // Garante que, se der erro no banco, nada seja salvo pela metade
    public Paciente salvar(Paciente paciente) {
        // Regra de negócio: não cadastrar se o CC já existir
        Optional<Paciente> jaExiste = pacienteRepository.findByCartaoCidadao(paciente.getCartaoCidadao());
        if (jaExiste.isPresent()) {
            throw new RuntimeException("Já existe um paciente cadastrado com este Cartão de Cidadão!");
        }
        return pacienteRepository.save(paciente);
    }

    // --- FUNÇÃO DE READ (LER) ---
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    // --- FUNÇÃO DE DELETE (APAGAR) ---
    @Transactional
    public void excluir(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado para exclusão!");
        }
        pacienteRepository.deleteById(id);
    }
}