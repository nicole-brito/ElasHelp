package com.soulcode.elas_help.repos;

import com.soulcode.elas_help.domain.Tecnico;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TecnicoRepository extends JpaRepository<Tecnico, UUID> {

    boolean existsByUsuarioIgnoreCase(String usuario);

}
