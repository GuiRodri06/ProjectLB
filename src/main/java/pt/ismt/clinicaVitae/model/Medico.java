package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idMedico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedico;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 150)
    private String especialidade;

    @Column(nullable = false, length = 9)
    private String telemovel;
}
