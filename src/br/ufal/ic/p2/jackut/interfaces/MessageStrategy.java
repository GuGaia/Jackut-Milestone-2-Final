package br.ufal.ic.p2.jackut.interfaces;

/**
 * A interface `MessageStrategy` define um contrato para estrat�gias de envio de mensagens no sistema Jackut.
 * Qualquer classe que implemente esta interface deve fornecer uma implementa��o para o m�todo `sendMessage`.
 */
public interface MessageStrategy {
    /**
     * Envia uma mensagem com base na estrat�gia espec�fica implementada.
     *
     * @param message A mensagem a ser enviada.
     * @param sender O login do remetente da mensagem.
     * @param receiver O login do destinat�rio da mensagem.
     */
    void sendMessage(String message, String sender, String receiver);
}
