package com.padelzgz.api.exception;

public class ClubNotFoundException extends RuntimeException {
    public ClubNotFoundException(long id) { super("Club no encontrado: " + id); }
    public ClubNotFoundException(String msg) { super(msg); }
}
