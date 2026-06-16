package com.padelzgz.api.repository;

import com.padelzgz.api.model.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Set<Reserva> findByFecha(LocalDate fecha);
    Set<Reserva> findByPagado(boolean pagado);
    Set<Reserva> findByPistaId(long pistaId);
    Set<Reserva> findByUsuarioId(long usuarioId);
    Set<Reserva> findByFechaAndPagado(LocalDate fecha, boolean pagado);
    Set<Reserva> findByFechaAndPistaId(LocalDate fecha, long pistaId);
}
