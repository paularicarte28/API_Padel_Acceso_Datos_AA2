package com.padelzgz.api.controller;

import com.padelzgz.api.dto.ClubPistasResumenDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Club;
import com.padelzgz.api.model.Pista;
import com.padelzgz.api.service.ClubService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private static final Logger logger = LoggerFactory.getLogger(ClubController.class);

    @Autowired private ClubService clubService;
    @Autowired private PistaService pistaService;

    // GET /clubs?ciudad=Zaragoza&activo=true
    @GetMapping
    public ResponseEntity<Set<Club>> getClubs(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String activo) {
        logger.info("GET /clubs ciudad={} activo={}", ciudad, activo);
        Set<Club> clubs;
        if (ciudad != null && activo != null) {
            clubs = clubService.findByCiudadAndActivo(ciudad, Boolean.parseBoolean(activo));
        } else if (ciudad != null) {
            clubs = clubService.findByCiudad(ciudad);
        } else if (activo != null) {
            clubs = clubService.findByActivo(Boolean.parseBoolean(activo));
        } else {
            clubs = clubService.findAll();
        }
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClub(@PathVariable long id) {
        logger.info("GET /clubs/{}", id);
        Club club = clubService.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
        return ResponseEntity.ok(club);
    }

    // Endpoint especial: resumen de pistas de un club con valoración media (usa DTO + relaciones)
    @GetMapping("/{id}/pistas-resumen")
    public ResponseEntity<ClubPistasResumenDTO> getClubPistasResumen(@PathVariable long id) {
        logger.info("GET /clubs/{}/pistas-resumen", id);
        Club club = clubService.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
        Set<Pista> pistas = pistaService.findByClubId(id);

        List<ClubPistasResumenDTO.PistaResumenDTO> pistaResumenes = pistas.stream().map(p -> {
            double media = p.getValoraciones() == null || p.getValoraciones().isEmpty() ? 0.0
                    : p.getValoraciones().stream().mapToInt(v -> v.getPuntuacion()).average().orElse(0.0);
            return new ClubPistasResumenDTO.PistaResumenDTO(
                    p.getId(), p.getNumero(), p.getTipo(), p.isInterior(), p.getPrecioHora(), media);
        }).collect(Collectors.toList());

        ClubPistasResumenDTO dto = new ClubPistasResumenDTO(
                club.getId(), club.getNombre(), club.getCiudad(),
                pistas.size(), (int) pistas.stream().filter(Pista::isActiva).count(),
                pistaResumenes);

        return ResponseEntity.ok(dto);
    }

    // Endpoint SQL nativa: clubs con más pistas activas
    @GetMapping("/mas-activos")
    public ResponseEntity<Set<Club>> getClubesMasActivos() {
        logger.info("GET /clubs/mas-activos");
        return ResponseEntity.ok(clubService.findClubesConMasPistasActivas());
    }

    @PostMapping
    public ResponseEntity<Club> addClub(@Valid @RequestBody Club club) {
        logger.info("POST /clubs nombre={}", club.getNombre());
        Club created = clubService.addClub(club);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> modifyClub(@PathVariable long id, @Valid @RequestBody Club club) {
        logger.info("PUT /clubs/{}", id);
        Club updated = clubService.modifyClub(id, club);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Club> patchClub(@PathVariable long id, @RequestBody Club partialClub) {
        logger.info("PATCH /clubs/{}", id);
        Club updated = clubService.patchClub(id, partialClub);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable long id) {
        logger.info("DELETE /clubs/{}", id);
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }

    // Manejadores de excepciones
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ClubNotFoundException e) {
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
        logger.error("Error de validación en Club: {}", errors);
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error interno en ClubController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
