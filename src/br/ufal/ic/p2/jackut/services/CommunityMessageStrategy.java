package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.interfaces.MessageStrategy;
import br.ufal.ic.p2.jackut.models.*;
/**
 * A classe CommunityMessageStrategy implementa a estrat�gia de envio de mensagens para uma comunidade.
 * Quando uma mensagem � enviada para uma comunidade, ela � distribu�da para todos os membros da comunidade.
 * Essa classe � utilizada para enviar mensagens para as comunidades no sistema Jackut.
 */
class CommunityMessageStrategy implements MessageStrategy {
    /**
     * Envia uma mensagem para uma comunidade, distribuindo-a para todos os membros da comunidade.
     *
     * @param message  O conte�do da mensagem a ser enviado para a comunidade.
     * @param sender   O login do remetente da mensagem.
     * @param receiver O nome da comunidade de destino da mensagem.
     */
    @Override
    public void sendMessage(String message, String sender, String receiver) {
        Community community = JackutSystemManager.SYSTEM.getCommunity(receiver);
        Message newMessage = new Message(sender, message);
        for(String user : community.getMembers()){
            JackutSystemManager.SYSTEM.getUser(user).getCommunityMessages().add(newMessage);
        }
    }
}

