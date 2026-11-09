package com.universidad.seguridad.controller;

import com.universidad.seguridad.service.TareaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class ApiTareaController {

    private final TareaService service;

    public ApiTareaController(TareaService service) {
        this.service = service;
    }

    // DTO: evita exponer al propietario (y su hash BCrypt) en el JSON
    public record TareaDto(Long id, String titulo, String descripcion, boolean completada) {}

    @GetMapping
    public List<TareaDto> misTareas(Authentication auth) {
        // auth viene poblado por JwtAuthFilter; auth.getName() = email del token
        return service.listarPorEmail(auth.getName()).stream()
                .map(t -> new TareaDto(t.getId(), t.getTitulo(), t.getDescripcion(), t.isCompletada()))
                .toList();
    }
}