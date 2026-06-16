package com.padelzgz.api.service;

import com.padelzgz.api.dto.InscripcionInDTO;
import com.padelzgz.api.model.Inscripcion;
import java.util.Optional;
import java.util.Set;

public interface InscripcionService {
    Set<Inscripcion> findAll();
    Set<Inscripcion> findByTorneoId(long torneoId);
    Set<Inscripcion> findByUsuarioId(long usuarioId);
    Set<Inscripcion> findByEstado(String estado);
    Set<Inscripcion> findByTorneoIdAndEstado(long torneoId, String estado);
    Optional<Inscripcion> findById(long id);
    Inscripcion addInscripcion(InscripcionInDTO dto);
    Inscripcion modifyInscripcion(long id, InscripcionInDTO dto);
    Inscripcion patchInscripcion(long id, Inscripcion partial);
    void deleteInscripcion(long id);
}
