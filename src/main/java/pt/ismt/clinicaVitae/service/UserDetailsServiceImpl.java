package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import pt.ismt.clinicaVitae.repository.RecepcionistaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Tenta buscar como Médico
        var medico = medicoRepository.findByEmail(username);
        if (medico.isPresent()) {
            return medico.get();
        }

        // 2. Se não for Médico, tenta buscar como Recepcionista
        var recepcionista = recepcionistaRepository.findByEmail(username);
        if (recepcionista.isPresent()) {
            return recepcionista.get();
        }

        // 3. Se não encontrar em nenhum, lança a exceção
        throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username);
    }
}