package com.padelzgz.api.service.impl;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.ValoracionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Autowired private ValoracionRepository valoracionRepository;
    @Autowired private PistaRepository pistaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Set<Valoracion> findAll() { return valoracionRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Valoracion> findByPistaId(long pistaId) { return valoracionRepository.findByPistaId(pistaId); }

    @Override
    public Set<Valoracion> findByUsuarioId(long usuarioId) { return valoracionRepository.findByUsuarioId(usuarioId); }

    @Override
    public Set<Valoracion> findByPuntuacion(int puntuacion) { return valoracionRepository.findByPuntuacion(puntuacion); }

    @Override
    public Set<Valoracion> findByPistaIdAndPuntuacion(long pistaId, int puntuacion) {
        return valoracionRepository.findByPistaIdAndPuntuacion(pistaId, puntuacion);
    }

    @Override
    public Optional<Valoracion> findById(long id) { return valoracionRepository.findById(id); }

    @Override
    public Valoracion addValoracion(Valoracion valoracion, long pistaId, long usuarioId) {
        Pista pista = pistaRepository.findById(pistaId).orElseThrow(() -> new PistaNotFoundException(pistaId));
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        valoracion.setPista(pista);
        valoracion.setUsuario(usuario);
        valoracion.setFechaValoracion(LocalDate.now());
        return valoracionRepository.save(valoracion);
    }

    @Override
    public Valoracion modifyValoracion(long id, Valoracion newValoracion) {
        Valoracion existing = valoracionRepository.findById(id).orElseThrow(() -> new ValoracionNotFoundException(id));
        existing.setPuntuacion(newValoracion.getPuntuacion());
        existing.setComentario(newValoracion.getComentario());
        existing.setVisibilidad(newValoracion.getVisibilidad());
        existing.setVerificada(newValoracion.isVerificada());
        existing.setUtilCount(newValoracion.getUtilCount());
        if (newValoracion.getFechaValoracion() != null) {
            existing.setFechaValoracion(newValoracion.getFechaValoracion());
        }
        return valoracionRepository.save(existing);
    }

    @Override
    public Valoracion patchValoracion(long id, Valoracion partial) {
        Valoracion existing = valoracionRepository.findById(id).orElseThrow(() -> new ValoracionNotFoundException(id));
        if (partial.getPuntuacion() > 0) existing.setPuntuacion(partial.getPuntuacion());
        if (partial.getComentario() != null) existing.setComentario(partial.getComentario());
        if (partial.getVisibilidad() != null) existing.setVisibilidad(partial.getVisibilidad());
        existing.setVerificada(partial.isVerificada());
        return valoracionRepository.save(existing);
    }

    @Override
    public void deleteValoracion(long id) {
        valoracionRepository.findById(id).orElseThrow(() -> new ValoracionNotFoundException(id));
        valoracionRepository.deleteById(id);
    }
}
