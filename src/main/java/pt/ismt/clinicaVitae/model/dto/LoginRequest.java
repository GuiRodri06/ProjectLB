package pt.ismt.clinicaVitae.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email; // Pode ser o email ou código do médico/paciente
    private String password;

    // Getters e Setters
}