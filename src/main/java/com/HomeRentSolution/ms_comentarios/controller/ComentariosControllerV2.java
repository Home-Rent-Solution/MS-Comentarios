package com.HomeRentSolution.ms_comentarios.controller;

import com.HomeRentSolution.ms_comentarios.assemblers.ComentariosModelAssembler;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosRequestDTO;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import com.HomeRentSolution.ms_comentarios.service.ComentariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/comentarios")
@RequiredArgsConstructor
@Tag(
        name = "Comentarios",
        description = "Controlador para la gestión de reseñas, feedback y calificaciones de los alojamientos"
)
public class ComentariosControllerV2 {

    private final ComentariosService comentariosService;
    private final ComentariosModelAssembler assembler;

    //GET /comentarios
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Obtener todos los comentarios",
            description = "Recupera una lista completa con todas las reseñas y valoraciones almacenadas en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de comentarios recuperada con éxito",
            content = @Content(
                    mediaType = "application/hal + json",
                    array = @ArraySchema(schema = @Schema(implementation = ComentariosResponseDTO.class))
            )
    )
    public CollectionModel<EntityModel<ComentariosResponseDTO>> getComentarios(){
        List<EntityModel<ComentariosResponseDTO>> comentarios = comentariosService
                .mostrarComentarios()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(
                comentarios,
                linkTo(methodOn(ComentariosControllerV2
                        .class)
                        .getComentarios())
                        .withSelfRel()
        );
    }

    //GET /comentarios/id
    @GetMapping(
            value = "{idComentario}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Obtener un comentario por ID",
            description = "Busca y devuelve el detalle de una reseña específica utilizando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Comentario localizado correctamente",
                    content = @Content(
                            mediaType = "application/hal + json",
                            schema = @Schema(implementation = ComentariosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El ID del comentario solicitado no existe",
                    content = @Content
            )
    })
    public EntityModel<ComentariosResponseDTO> getPorId(
            @Parameter(
                    description = "ID numérico del comentario a consultar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idComentario){
        ComentariosResponseDTO dto = comentariosService.mostrarPorId(idComentario);
        return assembler.toModel(dto);
    }

    //POST /comentarios
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Registrar un nuevo comentario",
            description = "Crea una reseña en la plataforma asociada a una propiedad. Valida las restricciones del" +
                    " cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comentario publicado de forma exitosa",
                    content = @Content(
                            mediaType = "application/hal + json",
                            schema = @Schema(implementation = ComentariosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos de entrada o parámetros del cuerpo incorrectos",
                    content = @Content
            )
    })
    public ResponseEntity<EntityModel<ComentariosResponseDTO>> postComentario(
            @io
                    .swagger
                    .v3
                    .oas
                    .annotations
                    .parameters
                    .RequestBody(
                        description = "Datos estructurados requeridos para publicar el comentario",
                        required = true
                    )
            @Valid @RequestBody ComentariosRequestDTO dto){
        ComentariosResponseDTO nuevoDto = comentariosService.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(nuevoDto));
    }

    //PUT /comentarios/id
    @PutMapping(
            value = "{idComentario}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Editar un comentario existente",
            description = "Permite modificar el contenido de texto o la calificación de una reseña basándose en su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comentario actualizado con éxito",
                    content = @Content(
                            mediaType = "application/hal + json",
                            schema = @Schema(implementation = ComentariosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID inexistente o datos de edición inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<EntityModel<ComentariosResponseDTO>> putComentario(
            @Parameter(
                    description = "ID del comentario que se desea editar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idComentario,
            @io
                    .swagger
                    .v3
                    .oas
                    .annotations
                    .parameters
                    .RequestBody(
                        description = "Esquema con la nueva información del comentario",
                        required = true
                    )
            @Valid @RequestBody ComentariosRequestDTO dto){
        ComentariosResponseDTO editadoDto = comentariosService.editar(
                idComentario,
                dto
        );
        return ResponseEntity.ok(assembler.toModel(editadoDto));
    }

    //DELETE /comentarios/id
    @DeleteMapping(
            value = "{idComentario}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Eliminar un comentario",
            description = "Remueve permanentemente una reseña del sistema a través de su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Comentario eliminado exitosamente (Sin contenido de retorno)"
                    , content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se encontró ningún comentario con el ID especificado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteComentario(
            @Parameter(
                    description = "ID del comentario a remover",
                    example = "1",
                    required = true
            )
            @PathVariable Long idComentario){
        comentariosService.borrar(idComentario);
        return ResponseEntity
                .noContent()
                .build();
    }
}
