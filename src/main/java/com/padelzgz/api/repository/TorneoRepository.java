package com.padelzgz.api.repository;

import com.padelzgz.api.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    Set<Torneo> findByInscripcionAbierta(boolean inscripcionAbierta);
    Set<Torneo> findByClubId(long clubId);
    Set<Torneo> findByFechaInicioBetween(LocalDate desde, LocalDate hasta);
    Set<Torneo> findByInscripcionAbiertaAndClubId(boolean inscripcionAbierta, long clubId);
}
