package com.padelzgz.api.service.impl;

import com.padelzgz.api.exception.ClubNotFoundException;
import com.padelzgz.api.model.Club;
import com.padelzgz.api.repository.ClubRepository;
import com.padelzgz.api.service.ClubService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired private ClubRepository clubRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Set<Club> findAll() { return clubRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Club> findByCiudad(String ciudad) { return clubRepository.findByCiudad(ciudad); }

    @Override
    public Set<Club> findByActivo(boolean activo) { return clubRepository.findByActivo(activo); }

    @Override
    public Set<Club> findByCiudadAndActivo(String ciudad, boolean activo) {
        return clubRepository.findByCiudadAndActivo(ciudad, activo);
    }

    @Override
    public Optional<Club> findById(long id) { return clubRepository.findById(id); }

    @Override
    public Club addClub(Club club) { return clubRepository.save(club); }

    @Override
    public Club modifyClub(long id, Club newClub) {
        Club existing = clubRepository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
        modelMapper.map(newClub, existing);
        existing.setId(id);
        return clubRepository.save(existing);
    }

    @Override
    public Club patchClub(long id, Club partialClub) {
        Club existing = clubRepository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
        if (partialClub.getNombre() != null) existing.setNombre(partialClub.getNombre());
        if (partialClub.getCiudad() != null) existing.setCiudad(partialClub.getCiudad());
        if (partialClub.getDireccion() != null) existing.setDireccion(partialClub.getDireccion());
        if (partialClub.getTelefono() != null) existing.setTelefono(partialClub.getTelefono());
        if (partialClub.getEmail() != null) existing.setEmail(partialClub.getEmail());
        if (partialClub.getFechaApertura() != null) existing.setFechaApertura(partialClub.getFechaApertura());
        existing.setActivo(partialClub.isActivo());
        return clubRepository.save(existing);
    }

    @Override
    public void deleteClub(long id) {
        clubRepository.findById(id).orElseThrow(() -> new ClubNotFoundException(id));
        clubRepository.deleteById(id);
    }

    @Override
    public Set<Club> findClubesConMasPistasActivas() {
        return clubRepository.findClubesConMasPistasActivas();
    }
}
