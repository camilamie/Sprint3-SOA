package com.br.sprint.ford.controller;

import com.br.sprint.ford.dto.EquipamentoResponseDTO;
import com.br.sprint.ford.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipamentos")
@RequiredArgsConstructor
@Tag(name = "Equipamentos", description = "Catálogo de atributos técnicos pesquisáveis")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @Operation(summary = "Lista os equipamentos/atributos disponíveis (filtro opcional por categoria)")
    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> listar(
            @RequestParam(name = "categoria", required = false) String categoria) {
        return ResponseEntity.ok(equipamentoService.listar(categoria));
    }
}