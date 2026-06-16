package com.padelzgz.api.service;

import com.padelzgz.api.exception.ClubNotFoundException;
import com.padelzgz.api.model.Club;
import com.padelzgz.api.repository.ClubRepository;
import com.padelzgz.api.service.impl.ClubServiceImpl;
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
class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClubServiceImpl clubService;

    private Club club;

    @BeforeEach
    void setUp() {
        club = new Club();
        club.setId(1L);
        club.setNombre("Club Pádel Zaragoza");
        club.setCiudad("Zaragoza");
        club.setActivo(true);
        club.setFechaApertura(LocalDate.of(2020, 1, 15));
    }

    @Test
    @DisplayName("findAll devuelve todos los clubs")
    void findAll_returnsAllClubs() {
        when(clubRepository.findAll()).thenReturn(List.of(club));
        Set<Club> result = clubService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clubRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById devuelve el club cuando existe")
    void findById_whenExists_returnsClub() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        Optional<Club> result = clubService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Club Pádel Zaragoza", result.get().getNombre());
    }

    @Test
    @DisplayName("findById devuelve vacío cuando no existe")
    void findById_whenNotExists_returnsEmpty() {
        when(clubRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(clubService.findById(99L).isPresent());
    }

    @Test
    @DisplayName("addClub guarda y devuelve el club")
    void addClub_savesAndReturns() {
        when(clubRepository.save(club)).thenReturn(club);
        Club result = clubService.addClub(club);
        assertNotNull(result);
        assertEquals("Club Pádel Zaragoza", result.getNombre());
        verify(clubRepository).save(club);
    }

    @Test
    @DisplayName("modifyClub actualiza cuando el club existe")
    void modifyClub_whenExists_updates() {
        Club updated = new Club();
        updated.setNombre("Club Nuevo");
        updated.setCiudad("Zaragoza");

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        doNothing().when(modelMapper).map(updated, club);
        when(clubRepository.save(club)).thenReturn(club);

        Club result = clubService.modifyClub(1L, updated);
        assertNotNull(result);
        verify(clubRepository).save(club);
    }

    @Test
    @DisplayName("modifyClub lanza excepción cuando el club no existe")
    void modifyClub_whenNotExists_throwsException() {
        when(clubRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ClubNotFoundException.class, () -> clubService.modifyClub(99L, club));
    }

    @Test
    @DisplayName("patchClub actualiza solo los campos no nulos")
    void patchClub_updatesOnlyNonNullFields() {
        Club partial = new Club();
        partial.setNombre("Nombre Actualizado");

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(clubRepository.save(club)).thenReturn(club);

        clubService.patchClub(1L, partial);
        assertEquals("Nombre Actualizado", club.getNombre());
        verify(clubRepository).save(club);
    }

    @Test
    @DisplayName("deleteClub elimina cuando el club existe")
    void deleteClub_whenExists_deletes() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        doNothing().when(clubRepository).deleteById(1L);
        assertDoesNotThrow(() -> clubService.deleteClub(1L));
        verify(clubRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteClub lanza excepción cuando el club no existe")
    void deleteClub_whenNotExists_throwsException() {
        when(clubRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ClubNotFoundException.class, () -> clubService.deleteClub(99L));
        verify(clubRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("findByCiudad filtra por ciudad correctamente")
    void findByCiudad_filtersByCiudad() {
        when(clubRepository.findByCiudad("Zaragoza")).thenReturn(Set.of(club));
        Set<Club> result = clubService.findByCiudad("Zaragoza");
        assertEquals(1, result.size());
    }
}
