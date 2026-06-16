package com.padelzgz.api.exception;

public class DuplicateClubException extends RuntimeException {

    public DuplicateClubException(String nombre, String ciudad) {
        super("Ya existe un club con el nombre '" + nombre + "' en la ciudad '" + ciudad + "'");
    }
}