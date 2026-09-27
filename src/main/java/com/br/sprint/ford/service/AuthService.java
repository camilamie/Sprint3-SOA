package com.br.sprint.ford.service;

import com.br.sprint.ford.dto.LoginRequestDTO;
import com.br.sprint.ford.dto.TokenResponseDTO;
import com.br.sprint.ford.exception.CredenciaisInvalidasException;
import com.br.sprint.ford.model.Usuario;
import com.br.sprint.ford.repository.UsuarioRepository;
import com.br.sprint.ford.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TokenResponseDTO login(LoginRequestDTO requisicao) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(requisicao.email())
                .filter(u -> passwordEncoder.matches(requisicao.senha(), u.getSenha()))
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos"));

        return tokenService.gerarToken(usuario);
    }
}