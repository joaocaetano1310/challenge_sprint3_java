package com.futurevet.service;

import com.futurevet.model.Consulta;
import com.futurevet.repository.ConsultaRepository;
import com.futurevet.security.Acesso;
import com.futurevet.web.forms.AgendaForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
public class AgendaService {
    private final ConsultaRepository repo;
    private final AnimalService animais;
    private final Acesso acesso;
    private final Clock clock;

    public AgendaService(ConsultaRepository repo, AnimalService animais, Acesso acesso, Clock clock) {
        this.repo = repo;
        this.animais = animais;
        this.acesso = acesso;
        this.clock = clock;
    }

    public List<Consulta> listar() {
        return repo.listar(acesso.atual());
    }

    public Consulta buscar(long id) {
        var c = repo.buscar(id).orElseThrow(() -> new RegraException("Consulta não encontrada."));
        acesso.dono(c.usuarioId());
        return c;
    }

    private void validar(AgendaForm f, long ignorar) {
        animais.buscar(f.getAnimalId());
        if (f.getHora().getSecond() != 0 || f.getHora().getNano() != 0)
            throw new RegraException("Informe o horário em horas e minutos.");
        if (!LocalDateTime.of(f.getData(), f.getHora()).isAfter(LocalDateTime.now(clock)))
            throw new RegraException("Escolha uma data e hora futuras.");
        if (f.getData().getDayOfWeek() == DayOfWeek.SUNDAY || f.getHora().isBefore(LocalTime.of(8, 0))
                || f.getHora().isAfter(LocalTime.of(18, 0)))
            throw new RegraException("Agendamentos de segunda a sábado, das 08:00 às 18:00.");
        if (repo.conflito(f.getAnimalId(), f.getData(), f.getHora().toString(), f.getLocal().strip(),
                ignorar))
            throw new RegraException("Este horário já está ocupado para o animal ou local escolhido.");
    }

    @Transactional
    public long agendar(AgendaForm f) {
        repo.bloquearAgenda();
        validar(f, -1);
        f.setLocal(f.getLocal().strip());
        return repo.criar(f);
    }

    @Transactional
    public void reagendar(long id, AgendaForm f) {
        repo.bloquearAgenda();
        var c = buscar(id);
        if (!c.ativa())
            throw new RegraException("Consulta encerrada não pode ser reagendada.");
        f.setAnimalId(c.animalId());
        validar(f, id);
        f.setLocal(f.getLocal().strip());
        repo.reagendar(id, f);
    }

    @Transactional
    public void confirmar(long id) {
        acesso.admin();
        repo.bloquearAgenda();
        var c = buscar(id);
        if (!c.status().equals("AGENDADA"))
            throw new RegraException("Somente consultas agendadas podem ser confirmadas.");
        if (passada(c))
            throw new RegraException("Não é possível confirmar consulta no passado. Reagende primeiro.");
        repo.status(id, "CONFIRMADA", c.observacao());
    }

    @Transactional
    public void cancelar(long id, String motivo) {
        repo.bloquearAgenda();
        var c = buscar(id);
        if (!c.ativa())
            throw new RegraException("A consulta já foi encerrada.");
        if (motivo == null || motivo.isBlank() || motivo.length() > 255)
            throw new RegraException("Informe o motivo do cancelamento (até 255 caracteres).");
        repo.status(id, "CANCELADA", motivo.strip());
    }

    @Transactional
    public void concluir(long id, String observacao) {
        acesso.admin();
        repo.bloquearAgenda();
        var c = buscar(id);
        if (!c.status().equals("CONFIRMADA"))
            throw new RegraException("Confirme a consulta antes de concluir.");
        if (c.hora() == null || !passada(c))
            throw new RegraException("Só é possível concluir após o horário da consulta.");
        if (observacao == null || observacao.isBlank() || observacao.length() > 255)
            throw new RegraException("Informe um resumo do atendimento (até 255 caracteres).");
        repo.status(id, "REALIZADA", observacao.strip());
    }

    private boolean passada(Consulta c) {
        return c.hora() != null
                && LocalDateTime.of(c.data(), LocalTime.parse(c.hora())).isBefore(LocalDateTime.now(clock));
    }
}
