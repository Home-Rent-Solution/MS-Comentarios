package com.HomeRentSolution.ms_comentarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentariosResponseDTO {

    // Sin validaciones: este DTO es de SALIDA,
    // el servidor lo construye, no viene del cliente

    private Long idComentario;
    private Integer puntuacion;
    private String comentario;
    private Long idPropiedad;
    private Long idInquilino;
}
