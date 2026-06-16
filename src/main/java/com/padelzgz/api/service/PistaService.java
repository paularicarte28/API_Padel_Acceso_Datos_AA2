package com.padelzgz.api.service;

import com.padelzgz.api.model.Pista;
import java.util.Optional;
import java.util.Set;

public interface PistaService {
    Set<Pista> findAll();
    Set<Pista> findByTipo(String tipo);
    Set<Pista> findByInterior(boolean interior);
    Set<Pista> findByActiva(boolean activa);
    Set<Pista> findByTipoAndInteriorAndActiva(String tipo, boolean interior, boolean activa);
    Set<Pista> findByClubId(long clubId);
    Optional<Pista> findById(long id);
    Pista addPista(Pista pista, long clubId);
    Pista modifyPista(long id, Pista pista);
    Pista patchPista(long id, Pista partialPista);
    void deletePista(long id);
    Set<Pista> findByPrecioHoraBetween(float min, float max);
    Set<Pista> findByPuntuacionMediaMinima(float minPuntuacion);
}
