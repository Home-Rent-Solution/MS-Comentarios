package com.HomeRentSolution.ms_comentarios.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentariosRequestDTO {

    // idComentario no se incluye porque MySQL lo genera

    @NotNull(message = "La puntuacion es obligatoria")
    @Min(
            value = 1,
            message = "La puntuacion minima es 1"
    )
    @Max(
            value = 10,
            message = "La puntuacion maxima es 10"
    )
    private Integer puntuacion;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(
            max = 500,
            message = "El comentario no puede superar los 500 caracteres"
    )
    private String comentario;

    @NotNull(message = "El id de la propiedad es obligatorio")
    private Long idPropiedad;

    @NotNull(message = "El id del inquilino es obligatorio")
    private Long idInquilino;
}
