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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // Rota pública para exibir o formulário de login
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response) {

        try {
            // 1. Autentica o usuário no banco de dados (via UserDetailsService)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // 2. Gera o token JWT após sucesso na autenticação
            String token = tokenProvider.generateToken(authentication.getName());

            // 3. Salva o token em um cookie HttpOnly (Seguro contra XSS)
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400); // 24 horas
            response.addCookie(cookie);

            // 4. Redirecionamento inteligente baseado na "Role" (Autoridade)
            boolean isMedico = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_MEDICO"));

            if (isMedico) {
                return "redirect:/medicos/dashboard";
            } else {
                return "redirect:/recepcionistas/dashboard";
            }

        } catch (Exception e) {
            // Em caso de falha (senha incorreta ou usuário inexistente)
            return "redirect:/?erro=credenciais_invalidas";
        }
    }
}