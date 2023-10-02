package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.interfaces.MessageStrategy;
import br.ufal.ic.p2.jackut.models.*;
import java.util.Objects;
/**
 * A classe `UserMessageStrategy` � uma estrat�gia para enviar mensagens entre usu�rios no sistema Jackut.
 * Esta estrat�gia verifica se o remetente e o destinat�rio s�o diferentes, se o destinat�rio n�o � um inimigo do remetente
 * e, em seguida, envia a mensagem para o destinat�rio.
 */
class UserMessageStrategy implements MessageStrategy {
    /**
     * Envia uma mensagem entre usu�rios.
     *
     * @param message A mensagem a ser enviada.
     * @param sender O login do remetente da mensagem.
     * @param receiver O login do destinat�rio da mensagem.
     * @throws InvalidMessageException Se o remetente tentar enviar uma mensagem para si mesmo.
     * @throws InvalidFunctionException Se o destinat�rio for um inimigo do remetente.
     * @throws UserNotFoundException Se o destinat�rio especificado n�o for encontrado no sistema.
     */
    @Override
    public void sendMessage(String message, String sender, String receiver) {
        User receiverUser = JackutSystemManager.SYSTEM.getUser(receiver);
        if (Objects.equals(sender, receiver)) throw new InvalidMessageException("Usu�rio n�o pode enviar recado para si mesmo.");
        else if (receiverUser.isEnemy(sender)){
            throw new InvalidFunctionException("Fun��o inv�lida: " + receiverUser.getName() + " � seu inimigo.");
        }
        else {
            Message newMessage = new Message(sender, message);
            receiverUser.receiveMessage(newMessage);
        }
    }
}