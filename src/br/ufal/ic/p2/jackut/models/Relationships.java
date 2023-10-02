package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
/**
 * A classe Relationships representa os relacionamentos de um usu�rio dentro do sistema Jackut.
 * Isso inclui listas de amigos, solicita��es de amizade pendentes, �dolos, f�s, paqueras e inimigos.
 */
public class Relationships {
    private ArrayList<String> friendsList; // Lista de amigos do usu�rio.
    private ArrayList<String> friendSolicitation; // Lista de solicita��es de amizade pendentes.
    private ArrayList<String> idols; // Lista de �dolos do usu�rio.
    private ArrayList<String> fans; // Lista de f�s do usu�rio.
    private ArrayList<String> crush; // Lista de paqueras do usu�rio.
    private ArrayList<String> enemy; // Lista de inimigos do usu�rio.

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
     * Obt�m a lista de amigos do usu�rio
     * @return lista de amigos do usu�rio
     */
    public ArrayList<String> getFriendsList() {
        return friendsList;
    }
    /**
     * Obt�m a lista de solicita��es de amizade do usu�rio
     * @return lista de solicita��es de amizade do usu�rio
     */
    public ArrayList<String> getFriendSolicitation() {
        return friendSolicitation;
    }

    /**
     * Atualiza a lista de amigos do usu�rio
     * @param friends nova lista de amigos do usu�rio
     */
    public void setFriends(ArrayList<String> friends) {
        this.friendsList = friends;
    }
    /**
     * Atualiza a lista de solicita��es de amizade do usu�rio
     * @param friendSolicitation nova lista de solicita��es de amizade do usu�rio
     */
    public void setFriendSolicitation(ArrayList<String> friendSolicitation) {
        this.friendSolicitation = friendSolicitation;
    }
    /**
     * Adiciona um amigo � lista de amigos do usu�rio, removendo a solicita��o de amizade, se existir.
     *
     * @param friend O login do amigo a ser adicionado.
     */
    public void addFriends(String friend) {
        this.friendSolicitation.remove(friend);
        this.friendsList.add(friend);
    }
    /**
     * Adiciona uma solicita��o de amizade � lista de solicita��es pendentes.
     *
     * @param friendSolicitation O login do usu�rio que enviou a solicita��o de amizade.
     */
    public void addFriendSolicitation(String friendSolicitation) {
        this.friendSolicitation.add(friendSolicitation);
    }
    /**
     * Adiciona um �dolo � lista de �dolos do usu�rio.
     *
     * @param idol O login do �dolo a ser adicionado.
     * @throws UserDuplicationException Se o usu�rio j� estiver adicionado como �dolo.
     */
    public void addIdol(String idol){
        if(this.idols.contains(idol)) throw new UserDuplicationException("Usu�rio j� est� adicionado como �dolo.");
        else this.idols.add(idol);
    }
    /**
     * Adiciona um f� � lista de f�s do usu�rio.
     *
     * @param fan O login do f� a ser adicionado.
     * @throws UserDuplicationException Se o usu�rio j� estiver adicionado como f�.
     */
    public void addFan(String fan){
        if(this.fans.contains(fan)) throw new UserDuplicationException("Usu�rio j� est� adicionado como f�.");
        else this.fans.add(fan);
    }
    /**
     * Adiciona uma paquera � lista de paqueras do usu�rio.
     *
     * @param crush O login da paquera a ser adicionada.
     * @throws UserDuplicationException Se o usu�rio j� estiver adicionado como paquera.
     */
    public void addCrush(String crush){
        if (this.crush.contains(crush)) throw new UserDuplicationException("Usu�rio j� est� adicionado como paquera.");
        else this.crush.add(crush);
    }
    /**
     * Adiciona um inimigo � lista de inimigos do usu�rio.
     *
     * @param enemy O login do inimigo a ser adicionado.
     * @throws UserDuplicationException Se o usu�rio j� estiver adicionado como inimigo.
     */
    public void addEnemy(String enemy){
        if (this.enemy.contains(enemy)) throw new UserDuplicationException("Usu�rio j� est� adicionado como inimigo.");
        else this.enemy.add(enemy);
    }
    /**
     * Obt�m a lista de �dolos do usu�rio.
     * @return A lista de �dolos do usu�rio.
     */
    public ArrayList<String> getIdols() {
        return idols;
    }
    /**
     * Obt�m a lista de f�s do usu�rio.
     * @return A lista de f�s do usu�rio.
     */
    public ArrayList<String> getFans() {
        return fans;
    }
    /**
     * Obt�m a lista de paqueras do usu�rio.
     * @return A lista de paqueras do usu�rio.
     */
    public ArrayList<String> getCrush() {
        return crush;
    }
    /**
     * Obt�m a lista de inimigos do usu�rio.
     * @return A lista de inimigos do usu�rio.
     */
    public ArrayList<String> getEnemy() {
        return enemy;
    }
}