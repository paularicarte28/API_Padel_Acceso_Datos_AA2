package com.padelzgz.api.service;

import com.padelzgz.api.model.Torneo;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface TorneoService {
    Set<Torneo> findAll();
    Set<Torneo> findByInscripcionAbierta(boolean abierta);
    Set<Torneo> findByClubId(long clubId);
    Set<Torneo> findByFechaInicioBetween(LocalDate desde, LocalDate hasta);
    Set<Torneo> findByInscripcionAbiertaAndClubId(boolean abierta, long clubId);
    Optional<Torneo> findById(long id);
    Torneo addTorneo(Torneo torneo, long clubId);
    Torneo modifyTorneo(long id, Torneo torneo);
    Torneo patchTorneo(long id, Torneo partial);
    void deleteTorneo(long id);
}
