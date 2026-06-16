package com.padelzgz.api.service;

import com.padelzgz.api.exception.TorneoNotFoundException;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.impl.TorneoServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock private TorneoRepository torneoRepository;
    @Mock private ClubRepository clubRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private TorneoServiceImpl torneoService;

    private Torneo torneo;
    private Club club;

    @BeforeEach
    void setUp() {
        club = new Club();
        club.setId(1L);
        club.setNombre("Club Test");

        torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Torneo Verano ZGZ");
        torneo.setFechaInicio(LocalDate.of(2025, 7, 1));
        torneo.setMaxParticipantes(16);
        torneo.setInscripcionAbierta(true);
        torneo.setClub(club);
    }

    @Test
    @DisplayName("findAll devuelve todos los torneos")
    void findAll_returnsAll() {
        when(torneoRepository.findAll()).thenReturn(List.of(torneo));
        assertEquals(1, torneoService.findAll().size());
    }

    @Test
    @DisplayName("addTorneo asigna el club y guarda")
    void addTorneo_assignsClubAndSaves() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(torneoRepository.save(torneo)).thenReturn(torneo);

        Torneo result = torneoService.addTorneo(torneo, 1L);

        assertEquals(club, result.getClub());
        verify(torneoRepository).save(torneo);
    }

    @Test
    @DisplayName("addTorneo lanza excepción si el club no existe")
    void addTorneo_whenClubNotFound_throwsException() {
        when(clubRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> torneoService.addTorneo(torneo, 99L));
    }

    @Test
    @DisplayName("deleteTorneo lanza excepción cuando no existe")
    void deleteTorneo_whenNotExists_throwsException() {
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TorneoNotFoundException.class, () -> torneoService.deleteTorneo(99L));
    }

    @Test
    @DisplayName("findByInscripcionAbierta filtra correctamente")
    void findByInscripcionAbierta_filtersCorrectly() {
        when(torneoRepository.findByInscripcionAbierta(true)).thenReturn(Set.of(torneo));
        assertEquals(1, torneoService.findByInscripcionAbierta(true).size());
    }

    @Test
    @DisplayName("patchTorneo actualiza solo campos no nulos")
    void patchTorneo_updatesOnlyNonNullFields() {
        Torneo partial = new Torneo();
        partial.setNombre("Torneo Otoño");

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(torneoRepository.save(torneo)).thenReturn(torneo);

        torneoService.patchTorneo(1L, partial);

        assertEquals("Torneo Otoño", torneo.getNombre());
        verify(torneoRepository).save(torneo);
    }
}
