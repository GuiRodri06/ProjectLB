package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Receita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idReceita")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceita;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacoes;

    // Relacionamento com a Consulta (1 para 1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idConsulta", nullable = false)
    private Consulta consulta;

    // Uma receita tem vários itens
    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaItem> itens = new ArrayList<>();
}