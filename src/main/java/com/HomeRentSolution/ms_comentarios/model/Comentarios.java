package com.HomeRentSolution.ms_comentarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(nullable = false, length = 500)
    private String comentario;

    @Column(name = "id_propiedad", nullable = false)
    private Long idPropiedad;

    @Column(name = "id_inquilino", nullable = false)
    private Long idInquilino;
}
