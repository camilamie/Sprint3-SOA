package com.br.sprint.ford.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemEspecDTO {
    private String equipamento;
    private String valor;
    private boolean disponivel;
}