package br.ufal.ic.p2.jackut.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
/**
 * A classe Community representa uma comunidade dentro do sistema Jackut.
 * Cada comunidade tem um nome, uma descrição, um gerente e uma lista de membros.
 * Os membros são usuários que fazem parte dessa comunidade.
 */
public class Community {
    private String name; // O nome da comunidade.
    private String description; // A descrição da comunidade.
    private String manager; // O login do gerente da comunidade.
    private ArrayList<String> members; // A lista de membros da comunidade.

    /**
     * Construtor da classe Community.
     *
     * @param name O nome da comunidade.
     * @param description A descrição da comunidade.
     * @param manager O login do gerente da comunidade.
     */
    @JsonCreator
    public Community(@JsonProperty("name") String name, @JsonProperty("description")String description, @JsonProperty("manager")String manager){
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.members = new ArrayList<>();
    }
    /**
     * Obtém a descrição da comunidade.
     *
     * @return A descrição da comunidade.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Obtém o login do gerente da comunidade.
     *
     * @return O login do gerente da comunidade.
     */
    public String getManager() {
        return manager;
    }
    /**
     * Obtém a lista de membros da comunidade.
     *
     * @return A lista de membros da comunidade.
     */
    public ArrayList<String> getMembers() {
        return members;
    }
    /**
     * Obtém o nome da comunidade.
     *
     * @return O nome da comunidade.
     */
    public String getName() {
        return name;
    }
    /**
     * Define o nome da comunidade.
     *
     * @param name O novo nome da comunidade.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Define a descrição da comunidade.
     *
     * @param description A nova descrição da comunidade.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Define o gerente da comunidade.
     *
     * @param manager O novo gerente da comunidade.
     */
    public void setManager(String manager) {
        this.manager = manager;
    }
    /**
     * Define a lista de membros da comunidade.
     *
     * @param members A nova lista de membros da comunidade.
     */
    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    /**
     * Adiciona um usuário à lista de membros da comunidade.
     *
     * @param user O usuário a ser adicionado à comunidade.
     */
    public void addMember(User user) {
        members.add(user.getLogin());
    }
}

