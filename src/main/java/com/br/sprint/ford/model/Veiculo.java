package com.br.sprint.ford.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 100)
    private String versao;

    @Column(name = "ano_modelo", length = 20)
    private String anoModelo;
}