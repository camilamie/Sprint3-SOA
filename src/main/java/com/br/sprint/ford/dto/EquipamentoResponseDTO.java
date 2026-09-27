package com.br.sprint.ford.dto;

import com.br.sprint.ford.model.Equipamento;

public record EquipamentoResponseDTO(Long id, String nome, String categoria) {

    public static EquipamentoResponseDTO de(Equipamento e) {
        return new EquipamentoResponseDTO(e.getId(), e.getNome(), e.getCategoria());
    }
}