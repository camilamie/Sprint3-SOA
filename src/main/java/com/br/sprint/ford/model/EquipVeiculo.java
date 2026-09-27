package com.br.sprint.ford.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculo_equipamento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EquipVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column(name = "equipamento_valor", length = 255)
    private String valor;

    @Column(nullable = false)
    private Boolean disponivel;
}