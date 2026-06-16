package com.padelzgz.api.security;

import com.padelzgz.api.model.Usuario;
import com.padelzgz.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        // Password puede ser null si no se ha configurado; usamos "" para el flujo JWT
        String password = usuario.getPassword() != null ? usuario.getPassword() : "";
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), password, new ArrayList<>());
    }
}
