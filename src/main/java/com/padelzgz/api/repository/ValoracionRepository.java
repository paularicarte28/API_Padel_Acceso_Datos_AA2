package com.padelzgz.api.repository;

import com.padelzgz.api.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Set<Valoracion> findByPistaId(long pistaId);
    Set<Valoracion> findByUsuarioId(long usuarioId);
    Set<Valoracion> findByPuntuacion(int puntuacion);
    Set<Valoracion> findByPistaIdAndPuntuacion(long pistaId, int puntuacion);
}
