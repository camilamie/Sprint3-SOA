package com.br.sprint.ford.service;

import com.br.sprint.ford.model.EquipVeiculo;
import com.br.sprint.ford.model.Equipamento;
import com.br.sprint.ford.model.Veiculo;
import com.br.sprint.ford.repository.EquipamentoRepository;
import com.br.sprint.ford.repository.VeiculoEquipamentoRepository;
import com.br.sprint.ford.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelDataLoaderService implements ApplicationRunner {

    private static final String MARCA = "Ford";
    private static final String MODELO = "Ranger";
    private static final String ANO_MODELO = "2026";

    private static final Set<String> CATEGORIAS = Set.of(
            "Engine & Transmission", "Wheels", "Connectivity", "Ice Line Up",
            "Air conditioning", "Safety", "High tech", "Global Closing",
            "Trim", "SunRoof", "Seats", "Lights", "4X4", "Others"
    );

    private final VeiculoRepository veiculoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final VeiculoEquipamentoRepository veiculoEquipamentoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            carregarDadosNaInicializacao();
        } catch (Exception e) {
            log.error(">>> ERRO ao carregar o Excel: {}", e.getMessage(), e);
        }
    }

    public void carregarDadosNaInicializacao() throws Exception {
        if (veiculoRepository.count() > 0 && equipamentoRepository.count() > 0
                && veiculoEquipamentoRepository.count() > 0) {
            log.info(">>> Banco já populado. Pulando carga do Excel (apague a pasta data/ para recarregar).");
            return;
        }

        veiculoEquipamentoRepository.deleteAllInBatch();
        equipamentoRepository.deleteAllInBatch();
        veiculoRepository.deleteAllInBatch();

        ClassPathResource recurso = new ClassPathResource("data/ford-data.xlsx");
        if (!recurso.exists()) {
            log.error(">>> Arquivo data/ford-data.xlsx não encontrado em src/main/resources/data/");
            return;
        }

        try (InputStream entrada = recurso.getInputStream();
             Workbook planilha = new XSSFWorkbook(entrada)) {

            Sheet aba = planilha.getSheet("BASE");
            if (aba == null) {
                log.error(">>> Aba 'BASE' não encontrada no Excel");
                return;
            }

            // Linha 0: cada coluna a partir da B é uma versão (XLT, Limited, Limited+, Raptor...)
            Row cabecalho = aba.getRow(0);
            List<Veiculo> veiculos = new ArrayList<>();
            List<Integer> colunas = new ArrayList<>();

            for (int coluna = 1; coluna < cabecalho.getLastCellNum(); coluna++) {
                String versao = obterValorCelula(cabecalho.getCell(coluna));
                if (estaVazio(versao)) continue;

                Veiculo salvo = veiculoRepository.save(Veiculo.builder()
                        .marca(MARCA).modelo(MODELO).versao(versao).anoModelo(ANO_MODELO)
                        .build());
                veiculos.add(salvo);
                colunas.add(coluna);
                log.info(">>> Veículo carregado: {} {} {}", salvo.getMarca(), salvo.getModelo(), salvo.getVersao());
            }

            String categoriaAtual = "Geral";
            int contador = 0;

            for (int idxLinha = 2; idxLinha <= aba.getLastRowNum(); idxLinha++) {
                Row linha = aba.getRow(idxLinha);
                if (linha == null) continue;

                String nomeEquipamento = obterValorCelula(linha.getCell(0));
                if (estaVazio(nomeEquipamento)) continue;

                // Linha de categoria (ex.: "Safety") -> só troca a categoria atual
                if (CATEGORIAS.contains(nomeEquipamento)) {
                    categoriaAtual = nomeEquipamento;
                    continue;
                }

                final String categoria = categoriaAtual;
                Equipamento equipamento = equipamentoRepository.findByNomeIgnoreCase(nomeEquipamento)
                        .orElseGet(() -> equipamentoRepository.save(
                                Equipamento.builder().nome(nomeEquipamento).categoria(categoria).build()));

                for (int i = 0; i < veiculos.size(); i++) {
                    String valor = obterValorCelula(linha.getCell(colunas.get(i)));
                    boolean disponivel = !estaVazio(valor) && !valor.equals("0") && !valor.equals("0.0");

                    veiculoEquipamentoRepository.save(EquipVeiculo.builder()
                            .veiculo(veiculos.get(i))
                            .equipamento(equipamento)
                            .valor(disponivel ? valor : null)
                            .disponivel(disponivel)
                            .build());
                }
                contador++;
            }

            log.info(">>> CARGA CONCLUÍDA: {} veículos, {} equipamentos.", veiculos.size(), contador);
        }
    }

    private boolean estaVazio(String s) {
        return s == null || s.isBlank();
    }

    private String obterValorCelula(Cell celula) {
        if (celula == null) return null;
        return switch (celula.getCellType()) {
            case STRING -> celula.getStringCellValue().trim();
            case NUMERIC -> {
                double numero = celula.getNumericCellValue();
                if (numero == Math.floor(numero)) yield String.valueOf((long) numero);
                else yield String.valueOf(numero);
            }
            case BOOLEAN -> String.valueOf(celula.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield celula.getStringCellValue().trim();
                } catch (Exception e) {
                    yield String.valueOf(celula.getNumericCellValue());
                }
            }
            default -> null;
        };
    }
}