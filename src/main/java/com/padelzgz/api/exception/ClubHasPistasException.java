package com.padelzgz.api.exception;

public class ClubHasPistasException extends RuntimeException {

    public ClubHasPistasException(long clubId) {
        super("No se puede eliminar el club con id " + clubId + " porque tiene pistas asociadas");
    }
}