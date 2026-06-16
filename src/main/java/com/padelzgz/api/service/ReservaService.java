package com.padelzgz.api.service;

import com.padelzgz.api.dto.ReservaInDTO;
import com.padelzgz.api.model.Reserva;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface ReservaService {
    Set<Reserva> findAll();
    Set<Reserva> findByFecha(LocalDate fecha);
    Set<Reserva> findByPagado(boolean pagado);
    Set<Reserva> findByFechaAndPagado(LocalDate fecha, boolean pagado);
    Set<Reserva> findByPistaId(long pistaId);
    Set<Reserva> findByUsuarioId(long usuarioId);
    Optional<Reserva> findById(long id);
    Reserva addReserva(ReservaInDTO dto);
    Reserva modifyReserva(long id, ReservaInDTO dto);
    Reserva patchReserva(long id, Reserva partial);
    void deleteReserva(long id);
}
