package com.futurevet.service;

import com.futurevet.model.Vacina;
import com.futurevet.repository.VacinaRepository;
import com.futurevet.security.Acesso;
import com.futurevet.web.forms.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
public class CarteiraService {
    public record Item(Vacina vacina, String situacao, Long dias) {
        public String descricao() {
            return switch (situacao) {
            case "ATRASADA" -> "Atrasada";
            case "PROXIMA" -> "Próxima dose";
            case "EM_DIA" -> "Em dia";
            case "HISTORICO" -> "Histórico";
            default -> "Sem previsão";
            };
        }
    }

    private final VacinaRepository repo;
    private final AnimalService animais;
    private final Acesso acesso;
    private final Clock clock;

    public CarteiraService(VacinaRepository repo, AnimalService animais, Acesso acesso, Clock clock) {
        this.repo = repo;
        this.animais = animais;
        this.acesso = acesso;
        this.clock = clock;
    }

    public List<Item> listar() {
        return repo.listar(acesso.atual()).stream().map(this::classificar).toList();
    }

    public Item classificar(Vacina v) {
        if (v.substituida())
            return new Item(v, "HISTORICO", null);
        if (v.proximaDose() == null)
            return new Item(v, "SEM_PREVISAO", null);
        long dias = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(clock), v.proximaDose());
        return new Item(v, dias < 0 ? "ATRASADA" : dias <= 30 ? "PROXIMA" : "EM_DIA", dias);
    }

    public Vacina buscar(long id) {
        var v = repo.buscar(id).orElseThrow(() -> new RegraException("Vacina não encontrada."));
        acesso.dono(v.usuarioId());
        return v;
    }

    private void validar(VacinaForm f, long id) {
        animais.buscar(f.getAnimalId());
        if (f.getData().isAfter(LocalDate.now(clock)))
            throw new RegraException("A aplicação não pode estar no futuro.");
        if (f.getProximaDose() != null && !f.getProximaDose().isAfter(f.getData()))
            throw new RegraException("A próxima dose deve ser posterior à aplicação.");
        if (repo.duplicada(f, id))
            throw new RegraException("Esta vacina já está registrada para o animal nesta data.");
    }

    @Transactional
    public long salvar(Long id, VacinaForm f) {
        if (id != null) {
            var v = buscar(id);
            if (v.substituida() || v.origemId() != null)
                throw new RegraException("Doses vinculadas a reforços não podem ser editadas.");
            f.setAnimalId(v.animalId());
        }
        validar(f, id == null ? -1 : id);
        if (id == null)
            return repo.criar(f, null);
        repo.atualizar(id, f);
        return id;
    }

    @Transactional
    public long reforcar(long id, ReforcoForm f) {
        buscar(id);
        repo.bloquear(id);
        var v = buscar(id);
        if (v.substituida())
            throw new RegraException("Esta dose já possui um reforço registrado.");
        if (!f.getData().isAfter(v.data()))
            throw new RegraException("O reforço deve ser posterior à dose anterior.");
        VacinaForm dose = new VacinaForm();
        dose.setAnimalId(v.animalId());
        dose.setNome(v.nome());
        dose.setData(f.getData());
        dose.setProximaDose(f.getData().plusMonths(f.getIntervalo()));
        dose.setLocal(f.getLocal());
        validar(dose, -1);
        return repo.criar(dose, id);
    }

    @Transactional
    public void excluir(long id) {
        var v = buscar(id);
        if (v.substituida() || v.origemId() != null)
            throw new RegraException("Doses vinculadas a reforços não podem ser excluídas.");
        repo.excluir(id);
    }
}
