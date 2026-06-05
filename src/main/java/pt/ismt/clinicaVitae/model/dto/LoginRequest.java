package pt.ismt.clinicaVitae.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Objeto de Transferência de Dados (DTO) que encapsula as credenciais enviadas no corpo do pedido HTTP durante o login
public class LoginRequest {

    // O e-mail institucional que serve como identificador único do utilizador (Médico ou Recepcionista)
    private String email;

    // A palavra-passe (senha) em texto limpo enviada pelo ecrã para ser validada pelo AuthenticationManager
    private String password;
}