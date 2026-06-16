package com.padelzgz.api.controller;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.Usuario;
import com.padelzgz.api.service.UsuarioService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Set<Usuario>> getUsuarios(
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) String nombre) {
        logger.info("GET /usuarios nivel={} nombre={}", nivel, nombre);
        Set<Usuario> usuarios;
        if (nivel != null && nombre != null) {
            usuarios = usuarioService.findByNivelAndNombre(nivel, nombre);
        } else if (nivel != null) {
            usuarios = usuarioService.findByNivel(nivel);
        } else if (nombre != null) {
            usuarios = usuarioService.findByNombre(nombre);
        } else {
            usuarios = usuarioService.findAll();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable long id) {
        logger.info("GET /usuarios/{}", id);
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        return ResponseEntity.ok(usuario);
    }

    // SQL nativa: usuarios con más reservas
    @GetMapping("/mas-activos")
    public ResponseEntity<Set<Usuario>> getUsuariosMasActivos(@RequestParam(defaultValue = "10") int limite) {
        logger.info("GET /usuarios/mas-activos limite={}", limite);
        return ResponseEntity.ok(usuarioService.findUsuariosConMasReservas(limite));
    }

    @PostMapping
    public ResponseEntity<Usuario> addUsuario(@Valid @RequestBody Usuario usuario) {
        logger.info("POST /usuarios email={}", usuario.getEmail());
        Usuario created = usuarioService.addUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modifyUsuario(@PathVariable long id, @Valid @RequestBody Usuario usuario) {
        logger.info("PUT /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.modifyUsuario(id, usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> patchUsuario(@PathVariable long id, @RequestBody Usuario partial) {
        logger.info("PATCH /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.patchUsuario(id, partial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable long id) {
        logger.info("DELETE /usuarios/{}", id);
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UsuarioNotFoundException e) {
        logger.error("Usuario no encontrado: {}", e.getMessage());
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
        logger.error("Error en UsuarioController: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalError());
    }
}
