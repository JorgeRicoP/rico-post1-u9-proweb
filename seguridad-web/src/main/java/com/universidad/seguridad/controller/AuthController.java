package com.universidad.seguridad.controller;

import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.security.UsuarioPrincipal;
import com.universidad.seguridad.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String inicio() { return "redirect:/dashboard"; }

    @GetMapping("/login")
    public String mostrarLogin() { return "auth/login"; }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute Usuario usuario, BindingResult result) {
        if (result.hasErrors()) return "auth/registro";
        try {
            service.registrar(usuario);
            return "redirect:/login?registrado";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.email", e.getMessage());
            return "auth/registro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UsuarioPrincipal principal) {
        model.addAttribute("nombre", principal.getNombre());
        model.addAttribute("email", principal.getUsername());
        model.addAttribute("roles", principal.getAuthorities());
        return "dashboard";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("usuarios", service.listarTodos());
        return "admin/panel";
    }
}