package com.padelzgz.api.service;

import com.padelzgz.api.dto.ReservaInDTO;
import com.padelzgz.api.exception.*;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private PistaRepository pistaRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Pista pista;
    private Usuario usuario;
    private ReservaInDTO dto;

    @BeforeEach
    void setUp() {
        pista = new Pista();
        pista.setId(1L);
        pista.setPrecioHora(12.0f);

        usuario = new Usuario();
        usuario.setId(1L);

        dto = new ReservaInDTO();
        dto.setFecha(LocalDate.now());
        dto.setHoraInicio("10:00");
        dto.setHoraFin("11:00");
        dto.setPistaId(1L);
        dto.setUsuarioId(1L);
    }

    @Test
    @DisplayName("addReserva crea una reserva con los datos del DTO")
    void addReserva_createsFromDto() {
        when(pistaRepository.findById(1L)).thenReturn(Optional.of(pista));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Reserva result = reservaService.addReserva(dto);

        assertNotNull(result);
        assertEquals(pista, result.getPista());
        assertEquals(usuario, result.getUsuario());
        assertEquals(12.0f, result.getPrecio()); // toma el precio de la pista
    }

    @Test
    @DisplayName("addReserva lanza excepción si la pista no existe")
    void addReserva_whenPistaNotFound_throwsException() {
        when(pistaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PistaNotFoundException.class, () -> reservaService.addReserva(dto));
    }

    @Test
    @DisplayName("addReserva lanza excepción si el usuario no existe")
    void addReserva_whenUsuarioNotFound_throwsException() {
        when(pistaRepository.findById(1L)).thenReturn(Optional.of(pista));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNotFoundException.class, () -> reservaService.addReserva(dto));
    }

    @Test
    @DisplayName("findAll devuelve todas las reservas")
    void findAll_returnsAll() {
        when(reservaRepository.findAll()).thenReturn(List.of(new Reserva()));
        assertEquals(1, reservaService.findAll().size());
    }

    @Test
    @DisplayName("deleteReserva lanza excepción cuando no existe")
    void deleteReserva_whenNotExists_throwsException() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ReservaNotFoundException.class, () -> reservaService.deleteReserva(99L));
    }
}
