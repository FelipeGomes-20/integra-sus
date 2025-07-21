package com.fiap.hackaton.integra_sus.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Paciente {
    private String id;
    private String nomeCompleto;
    private String cpf;
    private String cns; //Cartão Nacional de Saúde
    private LocalDate dataNascimento;
    private String sexo;
    private List<String> contatos;
    private List<String> alergias;
}
