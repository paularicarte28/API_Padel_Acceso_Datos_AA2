package com.padelzgz.api.repository;

import com.padelzgz.api.model.Pista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PistaRepository extends JpaRepository<Pista, Long> {

    Set<Pista> findByTipo(String tipo);
    Set<Pista> findByInterior(boolean interior);
    Set<Pista> findByActiva(boolean activa);
    Set<Pista> findByTipoAndInteriorAndActiva(String tipo, boolean interior, boolean activa);
    Set<Pista> findByClubId(long clubId);

    // SQL nativa: pistas activas con precio en rango
    @Query(value = "SELECT * FROM pistas WHERE precio_hora BETWEEN :precioMin AND :precioMax AND activa = true",
            nativeQuery = true)
    Set<Pista> findByPrecioHoraBetween(@Param("precioMin") float precioMin, @Param("precioMax") float precioMax);

    // SQL nativa: IDs de pistas con valoración media >= minPuntuacion, luego se resuelven por JPA
    @Query(value = "SELECT p.id FROM pistas p " +
            "INNER JOIN valoraciones v ON v.pista_id = p.id " +
            "GROUP BY p.id " +
            "HAVING AVG(v.puntuacion) >= :minPuntuacion",
            nativeQuery = true)
    Set<Long> findIdsByPuntuacionMediaMinima(@Param("minPuntuacion") float minPuntuacion);

    long countByClubId(long clubId);

    long countByClubIdAndActiva(long clubId, boolean activa);
}