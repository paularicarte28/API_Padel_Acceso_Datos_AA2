package com.padelzgz.api.controller;

import com.padelzgz.api.exception.ClubNotFoundException;
import com.padelzgz.api.model.Club;
import com.padelzgz.api.service.ClubService;
import com.padelzgz.api.service.PistaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubControllerTest {

    @Mock private ClubService clubService;
    @Mock private PistaService pistaService;
    @InjectMocks private ClubController clubController;

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
    @DisplayName("GET /clubs devuelve 200 con lista de clubs")
    void getClubs_returns200() {
        when(clubService.findAll()).thenReturn(Set.of(club));
        ResponseEntity<?> response = clubController.getClubs(null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /clubs/{id} devuelve 200 cuando existe")
    void getClub_whenExists_returns200() {
        when(clubService.findById(1L)).thenReturn(Optional.of(club));
        ResponseEntity<Club> response = clubController.getClub(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Club Pádel Zaragoza", response.getBody().getNombre());
    }

    @Test
    @DisplayName("GET /clubs/{id} lanza excepción cuando no existe")
    void getClub_whenNotExists_throws404() {
        when(clubService.findById(99L)).thenThrow(new ClubNotFoundException(99L));
        assertThrows(ClubNotFoundException.class, () -> clubController.getClub(99L));
    }

    @Test
    @DisplayName("POST /clubs devuelve 201")
    void addClub_returns201() {
        when(clubService.addClub(any(Club.class))).thenReturn(club);
        ResponseEntity<Club> response = clubController.addClub(club);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT /clubs/{id} devuelve 200")
    void modifyClub_returns200() {
        when(clubService.modifyClub(eq(1L), any(Club.class))).thenReturn(club);
        ResponseEntity<Club> response = clubController.modifyClub(1L, club);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT /clubs/{id} lanza excepción cuando no existe")
    void modifyClub_whenNotExists_throws404() {
        when(clubService.modifyClub(eq(99L), any(Club.class))).thenThrow(new ClubNotFoundException(99L));
        assertThrows(ClubNotFoundException.class, () -> clubController.modifyClub(99L, club));
    }

    @Test
    @DisplayName("PATCH /clubs/{id} devuelve 200")
    void patchClub_returns200() {
        when(clubService.patchClub(eq(1L), any(Club.class))).thenReturn(club);
        ResponseEntity<Club> response = clubController.patchClub(1L, club);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /clubs/{id} devuelve 204")
    void deleteClub_returns204() {
        doNothing().when(clubService).deleteClub(1L);
        ResponseEntity<Void> response = clubController.deleteClub(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /clubs/{id} lanza excepción cuando no existe")
    void deleteClub_whenNotExists_throws404() {
        doThrow(new ClubNotFoundException(99L)).when(clubService).deleteClub(99L);
        assertThrows(ClubNotFoundException.class, () -> clubController.deleteClub(99L));
    }
}