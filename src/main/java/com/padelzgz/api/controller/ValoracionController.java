package com.padelzgz.api.controller;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Valoracion;
import com.padelzgz.api.service.ValoracionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/valoraciones")
public class ValoracionController {

    private static final Logger logger = LoggerFactory.getLogger(ValoracionController.class);

    @Autowired
    private ValoracionService valoracionService;

    // GET /valoraciones?pistaId=1&puntuacion=5
    @GetMapping
    public ResponseEntity<Set<Valoracion>> getValoraciones(
            @RequestParam(required = false) Long pistaId,
            @RequestParam(required = false) Integer puntuacion) {
        logger.info("GET /valoraciones pistaId={} puntuacion={}", pistaId, puntuacion);
        Set<Valoracion> valoraciones;
        if (pistaId != null && puntuacion != null) {
            valoraciones = valoracionService.findByPistaIdAndPuntuacion(pistaId, puntuacion);
        } else if (pistaId != null) {
            valoraciones = valoracionService.findByPistaId(pistaId);
        } else if (puntuacion != null) {
            valoraciones = valoracionService.findByPuntuacion(puntuacion);
        } else {
            valoraciones = valoracionService.findAll();
        }
        return ResponseEntity.ok(valoraciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Valoracion> getValoracion(@PathVariable long id) {
        logger.info("GET /valoraciones/{}", id);
        return ResponseEntity.ok(valoracionService.findById(id)
                .orElseThrow(() -> new ValoracionNotFoundException(id)));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Set<Valoracion>> getValoracionesByUsuario(@PathVariable long usuarioId) {
        logger.info("GET /valoraciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(valoracionService.findByUsuarioId(usuarioId));
    }

    // POST /valoraciones/pista/{pistaId}/usuario/{usuarioId}
    @PostMapping("/pista/{pistaId}/usuario/{usuarioId}")
    public ResponseEntity<Valoracion> addValoracion(@PathVariable long pistaId,
                                                     @PathVariable long usuarioId,
                                                     @Valid @RequestBody Valoracion valoracion) {
        logger.info("POST /valoraciones pistaId={} usuarioId={}", pistaId, usuarioId);
        Valoracion created = valoracionService.addValoracion(valoracion, pistaId, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Valoracion> modifyValoracion(@PathVariable long id,
                                                        @Valid @RequestBody Valoracion valoracion) {
        logger.info("PUT /valoraciones/{}", id);
        return ResponseEntity.ok(valoracionService.modifyValoracion(id, valoracion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Valoracion> patchValoracion(@PathVariable long id,
                                                       @RequestBody Valoracion partial) {
        logger.info("PATCH /valoraciones/{}", id);
        return ResponseEntity.ok(valoracionService.patchValoracion(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValoracion(@PathVariable long id) {
        logger.info("DELETE /valoraciones/{}", id);
        valoracionService.deleteValoracion(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ValoracionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ValoracionNotFoundException e) {
        logger.error("Valoración no encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler({PistaNotFoundException.class, UsuarioNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleRelatedNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err ->
                errors.put(((FieldError) err).getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error en ValoracionController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
