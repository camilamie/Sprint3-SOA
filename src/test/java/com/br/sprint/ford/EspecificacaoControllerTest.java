package com.br.sprint.ford;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EspecificacaoControllerTest extends BaseIntegrationTest {

    private static final String CONSULTA_XLT = """
            {
              "marca": "Ford",
              "modelo": "Ranger",
              "versao": "XLT 3.0L V6 AT 26MY",
              "equipamentos": ["Potência", "Torque", "Equipamento Que Nao Existe"]
            }
            """;

    @Test
    void consultaRetornaFichaPadronizadaNaMesmaOrdemDoPedido() throws Exception {
        mockMvc.perform(post("/api/v1/especificacoes/consulta")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONSULTA_XLT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versao").value(VERSAO_XLT))
                .andExpect(jsonPath("$.especificacoes", hasSize(3)))
                .andExpect(jsonPath("$.especificacoes[0].valor").value("250"))
                .andExpect(jsonPath("$.especificacoes[0].disponivel").value(true))
                .andExpect(jsonPath("$.especificacoes[1].valor").value("600"))
                // item inexistente continua na lista, marcado explicitamente como indisponível
                .andExpect(jsonPath("$.especificacoes[2].disponivel").value(false));
    }

    @Test
    void consultaDeVeiculoInexistenteRetorna404() throws Exception {
        mockMvc.perform(post("/api/v1/especificacoes/consulta")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Ford\",\"modelo\":\"Ranger\",\"versao\":\"Nao Existe\",\"equipamentos\":[\"Torque\"]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void consultaSemEquipamentosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/especificacoes/consulta")
                        .header(HttpHeaders.AUTHORIZATION, bearerAnalista())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Ford\",\"modelo\":\"Ranger\",\"versao\":\"XLT 3.0L V6 AT 26MY\",\"equipamentos\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isNotEmpty());
    }

    @Test
    void consultaSemTokenRetorna401() throws Exception {
        mockMvc.perform(post("/api/v1/especificacoes/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONSULTA_XLT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void subRecursoDeEspecificacoesPorIdDoVeiculo() throws Exception {
        String token = bearerAnalista();

        String lista = mockMvc.perform(get("/api/v1/veiculos")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<Number> ids = JsonPath.read(lista, "$[?(@.versao == '" + VERSAO_XLT + "')].id");

        mockMvc.perform(get("/api/v1/veiculos/" + ids.get(0) + "/especificacoes")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("equipamentos", "Potência", "Torque"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especificacoes", hasSize(2)))
                .andExpect(jsonPath("$.especificacoes[0].valor").value("250"));
    }
}