package com.padelzgz.api.repository;

import com.padelzgz.api.model.Club;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Set<Club> findByCiudad(String ciudad);
    Set<Club> findByActivo(boolean activo);
    Set<Club> findByCiudadAndActivo(String ciudad, boolean activo);

    Optional<Club> findByNombreIgnoreCaseAndCiudadIgnoreCase(String nombre, String ciudad);

    boolean existsByNombreIgnoreCaseAndCiudadIgnoreCase(String nombre, String ciudad);

    // SQL nativa: Clubs con más pistas activas
    @Query(value = "SELECT c.* FROM clubs c " +
            "INNER JOIN pistas p ON p.club_id = c.id " +
            "WHERE p.activa = true " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(p.id) DESC",
            nativeQuery = true)
    Set<Club> findClubesConMasPistasActivas();
}