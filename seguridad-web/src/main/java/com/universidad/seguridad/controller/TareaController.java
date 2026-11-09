package com.universidad.seguridad.controller;

import com.universidad.seguridad.model.Tarea;
import com.universidad.seguridad.security.UsuarioPrincipal;
import com.universidad.seguridad.service.TareaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService service;

    public TareaController(TareaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @AuthenticationPrincipal UsuarioPrincipal principal,
                         Model model) {
        boolean esAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<Tarea> tareas;
        if (q != null && !q.isBlank()) {
            tareas = service.buscar(q).stream()
                    .filter(t -> esAdmin || t.getPropietario().getId().equals(principal.getId()))
                    .toList();
        } else {
            tareas = service.listarDe(principal.getId());
        }
        model.addAttribute("tareas", tareas);
        model.addAttribute("q", q);
        return "tareas/lista";
    }

    @PostMapping
    public String crear(@RequestParam String titulo,
                        @RequestParam(required = false) String descripcion,
                        @AuthenticationPrincipal UsuarioPrincipal principal) {
        if (!titulo.isBlank()) {
            service.crear(titulo.trim(), descripcion, principal.getId());
        }
        return "redirect:/tareas";
    }
}