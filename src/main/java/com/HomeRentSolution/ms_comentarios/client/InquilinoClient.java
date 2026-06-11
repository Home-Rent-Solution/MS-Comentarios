package com.HomeRentSolution.ms_comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "inquilino-client",
        url = "${ms.inquilinos.url}"
)
public interface InquilinoClient {

    // Validar si el inquilino existe y no está bloqueado
    @GetMapping("/api/v1/inquilinos/{id}/validar")
    boolean validarInquilino(@PathVariable Long id);
}
