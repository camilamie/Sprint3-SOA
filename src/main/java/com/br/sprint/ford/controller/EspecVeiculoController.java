package com.br.sprint.ford.controller;

import com.br.sprint.ford.dto.EspecRequestDTO;
import com.br.sprint.ford.dto.EspecResponseDTO;
import com.br.sprint.ford.service.EspecVeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/especificacoes")
@RequiredArgsConstructor
@Tag(name = "Especificações de Veículos")
public class EspecVeiculoController {

    private final EspecVeiculoService servico;


    @Operation(summary = "Consulta a ficha técnica padronizada por marca, modelo, versão e lista de equipamentos")
    @PostMapping("/consulta")
    public ResponseEntity<EspecResponseDTO> consultar(@Valid @RequestBody EspecRequestDTO requisicao) {
        return ResponseEntity.ok(servico.obterEspecificacoes(requisicao));
    }
}