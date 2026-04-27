package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Recepcionista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idRecepcionista")
public class Recepcionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecepcionista;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 9)
    private String telemovel;

    @Column(nullable = false)
    private String password;
}