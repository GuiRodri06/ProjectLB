package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;
import pt.ismt.clinicaVitae.model.enums.EstadoConsultaEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idConsulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsulta;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoConsultaEnum estadoConsultaEnum;

    private String receita; // Referência textual conforme seu SQL

    @Column(columnDefinition = "TEXT")
    private String notasMedico;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false)
    private LocalDate dia;

    // --- Relacionamentos (O Coração da Entidade) ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPaciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMedico", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRecepcionista", nullable = false)
    private Recepcionista recepcionista;
}