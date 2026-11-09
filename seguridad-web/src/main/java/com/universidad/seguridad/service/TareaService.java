package com.universidad.seguridad.service;

import com.universidad.seguridad.model.Tarea;
import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.TareaRepository;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TareaService {
    private final TareaRepository repo;
    private final UsuarioRepository usuarioRepo;

    public TareaService(TareaRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    @PreAuthorize("hasRole('ADMIN') or #usuarioId == authentication.principal.id")
    public List<Tarea> listarDe(Long usuarioId) {
        return repo.findByPropietarioId(usuarioId);
    }

    public List<Tarea> buscar(String texto) {
        return repo.buscarPorTitulo(texto); // @Query parametrizado: seguro
    }

    @Transactional
    public void crear(String titulo, String descripcion, Long usuarioId) {
        Tarea t = new Tarea();
        t.setTitulo(titulo);
        t.setDescripcion(descripcion);
        t.setPropietario(usuarioRepo.getReferenceById(usuarioId));
        repo.save(t);
    }

    // Para la API JWT: el email viene del token ya validado.
    // Solo devuelve las tareas de ese usuario.
    public List<Tarea> listarPorEmail(String email) {
        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return repo.findByPropietarioId(u.getId());
    }
}