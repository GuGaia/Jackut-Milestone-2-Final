package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.interfaces.MessageStrategy;
import br.ufal.ic.p2.jackut.services.JackutSystemManager;

import java.util.Objects;
/**
 * A classe Session representa uma sessão de usuário no sistema Jackut.
 * Cada sessão está associada a um usuário autenticado e possui um ID exclusivo.
 */
public class Session {
    private User user; // O usuário associado à sessão.
    private String ID; // O ID exclusivo da sessão.
    /**
     * Construtor da classe Session.
     * @param user O usuário associado à sessão.
     */
    public Session(User user){
        this.user = user;
        this.ID = generateSessionId(user.getUserAttribute("login"));
    }
    /**
     * Gera um ID único de sessão combinando o login do usuário e o momento da criação.
     *
     * @param login O login do usuário.
     * @return O ID exclusivo da sessão.
     */
    private String generateSessionId(String login) {
        long timestamp = System.currentTimeMillis();
        return login + "_" + timestamp;
    }
    /**
     * Obtém o usuário associado à sessão.
     * @return O usuário associado à sessão.
     */
    public User getUser() {
        return user;
    }
    /**
     * Obtém o ID exclusivo da sessão.
     * @return O ID exclusivo da sessão.
     */
    public String getID() {
        return ID;
    }
    /**
     * Edita o perfil do usuário autenticado.
     * @param atributte O atributo a ser editado ("nome", "senha", "login" ou atributo extra).
     * @param valor O novo valor para o atributo.
     */
    public void editProfile(String atributte, String valor){

        if (Objects.equals(atributte, "nome")) this.user.setName(valor);
        else if (Objects.equals(atributte, "senha")) this.user.setPassword(valor);
        else if (Objects.equals(atributte, "login")) {
            if (JackutSystemManager.SYSTEM.verifyUser(valor)) throw new InvalidCredentialException("Login inválido.");
            else this.user.setLogin(valor);
        }
        else this.user.setAttributes(atributte, valor);
    }
    /**
     * Adiciona um usuário autenticado como amigo de outro usuário enviando uma solicitação.
     * @param friend O usuário a ser adicionado como amigo.
     * @throws RuntimeException Se algum dos usuários não for encontrado ou se houver um erro na solicitação.
     */
    public void addFriend(User friend) {
        if (friend.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Função inválida: " + friend.getName() + " é seu inimigo.");
        }
        else if (Objects.equals(this.user, friend))
            throw new InvalidFriendSolicitationException("Usuário não pode adicionar a si mesmo como amigo.");
        else if (user.getMyRelationships().getFriendSolicitation().contains(friend.getLogin())) {
            user.getMyRelationships().addFriends(friend.getLogin());
            friend.getMyRelationships().addFriends(user.getLogin());
        } else if (friend.getMyRelationships().getFriendSolicitation().contains(user.getLogin()))
            throw new InvalidFriendSolicitationException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        else if (user.isFriend(friend.getLogin()))
            throw new UserDuplicationException("Usuário já está adicionado como amigo.");
        else {
            friend.getMyRelationships().addFriendSolicitation(user.getLogin());
        }
    }
    /**
     * Adiciona um usuário autenticado como paquera de outro usuário.
     * @param crush O usuário a ser adicionado como paquera.
     * @throws RuntimeException Se algum dos usuários não for encontrado ou se houver um erro na operação.
     */
    public void addCrush(User crush){
        if (crush.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Função inválida: " + crush.getName() + " é seu inimigo.");
        }
        else if(this.user == crush){
            throw new InvalidCredentialException("Usuário não pode ser paquera de si mesmo.");
        }
        else if(crush.isCrush(this.user.getLogin())){
            this.user.getMyRelationships().addCrush(crush.getLogin());
            Message messageUser = new Message("System", crush.getName() + " é seu paquera - Recado do Jackut.");
            Message messageCrush = new Message("System", this.user.getName() + " é seu paquera - Recado do Jackut.");
            this.user.getMessageBox().add(messageUser);
            crush.getMessageBox().add(messageCrush);
        }
        else this.user.getMyRelationships().addCrush(crush.getLogin());
    }
    /**
     * Adiciona um usuário autenticado como ídolo de outro usuário, e consequentemente o outro usuário como fã.
     * @param idol O usuário a ser adicionado como ídolo.
     * @throws RuntimeException Se algum dos usuários não for encontrado ou se houver um erro na operação.
     */
    public void addIdol(User idol){
        if (idol.isEnemy(this.user.getLogin())){
            throw new InvalidFunctionException("Função inválida: " + idol.getName() + " é seu inimigo.");
        }
        else if (this.user == idol){
            throw new InvalidCredentialException("Usuário não pode ser fã de si mesmo.");
        }
        else{
            this.user.getMyRelationships().addIdol(idol.getLogin());
            idol.getMyRelationships().addFan(this.user.getLogin());
        }
    }
    /**
     * Adiciona um usuário autenticado como inimigo de outro usuário.
     *
     * @param enemy O login do usuário a ser adicionado como inimigo.
     * @throws RuntimeException Se houver um erro na operação.
     */
    public void addEnemy(String enemy){
        if (Objects.equals(this.user.getLogin(), enemy)){
            throw new InvalidCredentialException("Usuário não pode ser inimigo de si mesmo.");
        }
        else this.user.getMyRelationships().addEnemy(enemy);
    }
    /**
     * Cria uma nova comunidade com um nome e descrição especificados.
     *
     * @param name O nome da comunidade.
     * @param description A descrição da comunidade.
     * @return A nova comunidade criada.
     */
    public Community createCommunity(String name, String description){
        Community newCommunity = new Community(name, description, this.user.getLogin());
        newCommunity.addMember(this.user);
        this.user.addCommunity(name);
        return newCommunity;
    }
    /**
     * Adiciona o usuário autenticado como membro de uma comunidade existente.
     *
     * @param community A comunidade à qual o usuário será adicionado.
     * @throws RuntimeException Se o usuário já faz parte da comunidade.
     */
    public void joinCommunity(Community community){
        if(this.user.getMyCommunities().contains(community.getName())) throw new InvalidCommunityException("Usuario já faz parte dessa comunidade.");
        else {
            community.addMember(this.user);
            this.user.addCommunity(community.getName());
        }
    }
    /**
     * Lê as mensagens da comunidade à qual o usuário pertence.
     * @return As mensagens da comunidade.
     */
    public String readCommunityMessages(){
        return this.user.readCommunityMessage();
    }
    /**
     * Envia uma mensagem para um destinatário específico com base na estratégia de mensagem fornecida.
     * @param receiver O destinatário da mensagem.
     * @param message O conteúdo da mensagem.
     * @param messageStrategy A estratégia de mensagem utilizada.
     */
    public void messageSender (String receiver, String message, MessageStrategy messageStrategy){
        messageStrategy.sendMessage(message, this.user.getLogin(), receiver);
    }

}
