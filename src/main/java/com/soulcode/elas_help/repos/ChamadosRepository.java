package com.soulcode.elas_help.repos;

import com.soulcode.elas_help.domain.Chamados;
import com.soulcode.elas_help.domain.Tecnico;
import com.soulcode.elas_help.domain.Usuario;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChamadosRepository extends JpaRepository<Chamados, UUID> {

    Chamados findFirstByTecnico(Tecnico tecnico);

    Chamados findFirstByUsers(Usuario usuario);

}
