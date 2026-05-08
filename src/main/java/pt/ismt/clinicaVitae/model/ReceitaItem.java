package pt.ismt.clinicaVitae.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReceitaItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idReceitaItem")
public class ReceitaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceitaItem;

    @Column(nullable = false, length = 100)
    private String dosagem;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instrucoes;

    // Relacionamento com a Receita pai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReceita", nullable = false)
    private Receita receita;
}
