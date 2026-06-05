package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ismt.clinicaVitae.model.Paciente;
import pt.ismt.clinicaVitae.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

@Service // Avisa ao Spring que esta é a camada de lógica e regras de negócio do Paciente
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // --- CADASTRAR PACIENTE ---
    @Transactional // Executa de forma transacional para assegurar a integridade dos dados no banco
    public Paciente salvar(Paciente paciente) {
        // Regra de negócio: Garante a unicidade do paciente através do Cartão de Cidadão (CC)
        Optional<Paciente> jaExiste = pacienteRepository.findByCartaoCidadao(paciente.getCartaoCidadao());
        if (jaExiste.isPresent()) {
            throw new RuntimeException("Já existe um paciente cadastrado com este Cartão de Cidadão!");
        }
        return pacienteRepository.save(paciente);
    }

    // --- LISTAR TODOS ---
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // --- BUSCAR POR ID ---
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    // --- REMOVER PACIENTE ---
    @Transactional
    public void excluir(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado para exclusão!");
        }
        pacienteRepository.deleteById(id);
    }
}