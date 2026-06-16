package com.padelzgz.api.service;

import com.padelzgz.api.exception.PistaNotFoundException;
import com.padelzgz.api.model.*;
import com.padelzgz.api.repository.*;
import com.padelzgz.api.service.impl.PistaServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PistaServiceTest {

    @Mock private PistaRepository pistaRepository;
    @Mock private ClubRepository clubRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private PistaServiceImpl pistaService;

    private Pista pista;
    private Club club;

    @BeforeEach
    void setUp() {
        club = new Club();
        club.setId(1L);
        club.setNombre("Club Test");
        club.setCiudad("Zaragoza");

        pista = new Pista();
        pista.setId(1L);
        pista.setNumero(1);
        pista.setTipo("CRISTAL");
        pista.setInterior(true);
        pista.setPrecioHora(12.5f);
        pista.setActiva(true);
        pista.setClub(club);
    }

    @Test
    @DisplayName("findAll devuelve todas las pistas")
    void findAll_returnsAll() {
        when(pistaRepository.findAll()).thenReturn(List.of(pista));
        assertEquals(1, pistaService.findAll().size());
    }

    @Test
    @DisplayName("findById devuelve pista cuando existe")
    void findById_whenExists_returnsPista() {
        when(pistaRepository.findById(1L)).thenReturn(Optional.of(pista));
        assertTrue(pistaService.findById(1L).isPresent());
    }

    @Test
    @DisplayName("addPista asigna el club y guarda")
    void addPista_assignsClubAndSaves() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(pistaRepository.save(pista)).thenReturn(pista);

        Pista result = pistaService.addPista(pista, 1L);

        assertNotNull(result);
        assertEquals(club, result.getClub());
        verify(pistaRepository).save(pista);
    }

    @Test
    @DisplayName("deletePista lanza excepción cuando no existe")
    void deletePista_whenNotExists_throwsException() {
        when(pistaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PistaNotFoundException.class, () -> pistaService.deletePista(99L));
    }

    @Test
    @DisplayName("findByTipo filtra correctamente")
    void findByTipo_filtersCorrectly() {
        when(pistaRepository.findByTipo("CRISTAL")).thenReturn(Set.of(pista));
        Set<Pista> result = pistaService.findByTipo("CRISTAL");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("patchPista actualiza solo campos no nulos/cero")
    void patchPista_updatesOnlyProvidedFields() {
        Pista partial = new Pista();
        partial.setTipo("PANORAMICA");

        when(pistaRepository.findById(1L)).thenReturn(Optional.of(pista));
        when(pistaRepository.save(pista)).thenReturn(pista);

        pistaService.patchPista(1L, partial);

        assertEquals("PANORAMICA", pista.getTipo());
        verify(pistaRepository).save(pista);
    }
}
