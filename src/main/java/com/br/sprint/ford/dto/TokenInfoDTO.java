package com.br.sprint.ford.dto;

import java.time.Instant;
import java.util.List;

public record TokenInfoDTO(String email, String nome, List<String> perfis,
                           Instant emitidoEm, Instant expiraEm) {}