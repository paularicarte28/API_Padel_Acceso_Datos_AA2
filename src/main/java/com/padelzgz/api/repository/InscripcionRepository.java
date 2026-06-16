package com.padelzgz.api.repository;

import com.padelzgz.api.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    Set<Inscripcion> findByTorneoId(long torneoId);
    Set<Inscripcion> findByUsuarioId(long usuarioId);
    Set<Inscripcion> findByEstado(String estado);
    Set<Inscripcion> findByTorneoIdAndEstado(long torneoId, String estado);
}
