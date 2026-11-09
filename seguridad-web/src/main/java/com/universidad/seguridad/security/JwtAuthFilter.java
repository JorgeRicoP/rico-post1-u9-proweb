package com.universidad.seguridad.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

// SIN @Component: lo registra únicamente apiFilterChain (/api/**),
// para que no se ejecute en las rutas web de sesión.
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.esValido(token)) {
                String email = jwtUtil.extraerEmail(token);
                String rol = jwtUtil.extraerRol(token);

                var authToken = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(new SimpleGrantedAuthority(rol)));

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
            // Token inválido, alterado o expirado: no se autentica y la cadena
            // responde 401 (entry point de apiFilterChain).
        }

        chain.doFilter(request, response);
    }
}