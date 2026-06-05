package pt.ismt.clinicaVitae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.ismt.clinicaVitae.repository.MedicoRepository;
import pt.ismt.clinicaVitae.repository.RecepcionistaRepository;

@Service // Regista a classe para que o Spring Security a encontre como o motor principal de autenticação
public class UserDetailsServiceImpl implements UserDetailsService { // Implementa obrigatoriamente a interface nativa do Spring

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    // Método automático invocado pelo Spring Security no momento em que o utilizador clica em "Login"
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // --- ESTRATÉGIA MULTI-TABELA ---
        // Como temos tabelas separadas para perfis diferentes, o sistema faz uma pesquisa sequencial utilizando o e-mail:

        // 1. Passo: Procura o e-mail na tabela de Médicos
        var medico = medicoRepository.findByEmail(username);
        if (medico.isPresent()) {
            // Se encontrar, retorna a entidade Médico. Como a classe Medico implementa "UserDetails",
            // o Spring Security lê as autoridades ("ROLE_MEDICO") e valida a senha guardada
            return medico.get();
        }

        // 2. Passo: Se não for um médico, tenta procurar o e-mail na tabela de Recepcionistas
        var recepcionista = recepcionistaRepository.findByEmail(username);
        if (recepcionista.isPresent()) {
            // Se encontrar, retorna a Recepcionista (que também implementa UserDetails com "ROLE_RECEPCIONISTA")
            return recepcionista.get();
        }

        // 3. Passo: Se o e-mail inserido no ecrã de login não existir em nenhuma tabela, barra o acesso imediatamente
        throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username);
    }
}