package com.br.sprint.ford.service;

import com.br.sprint.ford.dto.VeiculoRequestDTO;
import com.br.sprint.ford.dto.VeiculoResponseDTO;
import com.br.sprint.ford.exception.RecursoDuplicadoException;
import com.br.sprint.ford.exception.ResourceNotFoundException;
import com.br.sprint.ford.model.Veiculo;
import com.br.sprint.ford.repository.VeiculoEquipamentoRepository;
import com.br.sprint.ford.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final VeiculoEquipamentoRepository veiculoEquipamentoRepository;

    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> listar(String marca, String modelo) {
        return veiculoRepository.findAll(Sort.by("marca", "modelo", "versao")).stream()
                .filter(v -> marca == null || v.getMarca().equalsIgnoreCase(marca))
                .filter(v -> modelo == null || v.getModelo().equalsIgnoreCase(modelo))
                .map(VeiculoResponseDTO::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeiculoResponseDTO buscarPorId(Long id) {
        return VeiculoResponseDTO.de(buscarEntidade(id));
    }

    @Transactional
    public VeiculoResponseDTO criar(VeiculoRequestDTO dto) {
        validarDuplicidade(dto, null);
        Veiculo veiculo = Veiculo.builder()
                .marca(dto.marca())
                .modelo(dto.modelo())
                .versao(dto.versao())
                .anoModelo(dto.anoModelo())
                .build();
        return VeiculoResponseDTO.de(veiculoRepository.save(veiculo));
    }

    @Transactional
    public VeiculoResponseDTO atualizar(Long id, VeiculoRequestDTO dto) {
        Veiculo veiculo = buscarEntidade(id);
        validarDuplicidade(dto, id);
        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setVersao(dto.versao());
        veiculo.setAnoModelo(dto.anoModelo());
        return VeiculoResponseDTO.de(veiculoRepository.save(veiculo));
    }

    @Transactional
    public void excluir(Long id) {
        Veiculo veiculo = buscarEntidade(id);
        veiculoEquipamentoRepository.removerPorVeiculo(veiculo.getId());
        veiculoRepository.delete(veiculo);
    }

    private Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: id " + id));
    }

    private void validarDuplicidade(VeiculoRequestDTO dto, Long idAtual) {
        veiculoRepository
                .findByMarcaIgnoreCaseAndModeloIgnoreCaseAndVersaoIgnoreCase(dto.marca(), dto.modelo(), dto.versao())
                .filter(existente -> !existente.getId().equals(idAtual))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException("Veículo já cadastrado: "
                            + dto.marca() + " " + dto.modelo() + " " + dto.versao());
                });
    }
}