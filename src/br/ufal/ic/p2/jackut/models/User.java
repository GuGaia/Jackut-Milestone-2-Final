package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * A classe User representa um usu�rio do sistema de gerenciamento.
 *
 * @author Gustavo Gaia
 */
public class User {
    private String name;// Nome do usu�rio
    private String login;// Login do usu�rio
    private String password;// Senha do usu�rio
    private Relationships myRelationships;
    private Map<String, String> attributes;// Atributos extras do usu�rio
    private Queue<Message> messageBox;// Caixa de mensagens do usu�rio
    private Queue<Message> communityMessages;// Mensagens da comunidade
    private ArrayList<String> myCommunities;// Comunidades �s quais o usu�rio pertence

    /**
     * Construtor da classe User
     */
    public User() {
    }
    /**
     * Construtor da classe User nas configura��es para armazenamento JSON.
     *
     * @param login O login do usu�rio.
     * @param senha A senha do usu�rio.
     * @param nome O nome do usu�rio.
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
     * Verifica se a senha fornecida corresponde � senha do usu�rio.
     *
     * @param password A senha a ser verificada.
     * @return `true` se a senha estiver correta, `false` caso contr�rio.
     */
    public boolean verifyPassword(String password){
        return Objects.equals(password, this.password);
    }
    /**
     * Obt�m a lista de relacionamentos do usu�rio
     * @return lista de relacionamentos do usu�rio
     */
    public Relationships getMyRelationships() {
        return myRelationships;
    }
    /**
     * Obt�m a caixa de mensagens do usu�rio
     * @return caixa de mensagens do usu�rio
     */
    public Queue<Message> getMessageBox() {
        return messageBox;
    }
    /**
     * Atualiza o nome do usu�rio
     * @param name novo nome do usu�rio
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Atualiza o login do usu�rio
     * @param login novo login do usu�rio
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Atualiza a senha do usu�rio
     * @param password nova senha do usu�rio
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Atualiza aos relacionamentos do usu�rio
     * @param relationships novos relacionamentos do usu�rio
     */
    public void setMyRelationships(Relationships relationships) {
        this.myRelationships = relationships;
    }

    /**
     * Atualiza a caixa de mensagem do usu�rio
     * @param messageBox caixa de mensagem do usu�rio
     */
    public void setMessageBox(Queue<Message> messageBox) {
        this.messageBox = messageBox;
    }
    /**
     * Obt�m o atributo do usu�rio escolhido.
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
     * Obt�m o atributo extra do usu�rio escolhido.
     * @param attribute o atributo desejado.
     * @return Valor do atributo desejado da lista de atributos extras.
     */
    public String getExtraAttribute(String attribute) {
        if (attributes.containsKey(attribute)) return attributes.get(attribute);
            else throw new InvalidCredentialException("Atributo n�o preenchido.");
    }
    /**
     * Obt�m os atributos extras do usu�rio em um mapa.
     *
     * @return Um mapa contendo os atributos extras e seus valores.
     */
    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }
    /**
     * Define os atributos extras do usu�rio a partir de um mapa.
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
     * Adiciona um recado � caixa de mensagens do usu�rio.
     *
     * @param message O recado a ser adicionado.
     */
    public void receiveMessage(Message message){
        this.messageBox.add(message);
    }
    /**
     * Verifica se o usu�rio e f� de um usu�rio recebido.
     * @param idol O usu�rio a ser verificado.
     * @return `true` se for f�, `false` caso contr�rio.
     */
    public boolean isFan(String idol){return myRelationships.getIdols().contains(idol);}
    /**
     * Verifica se um usu�rio recebido � paquera do usu�rio.
     * @param crush O usu�rio a ser verificado.
     * @return `true` se for paquera, `false` caso contr�rio.
     */
    public boolean isCrush(String crush){
        return myRelationships.getCrush().contains(crush);
    }
    /**
     * Verifica se um usu�rio recebido � amigo do usu�rio.
     * @param friend O usu�rio a ser verificado.
     * @return `true` se for amigo, `false` caso contr�rio.
     */
    public boolean isFriend(String friend){
        return myRelationships.getFriendsList().contains(friend);
    }
    /**
     * Verifica se um usu�rio recebido � inimigo do usu�rio.
     * @param enemy O usu�rio a ser verificado.
     * @return `true` se for inimigo, `false` caso contr�rio.
     */
    public boolean isEnemy(String enemy){
        return myRelationships.getEnemy().contains(enemy);
    }
    /**
     * Obt�m a lista de amigos do usu�rio em formato de String.
     * @return Uma String que representa a lista de amigos do usu�rio no formato JSON.
     *         Se a lista estiver vazia, retorna "{}".
     */
    public String getFriendList(){
        ArrayList<String> friends = this.myRelationships.getFriendsList();
        return friends.isEmpty() ? "{}" : "{" + String.join(",", friends) + "}";
    }
    /**
     * L� o primeiro recado da caixa de mensagens de um usu�rio.
     * @throws RuntimeException Se n�o houver recados na caixa de mensagens.
     */
    public String readMessage(){
        Message message = this.messageBox.poll();
        if(message == null) throw new MessageNotFoundException("N�o h� recados.");
        else return message.getMessage();
    }
    /**
     * L� a primeira mensagem da caixa de mensagens da comunidade.
     *
     * @return O conte�do da mensagem lida.
     * @throws MessageNotFoundException Se n�o houver mensagens na caixa de mensagens da comunidade.
     */
    public String readCommunityMessage(){
        Message message = this.communityMessages.poll();
        if(message == null) throw new MessageNotFoundException("N�o h� mensagens.");
        else return message.getMessage();

    }
    /**
     * Obt�m o login do usu�rio.
     * @return O login do usu�rio.
     */
    public String getLogin() {
        return login;
    }
    /**
     * Obt�m o nome do usu�rio.
     * @return O nome do usu�rio.
     */
    public String getName() {
        return name;
    }
    /**
     * Obt�m a senha do usu�rio.
     * @return A senha do usu�rio.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Adiciona o nome de uma comunidade � lista de comunidades �s quais o usu�rio pertence.
     * @param name O nome da comunidade a ser adicionado.
     */
    public void addCommunity(String name){
        this.myCommunities.add(name);
    }
    /**
     * Obt�m a lista de comunidades �s quais o usu�rio pertence.
     * @return Uma lista de nomes de comunidades.
     */
    public ArrayList<String> getMyCommunities() {
        return myCommunities;
    }
    /**
     * Define a lista de comunidades �s quais o usu�rio pertence.
     * @param myCommunities Uma lista de nomes de comunidades.
     */
    public void setMyCommunities(ArrayList<String> myCommunities) {
        this.myCommunities = myCommunities;
    }
    /**
     * Obt�m a caixa de mensagens da comunidade.
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
