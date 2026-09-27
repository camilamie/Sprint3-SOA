package com.br.sprint.ford.service;

import com.br.sprint.ford.dto.RegistroRequestDTO;
import com.br.sprint.ford.dto.UsuarioResponseDTO;
import com.br.sprint.ford.exception.RecursoDuplicadoException;
import com.br.sprint.ford.model.Perfil;
import com.br.sprint.ford.model.Usuario;
import com.br.sprint.ford.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new RecursoDuplicadoException("Já existe um usuário com o e-mail " + dto.email());
        }
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email().toLowerCase())
                .senha(passwordEncoder.encode(dto.senha()))
                .perfil(Perfil.ANALISTA)
                .build();
        return UsuarioResponseDTO.de(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::de).toList();
    }
}