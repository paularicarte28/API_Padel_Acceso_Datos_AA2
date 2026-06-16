package com.padelzgz.api.service.impl;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.PistaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PistaServiceImpl implements PistaService {

    @Autowired private PistaRepository pistaRepository;
    @Autowired private ClubRepository clubRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Set<Pista> findAll() { return pistaRepository.findAll().stream().collect(Collectors.toSet()); }

    @Override
    public Set<Pista> findByTipo(String tipo) { return pistaRepository.findByTipo(tipo); }

    @Override
    public Set<Pista> findByInterior(boolean interior) { return pistaRepository.findByInterior(interior); }

    @Override
    public Set<Pista> findByActiva(boolean activa) { return pistaRepository.findByActiva(activa); }

    @Override
    public Set<Pista> findByTipoAndInteriorAndActiva(String tipo, boolean interior, boolean activa) {
        return pistaRepository.findByTipoAndInteriorAndActiva(tipo, interior, activa);
    }

    @Override
    public Set<Pista> findByClubId(long clubId) { return pistaRepository.findByClubId(clubId); }

    @Override
    public Optional<Pista> findById(long id) { return pistaRepository.findById(id); }

    @Override
    public Pista addPista(Pista pista, long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new ClubNotFoundException(clubId));
        pista.setClub(club);
        return pistaRepository.save(pista);
    }

    @Override
    public Pista modifyPista(long id, Pista newPista) {
        Pista existing = pistaRepository.findById(id).orElseThrow(() -> new PistaNotFoundException(id));
        existing.setNumero(newPista.getNumero());
        existing.setTipo(newPista.getTipo());
        existing.setInterior(newPista.isInterior());
        existing.setPrecioHora(newPista.getPrecioHora());
        existing.setActiva(newPista.isActiva());
        existing.setSuperficie(newPista.getSuperficie());
        return pistaRepository.save(existing);
    }

    @Override
    public Pista patchPista(long id, Pista partial) {
        Pista existing = pistaRepository.findById(id).orElseThrow(() -> new PistaNotFoundException(id));
        if (partial.getTipo() != null) existing.setTipo(partial.getTipo());
        if (partial.getSuperficie() != null) existing.setSuperficie(partial.getSuperficie());
        if (partial.getPrecioHora() > 0) existing.setPrecioHora(partial.getPrecioHora());
        if (partial.getNumero() > 0) existing.setNumero(partial.getNumero());
        existing.setInterior(partial.isInterior());
        existing.setActiva(partial.isActiva());
        return pistaRepository.save(existing);
    }

    @Override
    public void deletePista(long id) {
        pistaRepository.findById(id).orElseThrow(() -> new PistaNotFoundException(id));
        pistaRepository.deleteById(id);
    }

    @Override
    public Set<Pista> findByPrecioHoraBetween(float min, float max) {
        return pistaRepository.findByPrecioHoraBetween(min, max);
    }

    @Override
    public Set<Pista> findByPuntuacionMediaMinima(float minPuntuacion) {
        Set<Long> ids = pistaRepository.findIdsByPuntuacionMediaMinima(minPuntuacion);
        return ids.stream()
                .map(id -> pistaRepository.findById(id).orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toSet());
    }
}
