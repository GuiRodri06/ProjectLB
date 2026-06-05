package pt.ismt.clinicaVitae.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component // Regista a classe como uma engrenagem/componente gerido pelo Spring IoC
public class JwtTokenProvider {

    // Chave secreta de encriptação usada para assinar digitalmente o token e evitar adulterações (Deve ir para variáveis de ambiente em produção)
    private String jwtSecret = "SegredoSuperSecretoParaEstudos123";

    // Tempo de vida estrito do token fixado em 24 horas expressas em milissegundos
    private int jwtExpirationMs = 86400000;

    /**
     * GERAÇÃO DO TOKEN:
     * Constrói a assinatura JWT encriptada contendo o e-mail (Subject) do utilizador.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o e-mail do utilizador como o corpo/proprietário do token
                .setIssuedAt(new Date()) // Regista o carimbo de data/hora exato da criação
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Calcula o momento exato da expiração
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Aplica o algoritmo de hash HS512 usando a nossa assinatura secreta
                .compact(); // Compacta e transforma tudo numa String encadeada de três partes separadas por pontos
    }

    /**
     * VALIDAÇÃO DO TOKEN:
     * Descodifica o token e verifica se a assinatura é legítima e se o prazo de validade não expirou.
     */
    public boolean validateToken(String token) {
        try {
            // Se o parser conseguir ler a chave secreta e processar o token sem disparar erros, o token é idóneo
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Captura qualquer anomalia (Token expirado, corrompido, assinatura falsa ou nulo) e nega o acesso
            return false;
        }
    }

    /**
     * EXTRAÇÃO DE DADOS:
     * Abre o token validado para ler e extrair o e-mail (username) que está guardado no seu interior.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Devolve o e-mail mapeado no "Subject"
    }
}