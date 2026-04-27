package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;
import pt.ismt.clinicaVitae.model.enums.GeneroEnum;

import java.time.LocalDate;

@Entity
@Table(name = "Paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idPaciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 9)
    private String cartaoCidadao;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    @Column(nullable = false)
    private Double altura;

    @Column(nullable = false)
    private String morada;

    @Column(nullable = false, length = 100)
    private String nacionalidade;

    @Column(nullable = false, length = 9)
    private String telemovel;

    @Column(nullable = false, unique = true)
    private String email;
}
