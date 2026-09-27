package com.br.sprint.ford.dto;

import com.br.sprint.ford.model.Usuario;

public record UsuarioResponseDTO(Long id, String nome, String email, String perfil) {

    public static UsuarioResponseDTO de(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getPerfil().name());
    }
}