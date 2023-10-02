package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
/**
 * A classe Relationships representa os relacionamentos de um usuário dentro do sistema Jackut.
 * Isso inclui listas de amigos, solicitações de amizade pendentes, ídolos, fãs, paqueras e inimigos.
 */
public class Relationships {
    private ArrayList<String> friendsList; // Lista de amigos do usuário.
    private ArrayList<String> friendSolicitation; // Lista de solicitações de amizade pendentes.
    private ArrayList<String> idols; // Lista de ídolos do usuário.
    private ArrayList<String> fans; // Lista de fãs do usuário.
    private ArrayList<String> crush; // Lista de paqueras do usuário.
    private ArrayList<String> enemy; // Lista de inimigos do usuário.

    /**
     * Construtor da classe Relationships.
     * Inicializa todas as listas como vazias.
     */
    @JsonCreator
    public Relationships() {
       friendsList = new ArrayList<>();
       friendSolicitation = new ArrayList<>();
       idols = new ArrayList<>();
       fans = new ArrayList<>();
       crush = new ArrayList<>();
       enemy = new ArrayList<>();
    }
    /**
     * Obtém a lista de amigos do usuário
     * @return lista de amigos do usuário
     */
    public ArrayList<String> getFriendsList() {
        return friendsList;
    }
    /**
     * Obtém a lista de solicitações de amizade do usuário
     * @return lista de solicitações de amizade do usuário
     */
    public ArrayList<String> getFriendSolicitation() {
        return friendSolicitation;
    }

    /**
     * Atualiza a lista de amigos do usuário
     * @param friends nova lista de amigos do usuário
     */
    public void setFriends(ArrayList<String> friends) {
        this.friendsList = friends;
    }
    /**
     * Atualiza a lista de solicitações de amizade do usuário
     * @param friendSolicitation nova lista de solicitações de amizade do usuário
     */
    public void setFriendSolicitation(ArrayList<String> friendSolicitation) {
        this.friendSolicitation = friendSolicitation;
    }
    /**
     * Adiciona um amigo à lista de amigos do usuário, removendo a solicitação de amizade, se existir.
     *
     * @param friend O login do amigo a ser adicionado.
     */
    public void addFriends(String friend) {
        this.friendSolicitation.remove(friend);
        this.friendsList.add(friend);
    }
    /**
     * Adiciona uma solicitação de amizade à lista de solicitações pendentes.
     *
     * @param friendSolicitation O login do usuário que enviou a solicitação de amizade.
     */
    public void addFriendSolicitation(String friendSolicitation) {
        this.friendSolicitation.add(friendSolicitation);
    }
    /**
     * Adiciona um ídolo à lista de ídolos do usuário.
     *
     * @param idol O login do ídolo a ser adicionado.
     * @throws UserDuplicationException Se o usuário já estiver adicionado como ídolo.
     */
    public void addIdol(String idol){
        if(this.idols.contains(idol)) throw new UserDuplicationException("Usuário já está adicionado como ídolo.");
        else this.idols.add(idol);
    }
    /**
     * Adiciona um fã à lista de fãs do usuário.
     *
     * @param fan O login do fã a ser adicionado.
     * @throws UserDuplicationException Se o usuário já estiver adicionado como fã.
     */
    public void addFan(String fan){
        if(this.fans.contains(fan)) throw new UserDuplicationException("Usuário já está adicionado como fã.");
        else this.fans.add(fan);
    }
    /**
     * Adiciona uma paquera à lista de paqueras do usuário.
     *
     * @param crush O login da paquera a ser adicionada.
     * @throws UserDuplicationException Se o usuário já estiver adicionado como paquera.
     */
    public void addCrush(String crush){
        if (this.crush.contains(crush)) throw new UserDuplicationException("Usuário já está adicionado como paquera.");
        else this.crush.add(crush);
    }
    /**
     * Adiciona um inimigo à lista de inimigos do usuário.
     *
     * @param enemy O login do inimigo a ser adicionado.
     * @throws UserDuplicationException Se o usuário já estiver adicionado como inimigo.
     */
    public void addEnemy(String enemy){
        if (this.enemy.contains(enemy)) throw new UserDuplicationException("Usuário já está adicionado como inimigo.");
        else this.enemy.add(enemy);
    }
    /**
     * Obtém a lista de ídolos do usuário.
     * @return A lista de ídolos do usuário.
     */
    public ArrayList<String> getIdols() {
        return idols;
    }
    /**
     * Obtém a lista de fãs do usuário.
     * @return A lista de fãs do usuário.
     */
    public ArrayList<String> getFans() {
        return fans;
    }
    /**
     * Obtém a lista de paqueras do usuário.
     * @return A lista de paqueras do usuário.
     */
    public ArrayList<String> getCrush() {
        return crush;
    }
    /**
     * Obtém a lista de inimigos do usuário.
     * @return A lista de inimigos do usuário.
     */
    public ArrayList<String> getEnemy() {
        return enemy;
    }
}