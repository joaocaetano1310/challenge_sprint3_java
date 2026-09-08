package com.futurevet.service;

import com.futurevet.model.Animal;
import com.futurevet.repository.AnimalRepository;
import com.futurevet.security.Acesso;
import com.futurevet.web.forms.AnimalForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnimalService {
    private final AnimalRepository repo;
    private final Acesso acesso;

    public AnimalService(AnimalRepository repo, Acesso acesso) {
        this.repo = repo;
        this.acesso = acesso;
    }

    public List<Animal> listar() {
        return repo.listar(acesso.atual());
    }

    public Animal buscar(long id) {
        var a = repo.buscar(id).orElseThrow(() -> new RegraException("Animal não encontrado."));
        acesso.dono(a.usuarioId());
        return a;
    }

    @Transactional
    public long salvar(Long id, AnimalForm form) {
        if (id == null)
            return repo.criar(form, acesso.atual().id());
        buscar(id);
        repo.atualizar(id, form);
        return id;
    }

    @Transactional
    public void excluir(long id) {
        buscar(id);
        if (repo.temHistorico(id))
            throw new RegraException(
                    "Este animal possui histórico de consultas ou vacinas. Preserve o histórico; a exclusão foi bloqueada.");
        repo.excluir(id);
    }
}
