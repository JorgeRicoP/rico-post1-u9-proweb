package com.universidad.seguridad.security;

import com.universidad.seguridad.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    private final Long id;
    private final String nombre;
    private final String email;
    private final String contrasenia;
    private final boolean activo;
    private final List<GrantedAuthority> authorities;

    public UsuarioPrincipal(Usuario u) {
        this.id = u.getId();
        this.nombre = u.getNombre();
        this.email = u.getEmail();
        this.contrasenia = u.getContrasenia();
        this.activo = u.isActivo();
        this.authorities = List.of(new SimpleGrantedAuthority(u.getRol()));
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return contrasenia; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return activo; }
}