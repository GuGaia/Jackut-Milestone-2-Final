package br.ufal.ic.p2.jackut.interfaces;

/**
 * A interface `MessageStrategy` define um contrato para estratégias de envio de mensagens no sistema Jackut.
 * Qualquer classe que implemente esta interface deve fornecer uma implementação para o método `sendMessage`.
 */
public interface MessageStrategy {
    /**
     * Envia uma mensagem com base na estratégia específica implementada.
     *
     * @param message A mensagem a ser enviada.
     * @param sender O login do remetente da mensagem.
     * @param receiver O login do destinatário da mensagem.
     */
    void sendMessage(String message, String sender, String receiver);
}
