package com.padelzgz.api.exception;

public class InscripcionNotFoundException extends RuntimeException {
    public InscripcionNotFoundException(long id) { super("Inscripcion no encontrado/a: " + id); }
    public InscripcionNotFoundException(String msg) { super(msg); }
}
