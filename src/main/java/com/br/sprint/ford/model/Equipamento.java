package com.br.sprint.ford.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipamento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String nome;

    @Column(length = 100)
    private String categoria;
}