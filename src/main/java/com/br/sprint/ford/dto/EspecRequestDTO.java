package com.br.sprint.ford.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EspecRequestDTO {

    @NotBlank(message = "A marca é obrigatória")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "A versão é obrigatória")
    private String versao;

    @NotEmpty(message = "A lista de equipamentos não pode estar vazia")
    private List<String> equipamentos;
}