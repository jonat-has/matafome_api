package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.StatusPedidoEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {

    @NonNull
    @NotBlank
    private StatusPedidoEnum novoStatusPedido;

}
