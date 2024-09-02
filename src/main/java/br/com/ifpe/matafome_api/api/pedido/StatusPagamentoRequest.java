package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.StatusPagamentoEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusPagamentoRequest {

    @NonNull
    @NotBlank
    private StatusPagamentoEnum novoStatusPagamento;

}
