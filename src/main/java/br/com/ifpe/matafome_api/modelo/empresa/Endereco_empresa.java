    package br.com.ifpe.matafome_api.modelo.empresa;

    import org.hibernate.annotations.SQLRestriction;

    import com.fasterxml.jackson.annotation.JsonIgnore;

    import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Entity
    @Table(name = "endereco_empresa")
    @SQLRestriction("habilitado = true")

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Endereco_empresa extends EntidadeAuditavel {

        @JsonIgnore
        @OneToOne
        private Empresa empresa;

        @Column(length = 10/*,nullable = false*/)
        private String cep;

        @Column(length = 100)
        private String logradouro;

        @Column(length = 50)
        private String complemento;

        @Column(length = 10 /* , nullable = false*/)
        private String numero;

        @Column(length = 50 /* ,nullable = false*/)
        private String bairro;

        @Column(length = 40/*, nullable = false*/)
        private String cidade;

        @Column(length = 2 /* , nullable = false*/)
        private String estado;

    }
