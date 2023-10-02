package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * A classe User representa um usuário do sistema de gerenciamento.
 *
 * @author Gustavo Gaia
 */
public class User {
    private String name;// Nome do usuário
    private String login;// Login do usuário
    private String password;// Senha do usuário
    private Relationships myRelationships;
    private Map<String, String> attributes;// Atributos extras do usuário
    private Queue<Message> messageBox;// Caixa de mensagens do usuário
    private Queue<Message> communityMessages;// Mensagens da comunidade
    private ArrayList<String> myCommunities;// Comunidades às quais o usuário pertence

    /**
     * Construtor da classe User
     */
    public User() {
    }
    /**
     * Construtor da classe User nas configurações para armazenamento JSON.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @param nome O nome do usuário.
     */
    @JsonCreator
    public User(@JsonProperty("login") String login, @JsonProperty("senha") String senha, @JsonProperty("nome") String nome) {
        this.login = login;
        this.password = senha;
        this.name = nome;
        myRelationships = new Relationships();
        messageBox = new LinkedList<>();
        attributes = new HashMap<>();
        myCommunities = new ArrayList<>();
        communityMessages = new LinkedList<>();
    }
    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param password A senha a ser verificada.
     * @return `true` se a senha estiver correta, `false` caso contrário.
     */
    public boolean verifyPassword(String password){
        return Objects.equals(password, this.password);
    }
    /**
     * Obtém a lista de relacionamentos do usuário
     * @return lista de relacionamentos do usuário
     */
    public Relationships getMyRelationships() {
        return myRelationships;
    }
    /**
     * Obtém a caixa de mensagens do usuário
     * @return caixa de mensagens do usuário
     */
    public Queue<Message> getMessageBox() {
        return messageBox;
    }
    /**
     * Atualiza o nome do usuário
     * @param name novo nome do usuário
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Atualiza o login do usuário
     * @param login novo login do usuário
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Atualiza a senha do usuário
     * @param password nova senha do usuário
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Atualiza aos relacionamentos do usuário
     * @param relationships novos relacionamentos do usuário
     */
    public void setMyRelationships(Relationships relationships) {
        this.myRelationships = relationships;
    }

    /**
     * Atualiza a caixa de mensagem do usuário
     * @param messageBox caixa de mensagem do usuário
     */
    public void setMessageBox(Queue<Message> messageBox) {
        this.messageBox = messageBox;
    }
    /**
     * Obtém o atributo do usuário escolhido.
     * @param attribute o atributo desejado.
     * @return Valor do atributo desejado.
     */
    public String getUserAttribute(String attribute){
        if (Objects.equals(attribute, "nome")) return this.name;
        else if (Objects.equals(attribute, "senha")) return this.password;
        else if (Objects.equals(attribute, "login")) return this.login;
        else return getExtraAttribute(attribute);
    }
    /**
     * Obtém o atributo extra do usuário escolhido.
     * @param attribute o atributo desejado.
     * @return Valor do atributo desejado da lista de atributos extras.
     */
    public String getExtraAttribute(String attribute) {
        if (attributes.containsKey(attribute)) return attributes.get(attribute);
            else throw new InvalidCredentialException("Atributo não preenchido.");
    }
    /**
     * Obtém os atributos extras do usuário em um mapa.
     *
     * @return Um mapa contendo os atributos extras e seus valores.
     */
    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }
    /**
     * Define os atributos extras do usuário a partir de um mapa.
     *
     * @param attribute O nome do atributo.
     * @param value O valor do atributo.
     */
    @JsonAnySetter
    public void setAttributes(String attribute, String value) {
        if (attributes.containsKey(attribute)) attributes.replace(attribute, value);
        else attributes.put(attribute, value);
    }

    /**
     * Adiciona um recado à caixa de mensagens do usuário.
     *
     * @param message O recado a ser adicionado.
     */
    public void receiveMessage(Message message){
        this.messageBox.add(message);
    }
    /**
     * Verifica se o usuário e fã de um usuário recebido.
     * @param idol O usuário a ser verificado.
     * @return `true` se for fã, `false` caso contrário.
     */
    public boolean isFan(String idol){return myRelationships.getIdols().contains(idol);}
    /**
     * Verifica se um usuário recebido é paquera do usuário.
     * @param crush O usuário a ser verificado.
     * @return `true` se for paquera, `false` caso contrário.
     */
    public boolean isCrush(String crush){
        return myRelationships.getCrush().contains(crush);
    }
    /**
     * Verifica se um usuário recebido é amigo do usuário.
     * @param friend O usuário a ser verificado.
     * @return `true` se for amigo, `false` caso contrário.
     */
    public boolean isFriend(String friend){
        return myRelationships.getFriendsList().contains(friend);
    }
    /**
     * Verifica se um usuário recebido é inimigo do usuário.
     * @param enemy O usuário a ser verificado.
     * @return `true` se for inimigo, `false` caso contrário.
     */
    public boolean isEnemy(String enemy){
        return myRelationships.getEnemy().contains(enemy);
    }
    /**
     * Obtém a lista de amigos do usuário em formato de String.
     * @return Uma String que representa a lista de amigos do usuário no formato JSON.
     *         Se a lista estiver vazia, retorna "{}".
     */
    public String getFriendList(){
        ArrayList<String> friends = this.myRelationships.getFriendsList();
        return friends.isEmpty() ? "{}" : "{" + String.join(",", friends) + "}";
    }
    /**
     * Lê o primeiro recado da caixa de mensagens de um usuário.
     * @throws RuntimeException Se não houver recados na caixa de mensagens.
     */
    public String readMessage(){
        Message message = this.messageBox.poll();
        if(message == null) throw new MessageNotFoundException("Não há recados.");
        else return message.getMessage();
    }
    /**
     * Lê a primeira mensagem da caixa de mensagens da comunidade.
     *
     * @return O conteúdo da mensagem lida.
     * @throws MessageNotFoundException Se não houver mensagens na caixa de mensagens da comunidade.
     */
    public String readCommunityMessage(){
        Message message = this.communityMessages.poll();
        if(message == null) throw new MessageNotFoundException("Não há mensagens.");
        else return message.getMessage();

    }
    /**
     * Obtém o login do usuário.
     * @return O login do usuário.
     */
    public String getLogin() {
        return login;
    }
    /**
     * Obtém o nome do usuário.
     * @return O nome do usuário.
     */
    public String getName() {
        return name;
    }
    /**
     * Obtém a senha do usuário.
     * @return A senha do usuário.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Adiciona o nome de uma comunidade à lista de comunidades às quais o usuário pertence.
     * @param name O nome da comunidade a ser adicionado.
     */
    public void addCommunity(String name){
        this.myCommunities.add(name);
    }
    /**
     * Obtém a lista de comunidades às quais o usuário pertence.
     * @return Uma lista de nomes de comunidades.
     */
    public ArrayList<String> getMyCommunities() {
        return myCommunities;
    }
    /**
     * Define a lista de comunidades às quais o usuário pertence.
     * @param myCommunities Uma lista de nomes de comunidades.
     */
    public void setMyCommunities(ArrayList<String> myCommunities) {
        this.myCommunities = myCommunities;
    }
    /**
     * Obtém a caixa de mensagens da comunidade.
     * @return Uma fila de mensagens da comunidade.
     */
    public Queue<Message> getCommunityMessages() {
        return communityMessages;
    }
    /**
     * Define a caixa de mensagens da comunidade.
     * @param communityMessages Uma fila de mensagens da comunidade.
     */
    public void setCommunityMessages(Queue<Message> communityMessages) {
        this.communityMessages = communityMessages;
    }


}
