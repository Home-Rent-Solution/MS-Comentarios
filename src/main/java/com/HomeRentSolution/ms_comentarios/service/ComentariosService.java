package com.HomeRentSolution.ms_comentarios.service;

import com.HomeRentSolution.ms_comentarios.client.InquilinoClient;
import com.HomeRentSolution.ms_comentarios.client.PropiedadClient;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosRequestDTO;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import com.HomeRentSolution.ms_comentarios.model.Comentarios;
import com.HomeRentSolution.ms_comentarios.repository.ComentariosRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComentariosService {

    private final ComentariosRepository comentariosRepository;
    private final InquilinoClient inquilinoClient;
    private final PropiedadClient propiedadClient;

    private ComentariosResponseDTO mapToDTO(Comentarios comentario){
        return new ComentariosResponseDTO(
                comentario.getIdComentario(),
                comentario.getPuntuacion(),
                comentario.getComentario(),
                comentario.getIdPropiedad(),
                comentario.getIdInquilino()
        );
    }

    private Comentarios mapToEntity(ComentariosRequestDTO dto){
        return new Comentarios(
                null,
                dto.getPuntuacion(),
                dto.getComentario(),
                dto.getIdPropiedad(),
                dto.getIdInquilino()
        );
    }

    // VALIDACIÓN CON FEIGN
    private void validarInquilino(Long idInquilino){
        try {
            boolean habilitado = inquilinoClient.validarInquilino(idInquilino);
            log.info(
                    ">>> Inquilino {} validado correctamente (FeignClient)",
                    idInquilino
            );
            if (!habilitado){
                throw new RuntimeException("El inquilino con ID: " + idInquilino + " esta bloqueado");
            }
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El inquilino con ID: " + idInquilino + " no existe en ms-inquilinos");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con ms-inquilinos: " + e.getMessage());
        }
    }

    private void validarPropiedad(Long idPropiedad){
        try {
            propiedadClient.validarPropiedad(idPropiedad);
            log.info(
                    ">>> Propiedad {} validada correctamente (FeignClient)",
                    idPropiedad
            );
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("La propiedad con ID: " + idPropiedad + " no existe en ms-propiedades");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con ms-propiedades: " + e.getMessage());
        }
    }

    //***CRUD***
    //GET /comentarios
    public List<ComentariosResponseDTO> mostrarComentarios(){
        return comentariosRepository
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //GET /comentarios/id
    public ComentariosResponseDTO mostrarPorId(Long idComentario){
        Comentarios comentario = comentariosRepository
                .findById(idComentario)
                .orElseThrow(() -> new RuntimeException("El comentario con ID: " + idComentario + " no existe"));
        return mapToDTO(comentario);
    }

    //POST /comentarios
    public ComentariosResponseDTO save(ComentariosRequestDTO dto){
        validarInquilino(dto.getIdInquilino());
        validarPropiedad(dto.getIdPropiedad());
        return mapToDTO(comentariosRepository.save(mapToEntity(dto)));
    }

    //PUT /comentarios/id
    public ComentariosResponseDTO editar(Long idComentario, ComentariosRequestDTO dto){
        Comentarios comentarioExistente = comentariosRepository
                .findById(idComentario)
                .orElseThrow(() -> new RuntimeException("El comentario con ID: " + idComentario + " no existe"));
        validarInquilino(dto.getIdInquilino());
        validarPropiedad(dto.getIdPropiedad());
        comentarioExistente.setPuntuacion(dto.getPuntuacion());
        comentarioExistente.setComentario(dto.getComentario());
        comentarioExistente.setIdPropiedad(dto.getIdPropiedad());
        comentarioExistente.setIdInquilino(dto.getIdInquilino());

        return mapToDTO(comentariosRepository.save(comentarioExistente));
    }

    //DELETE /comentarios/id
    public void borrar(Long idComentario){
        if (!comentariosRepository.existsById(idComentario)){
            throw new RuntimeException("El comentario con ID: " + idComentario + " no existe");
        }
        comentariosRepository.deleteById(idComentario);
    }
}
