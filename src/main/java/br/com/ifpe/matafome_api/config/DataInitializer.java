package br.com.ifpe.matafome_api.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ifpe.matafome_api.modelo.pedido.Status_pedido;
import br.com.ifpe.matafome_api.modelo.pedido.Status_pedidoRepository;
import jakarta.annotation.PostConstruct;


@Component
public class DataInitializer {

    @Autowired
    private Status_pedidoRepository statusPedidoRepository;

    @PostConstruct
    public void init() {
        if (statusPedidoRepository.count() == 0) { // Verifica se a tabela está vazia
            Status_pedido aberto = Status_pedido.builder()
                .descricao("Aberto")
                .build();

            Status_pedido emPreparacao = Status_pedido.builder()
                .descricao("Em Preparacao")
                .build();

            Status_pedido aCaminho = Status_pedido.builder()
                .descricao("A Caminho")
                .build();

            Status_pedido entregue = Status_pedido.builder()
                .descricao("Entregue")
                .build();


            Status_pedido cancelado = Status_pedido.builder()
                .descricao("Cancelado")
                .build();

            statusPedidoRepository.save(aberto);
            statusPedidoRepository.save(emPreparacao);
            statusPedidoRepository.save(aCaminho);
            statusPedidoRepository.save(entregue);
            statusPedidoRepository.save(cancelado);
        }
    }
}