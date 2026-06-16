package com.padelzgz.api.service;

import com.padelzgz.api.model.Club;
import java.util.Optional;
import java.util.Set;

public interface ClubService {
    Set<Club> findAll();
    Set<Club> findByCiudad(String ciudad);
    Set<Club> findByActivo(boolean activo);
    Set<Club> findByCiudadAndActivo(String ciudad, boolean activo);
    Optional<Club> findById(long id);
    Club addClub(Club club);
    Club modifyClub(long id, Club club);
    Club patchClub(long id, Club partialClub);
    void deleteClub(long id);
    Set<Club> findClubesConMasPistasActivas();
}
