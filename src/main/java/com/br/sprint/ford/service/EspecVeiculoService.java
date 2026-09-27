package com.br.sprint.ford.service;

import com.br.sprint.ford.dto.EspecRequestDTO;
import com.br.sprint.ford.dto.EspecResponseDTO;
import com.br.sprint.ford.dto.ItemEspecDTO;
import com.br.sprint.ford.exception.ResourceNotFoundException;
import com.br.sprint.ford.model.Veiculo;
import com.br.sprint.ford.repository.VeiculoEquipamentoRepository;
import com.br.sprint.ford.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EspecVeiculoService {

    public static final String NAO_DISPONIVEL = "NÃO DISPONÍVEL";

    private final VeiculoRepository veiculoRepository;
    private final VeiculoEquipamentoRepository veiculoEquipamentoRepository;

    public EspecResponseDTO obterEspecificacoes(EspecRequestDTO requisicao) {
        Veiculo veiculo = veiculoRepository
                .findByMarcaIgnoreCaseAndModeloIgnoreCaseAndVersaoIgnoreCase(
                        requisicao.getMarca(), requisicao.getModelo(), requisicao.getVersao())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veículo não encontrado: " + requisicao.getMarca() + " " +
                                requisicao.getModelo() + " " + requisicao.getVersao()));
        return montarFicha(veiculo, requisicao.getEquipamentos());
    }

    public EspecResponseDTO obterEspecificacoesPorVeiculo(Long veiculoId, List<String> equipamentos) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: id " + veiculoId));
        return montarFicha(veiculo, equipamentos);
    }

    private EspecResponseDTO montarFicha(Veiculo veiculo, List<String> equipamentos) {
        List<ItemEspecDTO> itens = equipamentos.stream()
                .map(String::trim)
                .map(nome -> montarItem(veiculo.getId(), nome))
                .toList();

        return EspecResponseDTO.builder()
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .versao(veiculo.getVersao())
                .especificacoes(itens)
                .build();
    }

    private ItemEspecDTO montarItem(Long veiculoId, String nomeEquipamento) {
        return veiculoEquipamentoRepository.buscarPorVeiculoENomeEquipamento(veiculoId, nomeEquipamento)
                .filter(ve -> Boolean.TRUE.equals(ve.getDisponivel()))
                .map(ve -> ItemEspecDTO.builder()
                        .equipamento(nomeEquipamento)
                        .valor(ve.getValor())
                        .disponivel(true)
                        .build())
                .orElseGet(() -> ItemEspecDTO.builder()
                        .equipamento(nomeEquipamento)
                        .valor(NAO_DISPONIVEL)
                        .disponivel(false)
                        .build());
    }
}