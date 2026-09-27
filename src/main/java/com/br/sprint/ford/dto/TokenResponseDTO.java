package com.br.sprint.ford.dto;

import java.time.Instant;

public record TokenResponseDTO(String token, String tipo, Instant expiraEm, String perfil) {}