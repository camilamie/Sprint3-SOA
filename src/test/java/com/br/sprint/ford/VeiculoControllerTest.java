package com.br.sprint.ford;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VeiculoControllerTest extends BaseIntegrationTest {

    @Test
    void semTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/v1/veiculos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void tokenInvalidoRetorna401() throws Exception {
        mockMvc.perform(get("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token.invalido.aqui"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void analistaPodeListarVeiculos() throws Exception {
        mockMvc.perform(get("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void analistaNaoPodeCriarVeiculoRetorna403() throws Exception {
        mockMvc.perform(post("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Toyota\",\"modelo\":\"Hilux\",\"versao\":\"GR-S\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void analistaNaoPodeListarUsuariosRetorna403() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista()))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminFazCicloCompletoCriarAtualizarExcluir() throws Exception {
        String admin = bearerAdmin();
        String versao = "Teste " + System.nanoTime();

        String resposta = mockMvc.perform(post("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Chevrolet\",\"modelo\":\"S10\",\"versao\":\"" + versao + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Number id = JsonPath.read(resposta, "$.id");

        mockMvc.perform(put("/api/v1/veiculos/" + id)
                        .header(HttpHeaders.AUTHORIZATION, admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Chevrolet\",\"modelo\":\"S10\",\"versao\":\"" + versao + "\",\"anoModelo\":\"2026\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anoModelo").value("2026"));

        mockMvc.perform(delete("/api/v1/veiculos/" + id)
                        .header(HttpHeaders.AUTHORIZATION, admin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/veiculos/" + id)
                        .header(HttpHeaders.AUTHORIZATION, admin))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarVeiculoDuplicadoRetorna409() throws Exception {
        mockMvc.perform(post("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, bearerAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Ford\",\"modelo\":\"Ranger\",\"versao\":\"" + VERSAO_XLT + "\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void buscarVeiculoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/v1/veiculos/999999")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}