package br.com.ifpe.matafome_api.modelo.empresa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriaEmpresaEnum {

  /*  PIZZARIA("Pizzaria"),
    HAMBURGUERIA("Hamburgueria"),
    SUSHI_BAR("Sushi Bar"),
    ITALIANA("Italiana"),
    CHINESA("Chinesa"),
    BRASILEIRA("Brasileira"),
    MEXICANA("Mexicana"),
    CHURRASCARIA("Churrascaria"),
    FRUTOS_DO_MAR("Frutos do Mar"),
    VEGETARIANA("Vegetariana"),
    SOBREMESAS("Sobremesas"),
    CAFETERIA("Cafeteria"),
    FAST_FOOD("Fast Food"),
    COMIDA_SAUDAVEL("Comida Saudável"),
    CONFEITARIA("Confeitaria"),
    CREPERIA("Creperia"),
    TAPIOCARIA("Tapiocaria"),
    LANCHONETE("Lanchonete"),
    DOCERIA("Doceria"),
    SORVETERIA("Sorveteria"),
    GOURMET("Gourmet"),
    MINEIRA("Mineira"),
    NORDESTINA("Nordestina");*/

    ACAITERIA("Açaiteria"),
    CAFETERIA("Cafeteria"),
    CHURRASCARIA("Churrascaria"),
    HAMBURGUERIA("Hamburgueria"),
    LANCHONETE("Lanchonete"),
    PADARIA("Padaria"),
    PIZZARIA("Pizzaria"),
    RESTAURANTE("Restaurante"),
    SORVETERIA("Sorveteria"),
    SUSHI_BAR("Sushi Bar"),
    OUTRO("Outro");

    private final String categoria;
}