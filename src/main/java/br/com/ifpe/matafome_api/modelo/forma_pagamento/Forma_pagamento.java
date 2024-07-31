package br.com.ifpe.matafome_api.modelo.forma_pagamento;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "forma_de_pagamento")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Forma_pagamento extends EntidadeAuditavel {

    @JsonIgnore
    @ManyToOne
    private Cliente cliente;

    @Column(length = 50)
    private String tipo;

    @Column(length = 16)
    private String numero_cartao;

    @Column(length = 7)
    private String data_validade;

    @Column(length = 100)
    private String nome_titular;

    @Column(length = 3)
    private Integer cvv;

    @Column(length = 100)
    private String endereco_cobranca;

    @Column(length = 40)
    private String cidade_cobranca;

    @Column(length = 2)
    private String estado_cobranca;

    @Column(length = 10)
    private String cep_cobranca;

}
