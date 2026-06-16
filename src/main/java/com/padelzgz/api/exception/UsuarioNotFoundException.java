package com.padelzgz.api.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(long id) { super("Usuario no encontrado/a: " + id); }
    public UsuarioNotFoundException(String msg) { super(msg); }
}
