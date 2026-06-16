package com.padelzgz.api.service;

import com.padelzgz.api.exception.UsuarioNotFoundException;
import com.padelzgz.api.model.Usuario;
import com.padelzgz.api.repository.UsuarioRepository;
import com.padelzgz.api.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Pau");
        usuario.setApellidos("Ricarte");
        usuario.setEmail("pau@padelzgz.com");
        usuario.setNivel("AVANZADO");
        usuario.setPassword("pass123");
        usuario.setFechaRegistro(LocalDate.now());
    }

    @Test
    @DisplayName("findAll devuelve todos los usuarios")
    void findAll_returnsAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        assertEquals(1, usuarioService.findAll().size());
    }

    @Test
    @DisplayName("findById devuelve usuario cuando existe")
    void findById_whenExists_returnsUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        assertTrue(usuarioService.findById(1L).isPresent());
    }

    @Test
    @DisplayName("addUsuario codifica el password y setea fecha de registro")
    void addUsuario_encodesPasswordAndSetsDate() {
        when(passwordEncoder.encode("pass123")).thenReturn("encoded");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.addUsuario(usuario);

        assertEquals("encoded", usuario.getPassword());
        assertNotNull(usuario.getFechaRegistro());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("deleteUsuario lanza excepción cuando no existe")
    void deleteUsuario_whenNotExists_throwsException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.deleteUsuario(99L));
    }

    @Test
    @DisplayName("findByNivel filtra por nivel")
    void findByNivel_filtersByNivel() {
        when(usuarioRepository.findByNivel("AVANZADO")).thenReturn(Set.of(usuario));
        assertEquals(1, usuarioService.findByNivel("AVANZADO").size());
    }

    @Test
    @DisplayName("findByEmail devuelve usuario cuando existe")
    void findByEmail_whenExists_returnsUsuario() {
        when(usuarioRepository.findByEmail("pau@padelzgz.com")).thenReturn(Optional.of(usuario));
        assertTrue(usuarioService.findByEmail("pau@padelzgz.com").isPresent());
    }

    @Test
    @DisplayName("patchUsuario actualiza solo los campos no nulos")
    void patchUsuario_updatesOnlyNonNullFields() {
        Usuario partial = new Usuario();
        partial.setNivel("COMPETICION");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.patchUsuario(1L, partial);

        assertEquals("COMPETICION", usuario.getNivel());
        assertEquals("Pau", usuario.getNombre()); // no cambió
    }
}
