package br.com.ifpe.matafome_api.util.entity;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;

import java.time.LocalDate;

public class EntidadeAuditavelService {

    private EntidadeAuditavelService() {
    }

    public static void atualizarMetadadosEntidade(EntidadeAuditavel entidade, Usuario usuarioLogado) {
        entidade.setVersao(entidade.getVersao() + 1);
        entidade.setDataUltimaModificacao(LocalDate.now());
        entidade.setUltimaModificacaoPor(usuarioLogado);
    }

    public static void criarMetadadosEntidade(EntidadeAuditavel entidade, Usuario usuarioLogado) {
        entidade.setHabilitado(Boolean.TRUE);
        entidade.setVersao(1L);
        entidade.setDataCriacao(LocalDate.now());
        entidade.setCriadoPor(usuarioLogado);
    }

    public static void desativarEntidade(EntidadeAuditavel entidade, Usuario usuarioLogado) {
        entidade.setDataUltimaModificacao(LocalDate.now());
        entidade.setUltimaModificacaoPor(usuarioLogado);
        entidade.setHabilitado(Boolean.FALSE);
        entidade.setVersao(entidade.getVersao() + 1);
    }
}
