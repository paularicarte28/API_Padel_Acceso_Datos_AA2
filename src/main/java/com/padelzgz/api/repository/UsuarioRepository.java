package com.padelzgz.api.repository;

import com.padelzgz.api.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Set<Usuario> findByNivel(String nivel);
    Set<Usuario> findByNombreContainingIgnoreCase(String nombre);
    Set<Usuario> findByNivelAndNombreContainingIgnoreCase(String nivel, String nombre);

    // SQL nativa: Usuarios con más reservas
    
    @Query(value = "SELECT u.* FROM usuarios u " +
        "INNER JOIN reservas r ON r.usuario_id = u.id " +
        "GROUP BY u.id " +
        "ORDER BY COUNT(r.id) DESC",
        nativeQuery = true)
    Set<Usuario> findUsuariosConMasReservas();
}
