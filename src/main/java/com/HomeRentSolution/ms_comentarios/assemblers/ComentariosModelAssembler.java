package com.HomeRentSolution.ms_comentarios.assemblers;

import com.HomeRentSolution.ms_comentarios.controller.ComentariosController;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComentariosModelAssembler implements RepresentationModelAssembler<ComentariosResponseDTO,
        EntityModel<ComentariosResponseDTO>> {

    @Override
    public EntityModel<ComentariosResponseDTO> toModel (ComentariosResponseDTO dto){

        return EntityModel.of(
                dto,
                linkTo(methodOn(ComentariosController
                        .class)
                        .getPorId(dto.getIdComentario()))
                        .withSelfRel(),
                linkTo(methodOn(ComentariosController
                        .class)
                        .getComentarios())
                        .withRel("comentarios")
                );
    }
}
