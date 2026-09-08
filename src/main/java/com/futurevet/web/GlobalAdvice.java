package com.futurevet.web;

import com.futurevet.security.Acesso;
import com.futurevet.service.RegraException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.dao.DataIntegrityViolationException;

@ControllerAdvice
public class GlobalAdvice {
    private final Acesso acesso;

    public GlobalAdvice(Acesso acesso) {
        this.acesso = acesso;
    }

    @InitBinder
    public void binder(WebDataBinder b) {
        b.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        for (String campo : new String[] { "senha", "confirmacao" }) {
            b.registerCustomEditor(String.class, campo, new java.beans.PropertyEditorSupport() {
                @Override
                public void setAsText(String text) {
                    setValue(text);
                }
            });
        }
        b.setDisallowedFields("role", "senhaHash", "usuarioId");
    }

    @ModelAttribute
    public void usuario(Model m, java.security.Principal p) {
        if (p != null)
            m.addAttribute("usuario", acesso.atual());
    }

    @ExceptionHandler(RegraException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String regra(RegraException e, Model m) {
        m.addAttribute("mensagem", e.getMessage());
        return "erro";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String negado(Model m) {
        m.addAttribute("mensagem", "Você não tem permissão para acessar este registro.");
        return "erro";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String conflito(Model m) {
        m.addAttribute("mensagem",
                "Não foi possível salvar: há um registro duplicado ou dados vinculados. Confira as informações e tente novamente.");
        return "erro";
    }
}
