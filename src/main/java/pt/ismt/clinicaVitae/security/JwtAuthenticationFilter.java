package pt.ismt.clinicaVitae.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Filtro customizado de Segurança que intercepta OBRIGATORIAMENTE cada pedido HTTP que chega à aplicação
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService; // Injeta o nosso UserDetailsServiceImpl para validar utilizadores contra a BD

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // 1. RECOLHA DO TOKEN VIA COOKIE:
        // Como alterámos a arquitetura para mitigar ataques XSS, o token não vem no Header Authorization, mas sim em Cookies seguros
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // Procura especificamente o Cookie batizado com o nome "JWT_TOKEN"
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue(); // Extrai a string criptográfica do token
                    break;
                }
            }
        }

        // 2. PROCESSAMENTO E AUTORIZAÇÃO:
        // Se encontrámos o token e ele passou nos testes de integridade e validade do JwtTokenProvider
        if (token != null && tokenProvider.validateToken(token)) {
            // Extrai o e-mail escondido dentro das propriedades do token
            String username = tokenProvider.getUsernameFromToken(token);

            // Pergunta à nossa base de dados (através do UserDetailsService) quem é este utilizador e quais os seus níveis de acesso (Roles)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Cria uma credencial oficial de autenticação interna do Spring, anexando o perfil completo e os privilégios (Roles)
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // ESTABELECE A SESSÃO CLÍNICA:
            // Injeta a credencial diretamente no Contexto de Segurança Global do Spring.
            // A partir deste momento, as rotas bloqueadas por `@PreAuthorize` ou perfis são libertadas para este pedido.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 3. CONTINUIDADE DO PEDIDO:
        // Passa o controlo do pedido adiante na cadeia de filtros para que chegue com segurança ao Controller pretendido
        filterChain.doFilter(request, response);
    }
}