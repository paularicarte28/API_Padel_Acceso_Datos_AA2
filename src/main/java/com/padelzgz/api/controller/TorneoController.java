package com.padelzgz.api.controller;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Torneo;
import com.padelzgz.api.service.TorneoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/torneos")
public class TorneoController {

    private static final Logger logger = LoggerFactory.getLogger(TorneoController.class);

    @Autowired private TorneoService torneoService;

    @GetMapping
    public ResponseEntity<Set<Torneo>> getTorneos(
            @RequestParam(required = false) String inscripcionAbierta,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        logger.info("GET /torneos");
        Set<Torneo> torneos;
        if (inscripcionAbierta != null && clubId != null) {
            torneos = torneoService.findByInscripcionAbiertaAndClubId(Boolean.parseBoolean(inscripcionAbierta), clubId);
        } else if (desde != null && hasta != null) {
            torneos = torneoService.findByFechaInicioBetween(desde, hasta);
        } else if (clubId != null) {
            torneos = torneoService.findByClubId(clubId);
        } else if (inscripcionAbierta != null) {
            torneos = torneoService.findByInscripcionAbierta(Boolean.parseBoolean(inscripcionAbierta));
        } else {
            torneos = torneoService.findAll();
        }
        return ResponseEntity.ok(torneos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Torneo> getTorneo(@PathVariable long id) {
        return ResponseEntity.ok(torneoService.findById(id).orElseThrow(() -> new TorneoNotFoundException(id)));
    }

    @PostMapping("/club/{clubId}")
    public ResponseEntity<Torneo> addTorneo(@PathVariable long clubId, @Valid @RequestBody Torneo torneo) {
        logger.info("POST /torneos/club/{}", clubId);
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.addTorneo(torneo, clubId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Torneo> modifyTorneo(@PathVariable long id, @Valid @RequestBody Torneo torneo) {
        return ResponseEntity.ok(torneoService.modifyTorneo(id, torneo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Torneo> patchTorneo(@PathVariable long id, @RequestBody Torneo partial) {
        return ResponseEntity.ok(torneoService.patchTorneo(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTorneo(@PathVariable long id) {
        torneoService.deleteTorneo(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(TorneoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(TorneoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> errors.put(((FieldError) err).getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error en TorneoController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
