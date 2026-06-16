package com.padelzgz.api.service.impl;

import com.padelzgz.api.dto.InscripcionInDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired private InscripcionRepository inscripcionRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TorneoRepository torneoRepository;

    @Override
    public Set<Inscripcion> findAll() { return inscripcionRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Inscripcion> findByTorneoId(long torneoId) { return inscripcionRepository.findByTorneoId(torneoId); }

    @Override
    public Set<Inscripcion> findByUsuarioId(long usuarioId) { return inscripcionRepository.findByUsuarioId(usuarioId); }

    @Override
    public Set<Inscripcion> findByEstado(String estado) { return inscripcionRepository.findByEstado(estado); }

    @Override
    public Set<Inscripcion> findByTorneoIdAndEstado(long torneoId, String estado) {
        return inscripcionRepository.findByTorneoIdAndEstado(torneoId, estado);
    }

    @Override
    public Optional<Inscripcion> findById(long id) { return inscripcionRepository.findById(id); }

    @Override
    public Inscripcion addInscripcion(InscripcionInDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.getUsuarioId()));
        Torneo torneo = torneoRepository.findById(dto.getTorneoId())
                .orElseThrow(() -> new TorneoNotFoundException(dto.getTorneoId()));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion());
        inscripcion.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        inscripcion.setPagado(dto.isPagado());
        inscripcion.setNumeroPareja(dto.getNumeroPareja());
        inscripcion.setNotas(dto.getNotas());
        inscripcion.setUsuario(usuario);
        inscripcion.setTorneo(torneo);

        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public Inscripcion modifyInscripcion(long id, InscripcionInDTO dto) {
        Inscripcion existing = inscripcionRepository.findById(id).orElseThrow(() -> new InscripcionNotFoundException(id));
        existing.setEstado(dto.getEstado());
        existing.setPagado(dto.isPagado());
        existing.setNotas(dto.getNotas());
        existing.setNumeroPareja(dto.getNumeroPareja());
        return inscripcionRepository.save(existing);
    }

    @Override
    public Inscripcion patchInscripcion(long id, Inscripcion partial) {
        Inscripcion existing = inscripcionRepository.findById(id).orElseThrow(() -> new InscripcionNotFoundException(id));
        if (partial.getEstado() != null) existing.setEstado(partial.getEstado());
        if (partial.getNotas() != null) existing.setNotas(partial.getNotas());
        existing.setPagado(partial.isPagado());
        return inscripcionRepository.save(existing);
    }

    @Override
    public void deleteInscripcion(long id) {
        inscripcionRepository.findById(id).orElseThrow(() -> new InscripcionNotFoundException(id));
        inscripcionRepository.deleteById(id);
    }
}
