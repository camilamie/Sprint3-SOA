package com.br.sprint.ford.service;

import com.br.sprint.ford.dto.EquipamentoResponseDTO;
import com.br.sprint.ford.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    @Transactional(readOnly = true)
    public List<EquipamentoResponseDTO> listar(String categoria) {
        var equipamentos = (categoria == null || categoria.isBlank())
                ? equipamentoRepository.findAll(Sort.by("categoria", "nome"))
                : equipamentoRepository.findByCategoriaIgnoreCaseOrderByNomeAsc(categoria);
        return equipamentos.stream().map(EquipamentoResponseDTO::de).toList();
    }
}