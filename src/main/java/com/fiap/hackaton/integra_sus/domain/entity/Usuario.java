package com.fiap.hackaton.integra_sus.domain.entity;

import com.fiap.hackaton.integra_sus.domain.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class Usuario {
    private String id;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String cpf;
    private Role role;
    private String crm;
    private List<String> unidadesPermitidas;
    private boolean ativo;
}
