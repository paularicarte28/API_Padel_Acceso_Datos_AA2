package com.padelzgz.api.service.impl;

import com.padelzgz.api.dto.ReservaInDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private PistaRepository pistaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @Override
    public Set<Reserva> findAll() { return reservaRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Reserva> findByFecha(LocalDate fecha) { return reservaRepository.findByFecha(fecha); }

    @Override
    public Set<Reserva> findByPagado(boolean pagado) { return reservaRepository.findByPagado(pagado); }

    @Override
    public Set<Reserva> findByFechaAndPagado(LocalDate fecha, boolean pagado) {
        return reservaRepository.findByFechaAndPagado(fecha, pagado);
    }

    @Override
    public Set<Reserva> findByPistaId(long pistaId) { return reservaRepository.findByPistaId(pistaId); }

    @Override
    public Set<Reserva> findByUsuarioId(long usuarioId) { return reservaRepository.findByUsuarioId(usuarioId); }

    @Override
    public Optional<Reserva> findById(long id) { return reservaRepository.findById(id); }

    @Override
    public Reserva addReserva(ReservaInDTO dto) {
        Pista pista = pistaRepository.findById(dto.getPistaId())
                .orElseThrow(() -> new PistaNotFoundException(dto.getPistaId()));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.getUsuarioId()));

        Reserva reserva = new Reserva();
        reserva.setFecha(dto.getFecha());
        reserva.setHoraInicio(LocalTime.parse(dto.getHoraInicio()));
        reserva.setHoraFin(LocalTime.parse(dto.getHoraFin()));
        reserva.setPrecio(dto.getPrecio() > 0 ? dto.getPrecio() : pista.getPrecioHora());
        reserva.setPagado(dto.isPagado());
        reserva.setComentario(dto.getComentario());
        reserva.setPista(pista);
        reserva.setUsuario(usuario);

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva modifyReserva(long id, ReservaInDTO dto) {
        Reserva existing = reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException(id));
        Pista pista = pistaRepository.findById(dto.getPistaId())
                .orElseThrow(() -> new PistaNotFoundException(dto.getPistaId()));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.getUsuarioId()));

        existing.setFecha(dto.getFecha());
        existing.setHoraInicio(LocalTime.parse(dto.getHoraInicio()));
        existing.setHoraFin(LocalTime.parse(dto.getHoraFin()));
        existing.setPrecio(dto.getPrecio());
        existing.setPagado(dto.isPagado());
        existing.setComentario(dto.getComentario());
        existing.setPista(pista);
        existing.setUsuario(usuario);

        return reservaRepository.save(existing);
    }

    @Override
    public Reserva patchReserva(long id, Reserva partial) {
        Reserva existing = reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException(id));
        if (partial.getComentario() != null) existing.setComentario(partial.getComentario());
        if (partial.getPrecio() > 0) existing.setPrecio(partial.getPrecio());
        existing.setPagado(partial.isPagado());
        return reservaRepository.save(existing);
    }

    @Override
    public void deleteReserva(long id) {
        reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException(id));
        reservaRepository.deleteById(id);
    }
}
