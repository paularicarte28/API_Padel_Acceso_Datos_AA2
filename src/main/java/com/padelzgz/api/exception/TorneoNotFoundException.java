package com.padelzgz.api.exception;

public class TorneoNotFoundException extends RuntimeException {
    public TorneoNotFoundException(long id) { super("Torneo no encontrado/a: " + id); }
    public TorneoNotFoundException(String msg) { super(msg); }
}
