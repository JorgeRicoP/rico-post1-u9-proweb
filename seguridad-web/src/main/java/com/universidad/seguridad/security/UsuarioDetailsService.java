package com.universidad.seguridad.security;

import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UsuarioPrincipal(u);
    }
}