package com.padelzgz.api.exception;

public class PistaNotFoundException extends RuntimeException {
    public PistaNotFoundException(long id) { super("Pista no encontrado/a: " + id); }
    public PistaNotFoundException(String msg) { super(msg); }
}
