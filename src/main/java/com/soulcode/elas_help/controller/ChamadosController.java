package com.soulcode.elas_help.controller;

import com.soulcode.elas_help.domain.Tecnico;
import com.soulcode.elas_help.domain.Usuario;
import com.soulcode.elas_help.model.ChamadosDTO;
import com.soulcode.elas_help.repos.TecnicoRepository;
import com.soulcode.elas_help.repos.UsuarioRepository;
import com.soulcode.elas_help.service.ChamadosService;
import com.soulcode.elas_help.util.CustomCollectors;
import com.soulcode.elas_help.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/chamadoss")
public class ChamadosController {

    private final ChamadosService chamadosService;
    private final TecnicoRepository tecnicoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadosController(final ChamadosService chamadosService,
            final TecnicoRepository tecnicoRepository, final UsuarioRepository usuarioRepository) {
        this.chamadosService = chamadosService;
        this.tecnicoRepository = tecnicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("tecnicoValues", tecnicoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Tecnico::getId, Tecnico::getNome)));
        model.addAttribute("usersValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("chamadoses", chamadosService.findAll());
        return "chamados/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("chamados") final ChamadosDTO chamadosDTO) {
        return "chamados/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("chamados") @Valid final ChamadosDTO chamadosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamados/add";
        }
        chamadosService.create(chamadosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("chamados.create.success"));
        return "redirect:/chamadoss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("chamados", chamadosService.get(id));
        return "chamados/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("chamados") @Valid final ChamadosDTO chamadosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamados/edit";
        }
        chamadosService.update(id, chamadosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("chamados.update.success"));
        return "redirect:/chamadoss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        chamadosService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("chamados.delete.success"));
        return "redirect:/chamadoss";
    }

}
