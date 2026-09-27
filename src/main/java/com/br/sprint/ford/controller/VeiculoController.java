package com.br.sprint.ford.controller;

import com.br.sprint.ford.dto.EspecResponseDTO;
import com.br.sprint.ford.dto.VeiculoRequestDTO;
import com.br.sprint.ford.dto.VeiculoResponseDTO;
import com.br.sprint.ford.service.EspecVeiculoService;
import com.br.sprint.ford.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos")
public class VeiculoController {

    private final VeiculoService veiculoService;
    private final EspecVeiculoService especVeiculoService;

    @Operation(summary = "Lista veículos (filtros opcionais por marca e modelo)")
    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> listar(
            @RequestParam(name = "marca", required = false) String marca,
            @RequestParam(name = "modelo", required = false) String modelo) {
        return ResponseEntity.ok(veiculoService.listar(marca, modelo));
    }

    @Operation(summary = "Busca um veículo pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id));
    }

    @Operation(summary = "Cadastra um veículo (somente ADMIN)")
    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> criar(@Valid @RequestBody VeiculoRequestDTO dto) {
        VeiculoResponseDTO criado = veiculoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @Operation(summary = "Atualiza um veículo (somente ADMIN)")
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizar(@PathVariable("id") Long id,
                                                        @Valid @RequestBody VeiculoRequestDTO dto) {
        return ResponseEntity.ok(veiculoService.atualizar(id, dto));
    }

    @Operation(summary = "Remove um veículo e suas especificações (somente ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        veiculoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ficha técnica padronizada do veículo para os equipamentos informados",
            description = "Ex.: /api/v1/veiculos/1/especificacoes?equipamentos=Potência&equipamentos=Torque")
    @GetMapping("/{id}/especificacoes")
    public ResponseEntity<EspecResponseDTO> especificacoes(
            @PathVariable("id") Long id,
            @RequestParam(name = "equipamentos") List<String> equipamentos) {
        return ResponseEntity.ok(especVeiculoService.obterEspecificacoesPorVeiculo(id, equipamentos));
    }
}