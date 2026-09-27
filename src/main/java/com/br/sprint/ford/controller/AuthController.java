package com.br.sprint.ford.controller;

import com.br.sprint.ford.dto.LoginRequestDTO;
import com.br.sprint.ford.dto.RegistroRequestDTO;
import com.br.sprint.ford.dto.TokenResponseDTO;
import com.br.sprint.ford.dto.UsuarioResponseDTO;
import com.br.sprint.ford.service.AuthService;
import com.br.sprint.ford.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints públicos de login e cadastro")
@SecurityRequirements
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Autentica e devolve um JWT")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO requisicao) {
        return ResponseEntity.ok(authService.login(requisicao));
    }

    @Operation(summary = "Cadastra um novo usuário com perfil ANALISTA")
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(requisicao));
    }
}