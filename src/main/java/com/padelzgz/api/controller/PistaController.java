package com.padelzgz.api.controller;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Pista;
import com.padelzgz.api.service.PistaService;
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
@RequestMapping("/pistas")
public class PistaController {

    private static final Logger logger = LoggerFactory.getLogger(PistaController.class);

    @Autowired private PistaService pistaService;

    @GetMapping
    public ResponseEntity<Set<Pista>> getPistas(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String interior,
            @RequestParam(required = false) String activa) {
        logger.info("GET /pistas tipo={} interior={} activa={}", tipo, interior, activa);
        Set<Pista> pistas;
        if (tipo != null && interior != null && activa != null) {
            pistas = pistaService.findByTipoAndInteriorAndActiva(tipo,
                    Boolean.parseBoolean(interior), Boolean.parseBoolean(activa));
        } else if (tipo != null) {
            pistas = pistaService.findByTipo(tipo);
        } else if (interior != null) {
            pistas = pistaService.findByInterior(Boolean.parseBoolean(interior));
        } else if (activa != null) {
            pistas = pistaService.findByActiva(Boolean.parseBoolean(activa));
        } else {
            pistas = pistaService.findAll();
        }
        return ResponseEntity.ok(pistas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pista> getPista(@PathVariable long id) {
        logger.info("GET /pistas/{}", id);
        Pista pista = pistaService.findById(id).orElseThrow(() -> new PistaNotFoundException(id));
        return ResponseEntity.ok(pista);
    }

    // SQL nativa: pistas por precio
    @GetMapping("/por-precio")
    public ResponseEntity<Set<Pista>> getPistasByPrecio(
            @RequestParam float min,
            @RequestParam float max) {
        logger.info("GET /pistas/por-precio min={} max={}", min, max);
        return ResponseEntity.ok(pistaService.findByPrecioHoraBetween(min, max));
    }

    // SQL nativa: pistas con puntuación media mínima
    @GetMapping("/mejor-valoradas")
    public ResponseEntity<Set<Pista>> getPistasMejorValoradas(@RequestParam(defaultValue = "4") float minPuntuacion) {
        logger.info("GET /pistas/mejor-valoradas minPuntuacion={}", minPuntuacion);
        return ResponseEntity.ok(pistaService.findByPuntuacionMediaMinima(minPuntuacion));
    }

    @PostMapping("/club/{clubId}")
    public ResponseEntity<Pista> addPista(@PathVariable long clubId, @Valid @RequestBody Pista pista) {
        logger.info("POST /pistas/club/{}", clubId);
        Pista created = pistaService.addPista(pista, clubId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pista> modifyPista(@PathVariable long id, @Valid @RequestBody Pista pista) {
        logger.info("PUT /pistas/{}", id);
        return ResponseEntity.ok(pistaService.modifyPista(id, pista));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pista> patchPista(@PathVariable long id, @RequestBody Pista partial) {
        logger.info("PATCH /pistas/{}", id);
        return ResponseEntity.ok(pistaService.patchPista(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePista(@PathVariable long id) {
        logger.info("DELETE /pistas/{}", id);
        pistaService.deletePista(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PistaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PistaNotFoundException e) {
        logger.error("Pista no encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubNotFound(ClubNotFoundException e) {
        logger.error("Club no encontrado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            errors.put(field, err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error interno en PistaController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
