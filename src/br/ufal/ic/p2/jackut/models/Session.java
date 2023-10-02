package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.interfaces.MessageStrategy;
import br.ufal.ic.p2.jackut.services.JackutSystemManager;

import java.util.Objects;
/**
 * A classe Session representa uma sess�o de usu�rio no sistema Jackut.
 * Cada sess�o est� associada a um usu�rio autenticado e possui um ID exclusivo.
 */
public class Session {
    private User user; // O usu�rio associado � sess�o.
    private String ID; // O ID exclusivo da sess�o.
    /**
     * Construtor da classe Session.
     * @param user O usu�rio associado � sess�o.
     */
    public Session(User user){
        this.user = user;
        this.ID = generateSessionId(user.getUserAttribute("login"));
    }
    /**
     * Gera um ID �nico de sess�o combinando o login do usu�rio e o momento da cria��o.
     *
     * @param login O login do usu�rio.
     * @return O ID exclusivo da sess�o.
     */
    private String generateSessionId(String login) {
        long timestamp = System.currentTimeMillis();
        return login + "_" + timestamp;
    }
    /**
     * Obt�m o usu�rio associado � sess�o.
     * @return O usu�rio associado � sess�o.
     */
    public User getUser() {
        return user;
    }
    /**
     * Obt�m o ID exclusivo da sess�o.
     * @return O ID exclusivo da sess�o.
     */
    public String getID() {
        return ID;
    }
    /**
     * Edita o perfil do usu�rio autenticado.
     * @param atributte O atributo a ser editado ("nome", "senha", "login" ou atributo extra).
     * @param valor O novo valor para o atributo.
     */
    public void editProfile(String atributte, String valor){

        if (Objects.equals(atributte, "nome")) this.user.setName(valor);
        else if (Objects.equals(atributte, "senha")) this.user.setPassword(valor);
        else if (Objects.equals(atributte, "login")) {
            if (JackutSystemManager.SYSTEM.verifyUser(valor)) throw new InvalidCredentialException("Login inv�lido.");
            else this.user.setLogin(valor);
        }
        else this.user.setAttributes(atributte, valor);
    }
    /**
     * Adiciona um usu�rio autenticado como amigo de outro usu�rio enviando uma solicita��o.
     * @param friend O usu�rio a ser adicionado como amigo.
     * @throws RuntimeException Se algum dos usu�rios n�o for encontrado ou se houver um erro na solicita��o.
     */
    public void addFriend(User friend) {
        if (friend.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Fun��o inv�lida: " + friend.getName() + " � seu inimigo.");
        }
        else if (Objects.equals(this.user, friend))
            throw new InvalidFriendSolicitationException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
        else if (user.getMyRelationships().getFriendSolicitation().contains(friend.getLogin())) {
            user.getMyRelationships().addFriends(friend.getLogin());
            friend.getMyRelationships().addFriends(user.getLogin());
        } else if (friend.getMyRelationships().getFriendSolicitation().contains(user.getLogin()))
            throw new InvalidFriendSolicitationException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        else if (user.isFriend(friend.getLogin()))
            throw new UserDuplicationException("Usu�rio j� est� adicionado como amigo.");
        else {
            friend.getMyRelationships().addFriendSolicitation(user.getLogin());
        }
    }
    /**
     * Adiciona um usu�rio autenticado como paquera de outro usu�rio.
     * @param crush O usu�rio a ser adicionado como paquera.
     * @throws RuntimeException Se algum dos usu�rios n�o for encontrado ou se houver um erro na opera��o.
     */
    public void addCrush(User crush){
        if (crush.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Fun��o inv�lida: " + crush.getName() + " � seu inimigo.");
        }
        else if(this.user == crush){
            throw new InvalidCredentialException("Usu�rio n�o pode ser paquera de si mesmo.");
        }
        else if(crush.isCrush(this.user.getLogin())){
            this.user.getMyRelationships().addCrush(crush.getLogin());
            Message messageUser = new Message("System", crush.getName() + " � seu paquera - Recado do Jackut.");
            Message messageCrush = new Message("System", this.user.getName() + " � seu paquera - Recado do Jackut.");
            this.user.getMessageBox().add(messageUser);
            crush.getMessageBox().add(messageCrush);
        }
        else this.user.getMyRelationships().addCrush(crush.getLogin());
    }
    /**
     * Adiciona um usu�rio autenticado como �dolo de outro usu�rio, e consequentemente o outro usu�rio como f�.
     * @param idol O usu�rio a ser adicionado como �dolo.
     * @throws RuntimeException Se algum dos usu�rios n�o for encontrado ou se houver um erro na opera��o.
     */
    public void addIdol(User idol){
        if (idol.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Fun��o inv�lida: " + idol.getName() + " � seu inimigo.");
        }
        else if (this.user == idol){
            throw new InvalidCredentialException("Usu�rio n�o pode ser f� de si mesmo.");
        }
        else{
            this.user.getMyRelationships().addIdol(idol.getLogin());
            idol.getMyRelationships().addFan(this.user.getLogin());
        }
    }
    /**
     * Adiciona um usu�rio autenticado como inimigo de outro usu�rio.
     *
     * @param enemy O login do usu�rio a ser adicionado como inimigo.
     * @throws RuntimeException Se houver um erro na opera��o.
     */
    public void addEnemy(String enemy){
        if (Objects.equals(this.user.getLogin(), enemy)){
            throw new InvalidCredentialException("Usu�rio n�o pode ser inimigo de si mesmo.");
        }
        else this.user.getMyRelationships().addEnemy(enemy);
    }
    /**
     * Cria uma nova comunidade com um nome e descri��o especificados.
     *
     * @param name O nome da comunidade.
     * @param description A descri��o da comunidade.
     * @return A nova comunidade criada.
     */
    public Community createCommunity(String name, String description){
        Community newCommunity = new Community(name, description, this.user.getLogin());
        newCommunity.addMember(this.user);
        this.user.addCommunity(name);
        return newCommunity;
    }
    /**
     * Adiciona o usu�rio autenticado como membro de uma comunidade existente.
     *
     * @param community A comunidade � qual o usu�rio ser� adicionado.
     * @throws RuntimeException Se o usu�rio j� faz parte da comunidade.
     */
    public void joinCommunity(Community community){
        if(this.user.getMyCommunities().contains(community.getName())) throw new InvalidCommunityException("Usuario j� faz parte dessa comunidade.");
        else {
            community.addMember(this.user);
            this.user.addCommunity(community.getName());
        }
    }
    /**
     * L� as mensagens da comunidade � qual o usu�rio pertence.
     * @return As mensagens da comunidade.
     */
    public String readCommunityMessages(){
        return this.user.readCommunityMessage();
    }
    /**
     * Envia uma mensagem para um destinat�rio espec�fico com base na estrat�gia de mensagem fornecida.
     * @param receiver O destinat�rio da mensagem.
     * @param message O conte�do da mensagem.
     * @param messageStrategy A estrat�gia de mensagem utilizada.
     */
    public void messageSender (String receiver, String message, MessageStrategy messageStrategy){
        messageStrategy.sendMessage(message, this.user.getLogin(), receiver);
    }

}
