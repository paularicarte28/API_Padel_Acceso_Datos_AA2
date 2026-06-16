package com.padelzgz.api.service;

import com.padelzgz.api.model.Usuario;
import java.util.Optional;
import java.util.Set;

public interface UsuarioService {
    Set<Usuario> findAll();
    Set<Usuario> findByNivel(String nivel);
    Set<Usuario> findByNombre(String nombre);
    Set<Usuario> findByNivelAndNombre(String nivel, String nombre);
    Optional<Usuario> findById(long id);
    Optional<Usuario> findByEmail(String email);
    Usuario addUsuario(Usuario usuario);
    Usuario modifyUsuario(long id, Usuario usuario);
    Usuario patchUsuario(long id, Usuario partial);
    void deleteUsuario(long id);
    Set<Usuario> findUsuariosConMasReservas(int limite);
}
