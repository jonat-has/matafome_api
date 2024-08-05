package br.com.ifpe.matafome_api.modelo.pedido;

import java.time.LocalTime;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Pedido extends EntidadeAuditavel  {

    @ManyToOne
      @JoinColumn(nullable = false)
      private Usuario usuario;
  
    @Column
    private LocalTime hora_pedido;

    @Column
    private String forma_pagamento;

    @Column
    private String status_pedido;

    @Column
    private String status_pagamento;

    @Column
    private String horario;

    @Column
    private Double valor_total;

    @Column
    private LocalTime taxa_entrega;

}
