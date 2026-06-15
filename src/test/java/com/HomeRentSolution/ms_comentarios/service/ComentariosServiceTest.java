package com.HomeRentSolution.ms_comentarios.service;

import com.HomeRentSolution.ms_comentarios.client.InquilinoClient;
import com.HomeRentSolution.ms_comentarios.client.PropiedadClient;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosRequestDTO;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import com.HomeRentSolution.ms_comentarios.model.Comentarios;
import com.HomeRentSolution.ms_comentarios.repository.ComentariosRepository;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ComentariosServiceTest {

    @Autowired
    private ComentariosService comentariosService;

    @MockitoBean
    private ComentariosRepository comentariosRepository;

    @MockitoBean
    private InquilinoClient inquilinoClient;

    @MockitoBean
    private PropiedadClient propiedadClient;

    // TESTS CRUD

    @Test
    public void testMostrarComentarios() {
        Comentarios comentario = new Comentarios(
                1L,
                5,
                "Excelente lugar, muy limpio",
                10L,
                2L
        );
        when(comentariosRepository.findAll()).thenReturn(List.of(comentario));
        List<ComentariosResponseDTO> resultado = comentariosService.mostrarComentarios();
        assertNotNull(resultado);
        assertEquals(
                1,
                resultado.size()
        );
        assertEquals(
                "Excelente lugar, muy limpio",
                resultado.get(0).getComentario()
        );
    }

    @Test
    public void testMostrarPorId_Success() {
        Comentarios comentario = new Comentarios(
                1L,
                4,
                "Buena ubicación",
                10L,
                2L
        );
        when(comentariosRepository.findById(1L)).thenReturn(Optional.of(comentario));
        ComentariosResponseDTO resultado = comentariosService.mostrarPorId(1L);
        assertNotNull(resultado);
        assertEquals(
                4,
                resultado.getPuntuacion()
        );
        assertEquals(
                "Buena ubicación",
                resultado.getComentario()
        );
    }

    @Test
    public void testMostrarPorId_NotFound() {
        when(comentariosRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentariosService.mostrarPorId(99L);
        });
        assertEquals(
                "El comentario con ID: 99 no existe",
                exception.getMessage()
        );
    }

    @Test
    public void testSave_Success() {
        ComentariosRequestDTO request = new ComentariosRequestDTO(
                5,
                "Increíble todo",
                10L,
                2L
        );
        Comentarios comentarioGuardado = new Comentarios(
                1L,
                5,
                "Increíble todo",
                10L,
                2L
        );
        // Simulamos que el inquilino está habilitado y la propiedad existe
        when(inquilinoClient.validarInquilino(2L)).thenReturn(true);
        doNothing().when(propiedadClient).validarPropiedad(10L);
        when(comentariosRepository.save(any(Comentarios.class))).thenReturn(comentarioGuardado);
        ComentariosResponseDTO resultado = comentariosService.save(request);
        assertNotNull(resultado);
        assertEquals(
                1L,
                resultado.getIdComentario()
        );
        assertEquals(
                5,
                resultado.getPuntuacion()
        );
    }

    @Test
    public void testSave_InquilinoBloqueado() {
        ComentariosRequestDTO request = new ComentariosRequestDTO(
                5,
                "Increíble todo",
                10L,
                2L
        );
        // El cliente Feign responde que el inquilino no está habilitado (false)
        when(inquilinoClient.validarInquilino(2L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentariosService.save(request);
        });
        assertEquals(
                "El inquilino con ID: 2 esta bloqueado",
                exception.getMessage()
        );
        verify(
                comentariosRepository,
                never()).save(any()
        );
    }

    @Test
    public void testSave_InquilinoNotFound() {
        ComentariosRequestDTO request = new ComentariosRequestDTO(
                5,
                "Increíble todo",
                10L,
                2L
        );

        // Simulamos un error 404 del microservicio de inquilinos
        Request feignRequest = Request.create(
                Request.HttpMethod.GET,
                "/validar",
                Map.of(),
                null,
                null,
                null
        );
        FeignException.NotFound feignException = new FeignException.NotFound(
                "Not Found",
                feignRequest,
                null,
                null
        );
        when(inquilinoClient.validarInquilino(2L)).thenThrow(feignException);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentariosService.save(request);
        });
        assertTrue(exception.getMessage().contains("no existe en ms-inquilinos"));
    }

    @Test
    public void testSave_PropiedadNotFound() {
        ComentariosRequestDTO request = new ComentariosRequestDTO(
                5,
                "Increíble todo",
                10L,
                2L
        );

        when(inquilinoClient.validarInquilino(2L)).thenReturn(true);
        // Simulamos un error 404 del microservicio de propiedades
        Request feignRequest = Request.create(
                Request.HttpMethod.GET,
                "/validar",
                Map.of(),
                null,
                null,
                null
        );
        FeignException.NotFound feignException = new FeignException.NotFound(
                "Not Found",
                feignRequest,
                null,
                null
        );
        doThrow(feignException).when(propiedadClient).validarPropiedad(10L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentariosService.save(request);
        });
        assertTrue(exception.getMessage().contains("no existe en ms-propiedades"));
    }

    @Test
    public void testEditar_Success() {
        Comentarios comentarioExistente = new Comentarios(
                1L,
                3,
                "Malo",
                10L,
                2L
        );
        ComentariosRequestDTO nuevosDatos = new ComentariosRequestDTO(
                5,
                "Cambié de opinión, excelente",
                10L,
                2L
        );

        when(comentariosRepository.findById(1L)).thenReturn(Optional.of(comentarioExistente));
        when(inquilinoClient.validarInquilino(2L)).thenReturn(true);
        doNothing().when(propiedadClient).validarPropiedad(10L);
        when(comentariosRepository.save(any(Comentarios.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ComentariosResponseDTO resultado = comentariosService.editar(
                1L,
                nuevosDatos
        );
        assertNotNull(resultado);
        assertEquals(
                5,
                resultado.getPuntuacion()
        );
        assertEquals(
                "Cambié de opinión, excelente",
                resultado.getComentario()
        );
    }

    @Test
    public void testBorrar_Success() {
        when(comentariosRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> comentariosService.borrar(1L));
        verify(comentariosRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBorrar_NotFound() {
        when(comentariosRepository.existsById(99L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentariosService.borrar(99L);
        });
        assertEquals(
                "El comentario con ID: 99 no existe",
                exception.getMessage()
        );
    }
}
