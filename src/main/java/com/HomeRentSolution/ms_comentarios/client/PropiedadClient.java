package com.HomeRentSolution.ms_comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-propiedades",
        url = "${ms.propiedades.url}"
)
public interface PropiedadClient {

    // Validar si la propiedad existe
    @GetMapping("/api/v1/propiedades/{id}")
    Object validarPropiedad(@PathVariable Long id);
}
