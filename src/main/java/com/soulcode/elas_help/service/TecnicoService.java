package com.soulcode.elas_help.service;

import com.soulcode.elas_help.domain.Chamados;
import com.soulcode.elas_help.domain.Tecnico;
import com.soulcode.elas_help.model.TecnicoDTO;
import com.soulcode.elas_help.repos.ChamadosRepository;
import com.soulcode.elas_help.repos.TecnicoRepository;
import com.soulcode.elas_help.util.NotFoundException;
import com.soulcode.elas_help.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final ChamadosRepository chamadosRepository;

    public TecnicoService(final TecnicoRepository tecnicoRepository,
            final ChamadosRepository chamadosRepository) {
        this.tecnicoRepository = tecnicoRepository;
        this.chamadosRepository = chamadosRepository;
    }

    public List<TecnicoDTO> findAll() {
        final List<Tecnico> tecnicoes = tecnicoRepository.findAll(Sort.by("id"));
        return tecnicoes.stream()
                .map(tecnico -> mapToDTO(tecnico, new TecnicoDTO()))
                .toList();
    }

    public TecnicoDTO get(final UUID id) {
        return tecnicoRepository.findById(id)
                .map(tecnico -> mapToDTO(tecnico, new TecnicoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final TecnicoDTO tecnicoDTO) {
        final Tecnico tecnico = new Tecnico();
        mapToEntity(tecnicoDTO, tecnico);
        return tecnicoRepository.save(tecnico).getId();
    }

    public void update(final UUID id, final TecnicoDTO tecnicoDTO) {
        final Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tecnicoDTO, tecnico);
        tecnicoRepository.save(tecnico);
    }

    public void delete(final UUID id) {
        tecnicoRepository.deleteById(id);
    }

    private TecnicoDTO mapToDTO(final Tecnico tecnico, final TecnicoDTO tecnicoDTO) {
        tecnicoDTO.setId(tecnico.getId());
        tecnicoDTO.setNome(tecnico.getNome());
        tecnicoDTO.setUsuario(tecnico.getUsuario());
        tecnicoDTO.setSenha(tecnico.getSenha());
        return tecnicoDTO;
    }

    private Tecnico mapToEntity(final TecnicoDTO tecnicoDTO, final Tecnico tecnico) {
        tecnico.setNome(tecnicoDTO.getNome());
        tecnico.setUsuario(tecnicoDTO.getUsuario());
        tecnico.setSenha(tecnicoDTO.getSenha());
        return tecnico;
    }

    public boolean usuarioExists(final String usuario) {
        return tecnicoRepository.existsByUsuarioIgnoreCase(usuario);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Chamados tecnicoChamados = chamadosRepository.findFirstByTecnico(tecnico);
        if (tecnicoChamados != null) {
            referencedWarning.setKey("tecnico.chamados.tecnico.referenced");
            referencedWarning.addParam(tecnicoChamados.getId());
            return referencedWarning;
        }
        return null;
    }

}
