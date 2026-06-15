package com.HomeRentSolution.ms_comentarios.util;

import com.HomeRentSolution.ms_comentarios.model.Comentarios;
import com.HomeRentSolution.ms_comentarios.repository.ComentariosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile({"dev", "test"})
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private final ComentariosRepository comentariosRepository;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();

        //generamos 40 comentarios/reseñas
        for(int i = 0; i < 40; i++){

            Comentarios comentarios = new Comentarios();
            comentarios.setPuntuacion(faker.number().numberBetween(1, 10));
            comentarios.setComentario(faker.lorem().paragraph(2));
            comentarios.setIdPropiedad((long) faker.number().numberBetween(1, 20));
            comentarios.setIdInquilino((long) faker.number().numberBetween(1, 30));
            comentariosRepository.save(comentarios);
        }
        log.info(">> ms-comentarios: ¡Base de datos poblada con relaciones a propiedades e inquilinos!");
    }
}
