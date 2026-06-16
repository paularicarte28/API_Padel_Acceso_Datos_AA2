package com.padelzgz.api.controller;

import com.padelzgz.api.dto.InscripcionInDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Inscripcion;
import com.padelzgz.api.service.InscripcionService;
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
@RequestMapping("/inscripciones")
public class InscripcionController {

    private static final Logger logger = LoggerFactory.getLogger(InscripcionController.class);

    @Autowired
    private InscripcionService inscripcionService;

    // GET /inscripciones?torneoId=1&estado=CONFIRMADA
    @GetMapping
    public ResponseEntity<Set<Inscripcion>> getInscripciones(
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) String estado) {
        logger.info("GET /inscripciones torneoId={} estado={}", torneoId, estado);
        Set<Inscripcion> inscripciones;
        if (torneoId != null && estado != null) {
            inscripciones = inscripcionService.findByTorneoIdAndEstado(torneoId, estado);
        } else if (torneoId != null) {
            inscripciones = inscripcionService.findByTorneoId(torneoId);
        } else if (estado != null) {
            inscripciones = inscripcionService.findByEstado(estado);
        } else {
            inscripciones = inscripcionService.findAll();
        }
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> getInscripcion(@PathVariable long id) {
        logger.info("GET /inscripciones/{}", id);
        Inscripcion inscripcion = inscripcionService.findById(id)
                .orElseThrow(() -> new InscripcionNotFoundException(id));
        return ResponseEntity.ok(inscripcion);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Set<Inscripcion>> getInscripcionesByUsuario(@PathVariable long usuarioId) {
        logger.info("GET /inscripciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(inscripcionService.findByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Inscripcion> addInscripcion(@Valid @RequestBody InscripcionInDTO dto) {
        logger.info("POST /inscripciones usuarioId={} torneoId={}", dto.getUsuarioId(), dto.getTorneoId());
        Inscripcion created = inscripcionService.addInscripcion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> modifyInscripcion(@PathVariable long id,
                                                          @Valid @RequestBody InscripcionInDTO dto) {
        logger.info("PUT /inscripciones/{}", id);
        return ResponseEntity.ok(inscripcionService.modifyInscripcion(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inscripcion> patchInscripcion(@PathVariable long id,
                                                         @RequestBody Inscripcion partial) {
        logger.info("PATCH /inscripciones/{}", id);
        return ResponseEntity.ok(inscripcionService.patchInscripcion(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable long id) {
        logger.info("DELETE /inscripciones/{}", id);
        inscripcionService.deleteInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(InscripcionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(InscripcionNotFoundException e) {
        logger.error("Inscripción no encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler({UsuarioNotFoundException.class, TorneoNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleRelatedNotFound(RuntimeException e) {
        logger.error("Entidad relacionada no encontrada: {}", e.getMessage());
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
        logger.error("Error en InscripcionController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
