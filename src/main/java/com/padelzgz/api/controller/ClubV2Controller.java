package com.padelzgz.api.controller;

import com.padelzgz.api.dto.ClubOperationResponseDTO;
import com.padelzgz.api.dto.ClubV2ResponseDTO;
import com.padelzgz.api.exception.ClubHasPistasException;
import com.padelzgz.api.exception.ClubNotFoundException;
import com.padelzgz.api.exception.DuplicateClubException;
import com.padelzgz.api.exception.ErrorResponse;
import com.padelzgz.api.model.Club;
import com.padelzgz.api.repository.ClubRepository;
import com.padelzgz.api.repository.PistaRepository;
import com.padelzgz.api.service.ClubService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/clubs")
public class ClubV2Controller {

    private static final Logger logger = LoggerFactory.getLogger(ClubV2Controller.class);

    private final ClubService clubService;
    private final ClubRepository clubRepository;
    private final PistaRepository pistaRepository;

    public ClubV2Controller(ClubService clubService,
                            ClubRepository clubRepository,
                            PistaRepository pistaRepository) {
        this.clubService = clubService;
        this.clubRepository = clubRepository;
        this.pistaRepository = pistaRepository;
    }

    @GetMapping
    public ResponseEntity<Set<ClubV2ResponseDTO>> getClubsV2(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String activo) {

        logger.info("GET /api/v2/clubs ciudad={} activo={}", ciudad, activo);

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

        Set<ClubV2ResponseDTO> response = clubs.stream()
                .map(this::toV2DTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ClubOperationResponseDTO> addClubV2(@Valid @RequestBody Club club) {
        logger.info("POST /api/v2/clubs nombre={}", club.getNombre());

        if (clubRepository.existsByNombreIgnoreCaseAndCiudadIgnoreCase(club.getNombre(), club.getCiudad())) {
            throw new DuplicateClubException(club.getNombre(), club.getCiudad());
        }

        Club created = clubService.addClub(club);

        ClubOperationResponseDTO response = new ClubOperationResponseDTO(
                "Club creado correctamente en la version 2 de la API",
                toV2DTO(created)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubOperationResponseDTO> modifyClubV2(
            @PathVariable long id,
            @Valid @RequestBody Club club) {

        logger.info("PUT /api/v2/clubs/{}", id);

        Club duplicated = clubRepository
                .findByNombreIgnoreCaseAndCiudadIgnoreCase(club.getNombre(), club.getCiudad())
                .orElse(null);

        if (duplicated != null && duplicated.getId() != id) {
            throw new DuplicateClubException(club.getNombre(), club.getCiudad());
        }

        Club updated = clubService.modifyClub(id, club);

        ClubOperationResponseDTO response = new ClubOperationResponseDTO(
                "Club actualizado correctamente en la version 2 de la API",
                toV2DTO(updated)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClubOperationResponseDTO> deleteClubV2(@PathVariable long id) {
        logger.info("DELETE /api/v2/clubs/{}", id);

        Club club = clubService.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));

        long totalPistas = pistaRepository.countByClubId(id);

        if (totalPistas > 0) {
            throw new ClubHasPistasException(id);
        }

        ClubV2ResponseDTO deletedClub = toV2DTO(club);

        clubService.deleteClub(id);

        ClubOperationResponseDTO response = new ClubOperationResponseDTO(
                "Club eliminado correctamente en la version 2 de la API",
                deletedClub
        );

        return ResponseEntity.ok(response);
    }

    private ClubV2ResponseDTO toV2DTO(Club club) {
        long totalPistas = pistaRepository.countByClubId(club.getId());
        long pistasActivas = pistaRepository.countByClubIdAndActiva(club.getId(), true);

        return new ClubV2ResponseDTO(
                club.getId(),
                club.getNombre(),
                club.getCiudad(),
                club.getDireccion(),
                club.getTelefono(),
                club.getEmail(),
                club.getFechaApertura(),
                club.isActivo(),
                totalPistas,
                pistasActivas
        );
    }

    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ClubNotFoundException e) {
        logger.error("Club no encontrado en v2: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(DuplicateClubException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClub(DuplicateClubException e) {
        logger.error("Club duplicado en v2: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.conflict(e.getMessage()));
    }

    @ExceptionHandler(ClubHasPistasException.class)
    public ResponseEntity<ErrorResponse> handleClubHasPistas(ClubHasPistasException e) {
        logger.error("Club con pistas asociadas en v2: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.conflict(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            errors.put(field, err.getDefaultMessage());
        });

        logger.error("Error de validacion en ClubV2Controller: {}", errors);
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error interno en ClubV2Controller: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}