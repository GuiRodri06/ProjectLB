package pt.ismt.clinicaVitae.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ismt.clinicaVitae.security.JwtTokenProvider;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager; // Gerente nativo do Spring Security que valida as credenciais

    @Autowired
    private JwtTokenProvider tokenProvider; // Utilitário que gera o token encriptado baseado no utilizador

    // Rota raiz da aplicação: Apresenta a página inicial de login
    @GetMapping("/")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // Processamento do Pedido de Autenticação (POST)
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response) {

        try {
            // 1. Dispara a verificação no banco de dados (chama indiretamente o nosso UserDetailsServiceImpl)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // 2. Se as credenciais estiverem corretas, gera uma assinatura digital (Token JWT)
            String token = tokenProvider.generateToken(authentication.getName());

            // 3. SEGURANÇA AVANÇADA: Envelopa o token JWT num Cookie HTTP-Only.
            // Impede ataques de roubo de sessão via JavaScript malicioso (XSS).
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true); // Bloqueia leitura via document.cookie no front-end
            cookie.setPath("/");       // Disponível em todo o ecossistema do site
            cookie.setMaxAge(86400);   // Validade estrita de 24 horas
            response.addCookie(cookie); // Anexa o cookie à resposta HTTP enviada ao navegador

            // 4. Redirecionamento Inteligente de Perfil (Role-Based Routing)
            boolean isMedico = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_MEDICO"));

            if (isMedico) {
                return "redirect:/medicos/dashboard"; // Rota exclusiva médica
            } else {
                return "redirect:/recepcionistas/dashboard"; // Rota exclusiva da receção
            }

        } catch (Exception e) {
            // Se falhar (e-mail errado ou senha inválida), devolve à tela de login com parâmetro de erro
            return "redirect:/?erro=credenciais_invalidas";
        }
    }
}