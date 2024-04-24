package com.soulcode.elas_help.service;

import com.soulcode.elas_help.domain.Chamados;
import com.soulcode.elas_help.domain.Usuario;
import com.soulcode.elas_help.model.UsuarioDTO;
import com.soulcode.elas_help.repos.ChamadosRepository;
import com.soulcode.elas_help.repos.UsuarioRepository;
import com.soulcode.elas_help.util.NotFoundException;
import com.soulcode.elas_help.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ChamadosRepository chamadosRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final ChamadosRepository chamadosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.chamadosRepository = chamadosRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final UUID id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final UUID id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final UUID id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setSenha(usuario.getSenha());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNome(usuarioDTO.getNome());
        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setSenha(usuarioDTO.getSenha());
        return usuario;
    }

    public boolean usuarioExists(final String usuario) {
        return usuarioRepository.existsByUsuarioIgnoreCase(usuario);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Chamados usersChamados = chamadosRepository.findFirstByUsers(usuario);
        if (usersChamados != null) {
            referencedWarning.setKey("usuario.chamados.users.referenced");
            referencedWarning.addParam(usersChamados.getId());
            return referencedWarning;
        }
        return null;
    }

}
