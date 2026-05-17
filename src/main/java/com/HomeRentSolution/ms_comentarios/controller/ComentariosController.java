package com.HomeRentSolution.ms_comentarios.controller;

import com.HomeRentSolution.ms_comentarios.dto.ComentariosRequestDTO;
import com.HomeRentSolution.ms_comentarios.dto.ComentariosResponseDTO;
import com.HomeRentSolution.ms_comentarios.service.ComentariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comentarios")
@RequiredArgsConstructor
public class ComentariosController {

    private final ComentariosService comentariosService;

    //GET /comentarios
    @GetMapping
    public ResponseEntity<List<ComentariosResponseDTO>> getComentarios(){
        return ResponseEntity.ok(comentariosService.mostrarComentarios());
    }

    //GET /comentarios/id
    @GetMapping("{idComentario}")
    public ResponseEntity<ComentariosResponseDTO> getPorId(@PathVariable Long idComentario){
        return ResponseEntity.ok(comentariosService.mostrarPorId(idComentario));
    }

    //POST /comentarios
    @PostMapping
    public ResponseEntity<ComentariosResponseDTO> postComentario(@Valid @RequestBody ComentariosRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(comentariosService.save(dto));
    }

    //PUT /comentarios/id
    @PutMapping("{idComentario}")
    public ResponseEntity<ComentariosResponseDTO> putComentario(@PathVariable Long idComentario,
                                                               @Valid @RequestBody ComentariosRequestDTO dto){
        return ResponseEntity.ok(comentariosService.editar(idComentario, dto));
    }

    //DELETE /comentarios/id
    @DeleteMapping("{idComentario}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long idComentario){
        comentariosService.borrar(idComentario);
        return ResponseEntity.noContent().build();
    }
}
