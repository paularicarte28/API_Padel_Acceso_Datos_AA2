package com.padelzgz.api.exception;

public class ReservaNotFoundException extends RuntimeException {
    public ReservaNotFoundException(long id) { super("Reserva no encontrado/a: " + id); }
    public ReservaNotFoundException(String msg) { super(msg); }
}
