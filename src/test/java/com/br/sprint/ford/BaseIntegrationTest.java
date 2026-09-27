package com.br.sprint.ford;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    protected static final String ADMIN_EMAIL = "admin@ford.com";
    protected static final String ADMIN_SENHA = "admin123";
    protected static final String ANALISTA_EMAIL = "analista@ford.com";
    protected static final String ANALISTA_SENHA = "analista123";
    protected static final String VERSAO_XLT = "XLT 3.0L V6 AT 26MY";

    @Autowired
    protected MockMvc mockMvc;

    protected String obterToken(String email, String senha) throws Exception {
        String corpo = """
                {"email": "%s", "senha": "%s"}
                """.formatted(email, senha);

        String resposta = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        return JsonPath.read(resposta, "$.token");
    }

    protected String bearerAdmin() throws Exception {
        return "Bearer " + obterToken(ADMIN_EMAIL, ADMIN_SENHA);
    }

    protected String bearerAnalista() throws Exception {
        return "Bearer " + obterToken(ANALISTA_EMAIL, ANALISTA_SENHA);
    }
}