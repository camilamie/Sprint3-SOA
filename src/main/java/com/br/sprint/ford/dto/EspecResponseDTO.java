package com.br.sprint.ford.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EspecResponseDTO {
    private String marca;
    private String modelo;
    private String versao;
    private List<ItemEspecDTO> especificacoes;
}