package com.padelzgz.api.service.impl;

import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.TorneoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class TorneoServiceImpl implements TorneoService {

    @Autowired private TorneoRepository torneoRepository;
    @Autowired private ClubRepository clubRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Set<Torneo> findAll() { return torneoRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Torneo> findByInscripcionAbierta(boolean abierta) { return torneoRepository.findByInscripcionAbierta(abierta); }

    @Override
    public Set<Torneo> findByClubId(long clubId) { return torneoRepository.findByClubId(clubId); }

    @Override
    public Set<Torneo> findByFechaInicioBetween(LocalDate desde, LocalDate hasta) {
        return torneoRepository.findByFechaInicioBetween(desde, hasta);
    }

    @Override
    public Set<Torneo> findByInscripcionAbiertaAndClubId(boolean abierta, long clubId) {
        return torneoRepository.findByInscripcionAbiertaAndClubId(abierta, clubId);
    }

    @Override
    public Optional<Torneo> findById(long id) { return torneoRepository.findById(id); }

    @Override
    public Torneo addTorneo(Torneo torneo, long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new ClubNotFoundException(clubId));
        torneo.setClub(club);
        return torneoRepository.save(torneo);
    }

    @Override
    public Torneo modifyTorneo(long id, Torneo newTorneo) {
        Torneo existing = torneoRepository.findById(id).orElseThrow(() -> new TorneoNotFoundException(id));
        existing.setNombre(newTorneo.getNombre());
        existing.setDescripcion(newTorneo.getDescripcion());
        existing.setFechaInicio(newTorneo.getFechaInicio());
        existing.setFechaFin(newTorneo.getFechaFin());
        existing.setMaxParticipantes(newTorneo.getMaxParticipantes());
        existing.setInscripcionAbierta(newTorneo.isInscripcionAbierta());
        existing.setPrecioInscripcion(newTorneo.getPrecioInscripcion());
        return torneoRepository.save(existing);
    }

    @Override
    public Torneo patchTorneo(long id, Torneo partial) {
        Torneo existing = torneoRepository.findById(id).orElseThrow(() -> new TorneoNotFoundException(id));
        if (partial.getNombre() != null) existing.setNombre(partial.getNombre());
        if (partial.getDescripcion() != null) existing.setDescripcion(partial.getDescripcion());
        if (partial.getFechaFin() != null) existing.setFechaFin(partial.getFechaFin());
        if (partial.getMaxParticipantes() > 0) existing.setMaxParticipantes(partial.getMaxParticipantes());
        existing.setInscripcionAbierta(partial.isInscripcionAbierta());
        return torneoRepository.save(existing);
    }

    @Override
    public void deleteTorneo(long id) {
        torneoRepository.findById(id).orElseThrow(() -> new TorneoNotFoundException(id));
        torneoRepository.deleteById(id);
    }
}
