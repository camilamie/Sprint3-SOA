package com.br.sprint.ford.dto;

import com.br.sprint.ford.model.Veiculo;

public record VeiculoResponseDTO(Long id, String marca, String modelo, String versao, String anoModelo) {

    public static VeiculoResponseDTO de(Veiculo v) {
        return new VeiculoResponseDTO(v.getId(), v.getMarca(), v.getModelo(), v.getVersao(), v.getAnoModelo());
    }
}