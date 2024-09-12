package br.com.ifpe.matafome_api.modelo.pedido;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;  
import lombok.Setter;

@Entity
@Table(name = "pedido")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido extends EntidadeAuditavel {

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne
    private Endereco_cliente enderecoEntrega;

    @ManyToOne
    private Forma_pagamento formaPagamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Itens_pedido> itensPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedidoEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    private StatusPagamentoEnum statusPagamento;

    @Column(nullable = false)
    private LocalDateTime dataHoraPedido;

    @Column(nullable = false)
    private Float taxaEntrega;

    @Column(nullable = false)
    private Double valorTotal;

    @Column
    private String observacao;
}
