package com.soulcode.elas_help.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChamadosDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String setor;

    @NotNull
    @Size(max = 255)
    private String descricao;

    private Integer prioridade;

    @NotNull
    private Boolean aberto;

    @NotNull
    private UUID tecnico;

    @NotNull
    private UUID users;

}
