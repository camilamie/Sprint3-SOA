package com.br.sprint.ford.repository;

import com.br.sprint.ford.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Optional<Equipamento> findByNomeIgnoreCase(String nome);

    List<Equipamento> findByCategoriaIgnoreCaseOrderByNomeAsc(String categoria);
}