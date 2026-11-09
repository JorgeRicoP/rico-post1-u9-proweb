package com.universidad.seguridad.service;

import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public void registrar(Usuario usuario) {
        if (repo.existsByEmail(usuario.getEmail()))
            throw new RuntimeException("El correo ya está registrado");
        usuario.setContrasenia(encoder.encode(usuario.getContrasenia()));
        usuario.setRol("ROLE_USER"); // siempre USER, ignora lo que venga del formulario
        repo.save(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarTodos() { return repo.findAll(); }
}