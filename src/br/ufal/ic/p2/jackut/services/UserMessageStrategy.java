package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.interfaces.MessageStrategy;
import br.ufal.ic.p2.jackut.models.*;
import java.util.Objects;
/**
 * A classe `UserMessageStrategy` é uma estratégia para enviar mensagens entre usuários no sistema Jackut.
 * Esta estratégia verifica se o remetente e o destinatário são diferentes, se o destinatário não é um inimigo do remetente
 * e, em seguida, envia a mensagem para o destinatário.
 */
class UserMessageStrategy implements MessageStrategy {
    /**
     * Envia uma mensagem entre usuários.
     *
     * @param message A mensagem a ser enviada.
     * @param sender O login do remetente da mensagem.
     * @param receiver O login do destinatário da mensagem.
     * @throws InvalidMessageException Se o remetente tentar enviar uma mensagem para si mesmo.
     * @throws InvalidFunctionException Se o destinatário for um inimigo do remetente.
     * @throws UserNotFoundException Se o destinatário especificado não for encontrado no sistema.
     */
    @Override
    public void sendMessage(String message, String sender, String receiver) {
        User receiverUser = JackutSystemManager.SYSTEM.getUser(receiver);
        if (Objects.equals(sender, receiver)) throw new InvalidMessageException("Usuário não pode enviar recado para si mesmo.");
        else if (receiverUser.isEnemy(sender)){
            throw new InvalidFunctionException("Função inválida: " + receiverUser.getName() + " é seu inimigo.");
        }
        else {
            Message newMessage = new Message(sender, message);
            receiverUser.receiveMessage(newMessage);
        }
    }
}