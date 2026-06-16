package com.padelzgz.api.controller;

import com.padelzgz.api.dto.ReservaInDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Reserva;
import com.padelzgz.api.service.ReservaService;
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
@RequestMapping("/reservas")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<Set<Reserva>> getReservas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String pagado) {
        logger.info("GET /reservas fecha={} pagado={}", fecha, pagado);
        Set<Reserva> reservas;
        if (fecha != null && pagado != null) {
            reservas = reservaService.findByFechaAndPagado(fecha, Boolean.parseBoolean(pagado));
        } else if (fecha != null) {
            reservas = reservaService.findByFecha(fecha);
        } else if (pagado != null) {
            reservas = reservaService.findByPagado(Boolean.parseBoolean(pagado));
        } else {
            reservas = reservaService.findAll();
        }
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReserva(@PathVariable long id) {
        logger.info("GET /reservas/{}", id);
        return ResponseEntity.ok(reservaService.findById(id).orElseThrow(() -> new ReservaNotFoundException(id)));
    }

    @GetMapping("/pista/{pistaId}")
    public ResponseEntity<Set<Reserva>> getReservasByPista(@PathVariable long pistaId) {
        logger.info("GET /reservas/pista/{}", pistaId);
        return ResponseEntity.ok(reservaService.findByPistaId(pistaId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Set<Reserva>> getReservasByUsuario(@PathVariable long usuarioId) {
        logger.info("GET /reservas/usuario/{}", usuarioId);
        return ResponseEntity.ok(reservaService.findByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Reserva> addReserva(@Valid @RequestBody ReservaInDTO dto) {
        logger.info("POST /reservas pistaId={} usuarioId={}", dto.getPistaId(), dto.getUsuarioId());
        Reserva created = reservaService.addReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> modifyReserva(@PathVariable long id, @Valid @RequestBody ReservaInDTO dto) {
        logger.info("PUT /reservas/{}", id);
        return ResponseEntity.ok(reservaService.modifyReserva(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Reserva> patchReserva(@PathVariable long id, @RequestBody Reserva partial) {
        logger.info("PATCH /reservas/{}", id);
        return ResponseEntity.ok(reservaService.patchReserva(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable long id) {
        logger.info("DELETE /reservas/{}", id);
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReservaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ReservaNotFoundException e) {
        logger.error("Reserva no encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler({PistaNotFoundException.class, UsuarioNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleRelatedNotFound(RuntimeException e) {
        logger.error("Entidad relacionada no encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            errors.put(((FieldError) err).getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Error en ReservaController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
