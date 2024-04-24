package com.soulcode.elas_help.rest;

import com.soulcode.elas_help.model.ChamadosDTO;
import com.soulcode.elas_help.service.ChamadosService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/chamadoss", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChamadosResource {

    private final ChamadosService chamadosService;

    public ChamadosResource(final ChamadosService chamadosService) {
        this.chamadosService = chamadosService;
    }

    @GetMapping
    public ResponseEntity<List<ChamadosDTO>> getAllChamadoss() {
        return ResponseEntity.ok(chamadosService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadosDTO> getChamados(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(chamadosService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createChamados(@RequestBody @Valid final ChamadosDTO chamadosDTO) {
        final UUID createdId = chamadosService.create(chamadosDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateChamados(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ChamadosDTO chamadosDTO) {
        chamadosService.update(id, chamadosDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamados(@PathVariable(name = "id") final UUID id) {
        chamadosService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
