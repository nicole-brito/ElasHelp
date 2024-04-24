package com.soulcode.elas_help.service;

import com.soulcode.elas_help.domain.Chamados;
import com.soulcode.elas_help.domain.Tecnico;
import com.soulcode.elas_help.domain.Usuario;
import com.soulcode.elas_help.model.ChamadosDTO;
import com.soulcode.elas_help.repos.ChamadosRepository;
import com.soulcode.elas_help.repos.TecnicoRepository;
import com.soulcode.elas_help.repos.UsuarioRepository;
import com.soulcode.elas_help.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChamadosService {

    private final ChamadosRepository chamadosRepository;
    private final TecnicoRepository tecnicoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadosService(final ChamadosRepository chamadosRepository,
            final TecnicoRepository tecnicoRepository, final UsuarioRepository usuarioRepository) {
        this.chamadosRepository = chamadosRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ChamadosDTO> findAll() {
        final List<Chamados> chamadoses = chamadosRepository.findAll(Sort.by("id"));
        return chamadoses.stream()
                .map(chamados -> mapToDTO(chamados, new ChamadosDTO()))
                .toList();
    }

    public ChamadosDTO get(final UUID id) {
        return chamadosRepository.findById(id)
                .map(chamados -> mapToDTO(chamados, new ChamadosDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ChamadosDTO chamadosDTO) {
        final Chamados chamados = new Chamados();
        mapToEntity(chamadosDTO, chamados);
        return chamadosRepository.save(chamados).getId();
    }

    public void update(final UUID id, final ChamadosDTO chamadosDTO) {
        final Chamados chamados = chamadosRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chamadosDTO, chamados);
        chamadosRepository.save(chamados);
    }

    public void delete(final UUID id) {
        chamadosRepository.deleteById(id);
    }

    private ChamadosDTO mapToDTO(final Chamados chamados, final ChamadosDTO chamadosDTO) {
        chamadosDTO.setId(chamados.getId());
        chamadosDTO.setSetor(chamados.getSetor());
        chamadosDTO.setDescricao(chamados.getDescricao());
        chamadosDTO.setPrioridade(chamados.getPrioridade());
        chamadosDTO.setAberto(chamados.getAberto());
        chamadosDTO.setTecnico(chamados.getTecnico() == null ? null : chamados.getTecnico().getId());
        chamadosDTO.setUsers(chamados.getUsers() == null ? null : chamados.getUsers().getId());
        return chamadosDTO;
    }

    private Chamados mapToEntity(final ChamadosDTO chamadosDTO, final Chamados chamados) {
        chamados.setSetor(chamadosDTO.getSetor());
        chamados.setDescricao(chamadosDTO.getDescricao());
        chamados.setPrioridade(chamadosDTO.getPrioridade());
        chamados.setAberto(chamadosDTO.getAberto());
        final Tecnico tecnico = chamadosDTO.getTecnico() == null ? null : tecnicoRepository.findById(chamadosDTO.getTecnico())
                .orElseThrow(() -> new NotFoundException("tecnico not found"));
        chamados.setTecnico(tecnico);
        final Usuario users = chamadosDTO.getUsers() == null ? null : usuarioRepository.findById(chamadosDTO.getUsers())
                .orElseThrow(() -> new NotFoundException("users not found"));
        chamados.setUsers(users);
        return chamados;
    }

}
