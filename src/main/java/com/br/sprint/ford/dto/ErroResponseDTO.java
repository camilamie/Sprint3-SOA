package com.br.sprint.ford.dto;

import java.time.Instant;
import java.util.List;

public record ErroResponseDTO(Instant timestamp, int status, String erro,
                              String mensagem, String caminho, List<String> detalhes) {}