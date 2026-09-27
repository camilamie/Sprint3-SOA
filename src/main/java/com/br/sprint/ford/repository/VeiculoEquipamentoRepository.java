package com.br.sprint.ford.repository;

import com.br.sprint.ford.model.EquipVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoEquipamentoRepository extends JpaRepository<EquipVeiculo, Long> {

    @Query("SELECT ve FROM EquipVeiculo ve " +
            "WHERE ve.veiculo.id = :veiculoId " +
            "AND LOWER(ve.equipamento.nome) = LOWER(:equipamentoNome)")
    Optional<EquipVeiculo> buscarPorVeiculoENomeEquipamento(
            @Param("veiculoId") Long veiculoId,
            @Param("equipamentoNome") String equipamentoNome);

    @Modifying
    @Query("DELETE FROM EquipVeiculo ve WHERE ve.veiculo.id = :veiculoId")
    void removerPorVeiculo(@Param("veiculoId") Long veiculoId);
}