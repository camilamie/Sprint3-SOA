package com.br.sprint.ford.dto;

import jakarta.validation.constraints.NotBlank;

public record VeiculoRequestDTO(
        @NotBlank(message = "A marca é obrigatória") String marca,
        @NotBlank(message = "O modelo é obrigatório") String modelo,
        @NotBlank(message = "A versão é obrigatória") String versao,
        String anoModelo
) {}