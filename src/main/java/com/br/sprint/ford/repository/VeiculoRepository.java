package com.br.sprint.ford.repository;

import com.br.sprint.ford.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByMarcaIgnoreCaseAndModeloIgnoreCaseAndVersaoIgnoreCase(
            String marca, String modelo, String versao);
}