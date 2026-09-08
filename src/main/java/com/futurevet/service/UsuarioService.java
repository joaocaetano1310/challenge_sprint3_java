package com.futurevet.service;

import com.futurevet.repository.UsuarioRepository;
import com.futurevet.web.forms.RegistroForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public long registrar(RegistroForm f) {
        if (!f.getSenha().equals(f.getConfirmacao()))
            throw new RegraException("As senhas não conferem.");
        if (f.getSenha().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
            throw new RegraException("A senha deve ter até 72 bytes em UTF-8.");
        String cpf = f.getCpf().replaceAll("[^0-9]", "");
        String tel = f.getTelefone() == null ? "" : f.getTelefone().replaceAll("[^0-9]", "");
        if (cpf.length() != 11)
            throw new RegraException("Informe os 11 dígitos do CPF.");
        if (!tel.isEmpty() && tel.length() != 10 && tel.length() != 11)
            throw new RegraException("Telefone deve ter 10 ou 11 dígitos com DDD.");
        String email = f.getEmail().strip().toLowerCase(java.util.Locale.ROOT);
        if (repo.duplicado(email, Long.parseLong(cpf)))
            throw new RegraException("E-mail ou CPF já cadastrado.");
        return repo.criar(f.getNome().strip(), email, encoder.encode(f.getSenha()), Long.parseLong(cpf),
                tel.isEmpty() ? null : Long.valueOf(tel), "TUTOR");
    }
}
