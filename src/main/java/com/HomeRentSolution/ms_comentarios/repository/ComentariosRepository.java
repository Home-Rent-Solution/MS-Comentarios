package com.HomeRentSolution.ms_comentarios.repository;

import com.HomeRentSolution.ms_comentarios.model.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentariosRepository extends JpaRepository<Comentarios, Long>{

    // Buscar comentarios por propiedad
    @Query("SELECT c FROM Comentarios c WHERE c.idPropiedad = :idPropiedad")
    List<Comentarios> findByIdPropiedad(@Param("idPropiedad") Long idPropiedad);

    // Buscar comentarios por inquilino
    @Query("SELECT c FROM Comentarios c WHERE c.idInquilino = :idInquilino")
    List<Comentarios> findByIdInquilino(@Param("idInquilino") Long idInquilino);

    // Buscar por puntuacion
    @Query("SELECT c FROM Comentarios c WHERE c.puntuacion = :puntuacion ORDER BY c.idComentario")
    List<Comentarios> findByPuntuacion(@Param("puntuacion") Integer puntuacion);
}

