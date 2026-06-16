package com.padelzgz.api.exception;

public class ValoracionNotFoundException extends RuntimeException {
    public ValoracionNotFoundException(long id) { super("Valoracion no encontrado/a: " + id); }
    public ValoracionNotFoundException(String msg) { super(msg); }
}
