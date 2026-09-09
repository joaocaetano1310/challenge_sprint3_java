package com.futurevet.web.api;

import com.futurevet.repository.UsuarioRepository;
import com.futurevet.security.JwtService;
import com.futurevet.service.RegraException;
import com.futurevet.service.UsuarioService;
import com.futurevet.web.api.dto.LoginRequest;
import com.futurevet.web.api.dto.TokenResponse;
import com.futurevet.web.forms.RegistroForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarios;
    private final UsuarioService usuarioService;
    private final JwtService jwt;

    public AuthApiController(AuthenticationManager authManager, UsuarioRepository usuarios,
            UsuarioService usuarioService, JwtService jwt) {
        this.authManager = authManager;
        this.usuarios = usuarios;
        this.usuarioService = usuarioService;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest body) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.email().strip(), body.senha()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }
        var u = usuarios.email(body.email().strip())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));
        return new TokenResponse(jwt.gerar(u.email(), u.role()), u.id(), u.nome(), u.email(), u.role());
    }

    @PostMapping("/registro")
    public ResponseEntity<TokenResponse> registrar(@Valid @RequestBody RegistroForm form) {
        usuarioService.registrar(form);
        var u = usuarios.email(form.getEmail().strip().toLowerCase())
                .orElseThrow(() -> new RegraException("Falha ao localizar usuário recém-criado."));
        return ResponseEntity.status(201)
                .body(new TokenResponse(jwt.gerar(u.email(), u.role()), u.id(), u.nome(), u.email(), u.role()));
    }
}