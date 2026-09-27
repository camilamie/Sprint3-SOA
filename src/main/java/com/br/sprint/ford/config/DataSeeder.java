package com.br.sprint.ford.config;

import com.br.sprint.ford.model.Perfil;
import com.br.sprint.ford.model.Usuario;
import com.br.sprint.ford.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        criarSeNaoExistir("Administrador", "admin@ford.com", "admin123", Perfil.ADMIN);
        criarSeNaoExistir("Analista", "analista@ford.com", "analista123", Perfil.ANALISTA);
    }

    private void criarSeNaoExistir(String nome, String email, String senha, Perfil perfil) {
        if (usuarioRepository.existsByEmailIgnoreCase(email)) return;
        usuarioRepository.save(Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .perfil(perfil)
                .build());
        log.info(">>> Usuário inicial criado: {} ({})", email, perfil);
    }
}