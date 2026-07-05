package com.HomeRentSolution.ms_comentarios.config;

import com.HomeRentSolution.ms_comentarios.model.Comentarios;
import com.HomeRentSolution.ms_comentarios.repository.ComentariosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Component
public class DataInitializar implements CommandLineRunner{

    private final ComentariosRepository comentariosRepository;


    @Autowired
    public DataInitializar(ComentariosRepository comentariosRepository) {
        this.comentariosRepository = comentariosRepository;
    }

    @Override
    public void run(String... args){
        if (comentariosRepository.count() > 0){
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial");
            return;
        }
        log.info(">>> DataInitializer: BD vacia detectada, insertando comentarios de prueba");

        comentariosRepository.save(new Comentarios(
                null,
                5,
                "Excelente propiedad, muy limpia y bien ubicada",
                1L,
                1L
        ));

        comentariosRepository.save(new Comentarios(
                null,
                4,
                "Muy buena experiencia, el anfitrion fue muy amable",
                2L,
                1L
        ));

        comentariosRepository.save(new Comentarios(
                null,
                3,
                "Buena propiedad pero le falta mantenimiento",
                3L,
                2L
        ));

        log.info(
                ">>> DataInitializer: {} comentarios insertados correctamente",
                comentariosRepository.count()
        );
    }
}
