package com.soulcode.elas_help.repos;

import com.soulcode.elas_help.domain.Usuario;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByUsuarioIgnoreCase(String usuario);

}
