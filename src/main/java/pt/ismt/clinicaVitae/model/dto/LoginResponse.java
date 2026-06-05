package pt.ismt.clinicaVitae.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Objeto de Resposta (DTO) concebido para devolver os metadados da sessão após uma autenticação bem-sucedida
public class LoginResponse {

    // O token criptográfico (JWT) gerado, contendo a identidade e permissões do utilizador
    private String accessToken;

    // Define o padrão do esquema de autorização HTTP (por omissão, "Bearer")
    private String tokenType = "Bearer";

    // Construtor de conveniência que permite inicializar o DTO passando apenas a string do token
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}