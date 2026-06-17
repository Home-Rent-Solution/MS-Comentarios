package com.HomeRentSolution.ms_comentarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosRequestDTO;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import com.HomeRentSolution.ms_comentarios.service.ComentariosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(ComentariosController.class) // Indica que se está probando el controlador de Comentarios
public class ComentariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComentariosService comentariosService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ComentariosResponseDTO comentarioResponse;
    private ComentariosRequestDTO comentarioRequest;

    @BeforeEach
    void setUp() {
        // Configura los objetos DTO de ejemplo antes de cada prueba
        comentarioResponse = new ComentariosResponseDTO(
                1L,
                10,
                "Excelente lugar, muy limpio y buena ubicación.",
                10L,
                5L
        );

        comentarioRequest = new ComentariosRequestDTO(
                10,
                "Excelente lugar, muy limpio y buena ubicación.",
                5L,
                5L
        );
    }

    // TESTS CRUD

    @Test
    public void testGetComentarios() throws Exception {
        when(comentariosService.mostrarComentarios()).thenReturn(List.of(comentarioResponse));
        mockMvc.perform(get("/api/v1/comentarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idComentario").value(1))
                .andExpect(jsonPath("$[0].idPropiedad").value(10))
                .andExpect(jsonPath("$[0].idInquilino").value(5))
                .andExpect(jsonPath("$[0].puntuacion").value(10))
                .andExpect(jsonPath("$[0].comentario")
                        .value("Excelente lugar, muy limpio y buena ubicación."));
    }

    @Test
    public void testGetPorId() throws Exception {
        when(comentariosService.mostrarPorId(1L)).thenReturn(comentarioResponse);
        mockMvc.perform(get("/api/v1/comentarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComentario").value(1))
                .andExpect(jsonPath("$.comentario")
                        .value("Excelente lugar, muy limpio y buena ubicación."))
                .andExpect(jsonPath("$.puntuacion").value(10));
    }

    @Test
    public void testPostComentario() throws Exception {
        when(comentariosService.save(any(ComentariosRequestDTO.class))).thenReturn(comentarioResponse);
        mockMvc.perform(post("/api/v1/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idComentario").value(1))
                .andExpect(jsonPath("$.comentario")
                        .value("Excelente lugar, muy limpio y buena ubicación."));
    }

    @Test
    public void testPutComentario() throws Exception {
        when(comentariosService.editar(eq(1L), any(ComentariosRequestDTO.class))).thenReturn(comentarioResponse);
        mockMvc.perform(put("/api/v1/comentarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComentario").value(1))
                .andExpect(jsonPath("$.comentario")
                        .value("Excelente lugar, muy limpio y buena ubicación."));
    }

    @Test
    public void testDeleteComentario() throws Exception {
        doNothing().when(comentariosService).borrar(1L);
        mockMvc.perform(delete("/api/v1/comentarios/1"))
                .andExpect(status().isNoContent());
        verify(comentariosService, times(1)).borrar(1L);
    }
}
