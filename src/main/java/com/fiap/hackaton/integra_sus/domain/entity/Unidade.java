package com.fiap.hackaton.integra_sus.domain.entity;

import lombok.Data;

@Data
public class Unidade {
    private String id;
    private String cnpj;
    private String razaoSocial;
    private boolean ativo;
}
