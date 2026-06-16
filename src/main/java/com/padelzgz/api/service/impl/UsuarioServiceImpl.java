package com.padelzgz.api.service.impl;

import com.padelzgz.api.exception.UsuarioNotFoundException;
import com.padelzgz.api.model.Usuario;
import com.padelzgz.api.repository.UsuarioRepository;
import com.padelzgz.api.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Set<Usuario> findAll() { return usuarioRepository.findAll().stream().collect(java.util.stream.Collectors.toSet()); }

    @Override
    public Set<Usuario> findByNivel(String nivel) { return usuarioRepository.findByNivel(nivel); }

    @Override
    public Set<Usuario> findByNombre(String nombre) { return usuarioRepository.findByNombreContainingIgnoreCase(nombre); }

    @Override
    public Set<Usuario> findByNivelAndNombre(String nivel, String nombre) {
        return usuarioRepository.findByNivelAndNombreContainingIgnoreCase(nivel, nombre);
    }

    @Override
    public Optional<Usuario> findById(long id) { return usuarioRepository.findById(id); }

    @Override
    public Optional<Usuario> findByEmail(String email) { return usuarioRepository.findByEmail(email); }

    @Override
    public Usuario addUsuario(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuario.setFechaRegistro(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario modifyUsuario(long id, Usuario newUsuario) {
        Usuario existing = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        String password = existing.getPassword();
        modelMapper.map(newUsuario, existing);
        existing.setId(id);
        existing.setPassword(password); // no sobreescribir password con PUT
        return usuarioRepository.save(existing);
    }

    @Override
    public Usuario patchUsuario(long id, Usuario partial) {
        Usuario existing = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        if (partial.getNombre() != null) existing.setNombre(partial.getNombre());
        if (partial.getApellidos() != null) existing.setApellidos(partial.getApellidos());
        if (partial.getTelefono() != null) existing.setTelefono(partial.getTelefono());
        if (partial.getNivel() != null) existing.setNivel(partial.getNivel());
        return usuarioRepository.save(existing);
    }

    @Override
    public void deleteUsuario(long id) {
        usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        usuarioRepository.deleteById(id);
    }

   @Override
    public Set<Usuario> findUsuariosConMasReservas(int limite) {
        return usuarioRepository.findUsuariosConMasReservas()
            .stream()
            .limit(limite)
            .collect(java.util.stream.Collectors.toSet());  
    }
}
