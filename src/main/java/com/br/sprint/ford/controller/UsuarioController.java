package com.br.sprint.ford.controller;

import com.br.sprint.ford.dto.TokenInfoDTO;
import com.br.sprint.ford.dto.UsuarioResponseDTO;
import com.br.sprint.ford.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Lista todos os usuários (somente ADMIN)")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Retorna os dados do usuário logado, lidos do próprio JWT")
    @GetMapping("/me")
    public ResponseEntity<TokenInfoDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new TokenInfoDTO(
                jwt.getSubject(),
                jwt.getClaimAsString("nome"),
                jwt.getClaimAsStringList("roles"),
                jwt.getIssuedAt(),
                jwt.getExpiresAt()));
    }
}