package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
/**
 * A classe `JackutSystemManager` � respons�vel por gerenciar todo o sistema Jackut.
 * Ela mant�m os mapas de usu�rios, sess�es e comunidades, e � projetada para inicializar
 * e carregar os dados do sistema a partir de arquivos JSON, bem como salvar os dados em
 * arquivos JSON quando o sistema � encerrado.
 *
 * A classe `JackutSystemManager` � implementada como um singleton para garantir que haja apenas
 * uma inst�ncia do gerenciador do sistema em execu��o.
 */
public class JackutSystemManager {

    private Map<String, User> users; // Mapa para armazenar usu�rios
    private Map<String, Session> sessions; // Mapa para armazenar sess�es
    private Map<String, Community> communities; //Mapa de Comunidades
    private File usersData, communitiesData;// Arquivos para armazenar dados em JSON
    public static final JackutSystemManager SYSTEM = new JackutSystemManager();// Inst�ncia �nica do sistema
    /**
     * Construtor privado da classe `JackutSystemManager`.
     * Inicializa os mapas de usu�rios e sess�es e carrega os dados existentes do sistema, se dispon�veis.
     * Este construtor � privado para garantir que apenas uma inst�ncia seja criada.
     */
    private JackutSystemManager() {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        this.communities = new HashMap<>();
        this.usersData = new File("usuarios.json");
        this.communitiesData =  new File("comunidades.json");
        loadSystem();
    }
    /**
     * Carrega os dados do sistema a partir de arquivos JSON, se existirem.
     * Os dados carregados incluem informa��es sobre usu�rios, sess�es e comunidades.
     * Isso � feito durante a inicializa��o do sistema.
     */
    public void loadSystem(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            if(usersData.exists() && communitiesData.exists()){
                List<User> usersList = objectMapper.readValue(usersData, new TypeReference<List<User>>() {});
                List<Community> communitiesList = objectMapper.readValue(communitiesData, new TypeReference<List<Community>>() {});

                for (User user : usersList) {
                    createUser(user.getLogin(), user.getPassword(), user.getName());
                    User newUser = users.get(user.getUserAttribute("login"));
                    newUser.setMyRelationships(user.getMyRelationships());
                    newUser.setMessageBox(user.getMessageBox());
                    newUser.setMyCommunities(user.getMyCommunities());
                    newUser.setCommunityMessages(user.getCommunityMessages());
                    for(Map.Entry<String, String> entry : user.getAttributes().entrySet()){
                        newUser.setAttributes(entry.getKey(), entry.getValue());}
                }
                for (Community community : communitiesList){
                    Community newCommunity = new Community(community.getName(), community.getDescription(), community.getManager());
                    communities.put(newCommunity.getName(), newCommunity);
                    newCommunity.setMembers(community.getMembers());
                }
                System.out.println("Dados carregados com sucesso");
            }
        } catch (IOException e){
            System.err.println("Erro ao carregar dados do JSON.");
            e.printStackTrace();
        }
    }
    /**
     * M�todo `cleanSystem` exclui todos os dados do sistema, limpando os mapas de usu�rios, sess�es
     * e excluindo os arquivos de dados JSON.
     */
    public void cleanSystem(){
        users.clear();
        sessions.clear();
        communities.clear();
        usersData.delete();
        communitiesData.delete();
    }
    /**
     * M�todo `closeSystem` encerra o sistema, salvando os dados em arquivos JSON.
     * Isso � feito quando o sistema � encerrado.
     */
    public void closeSystem() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<User> usersList = new ArrayList<>(users.values());
            List<Community> communityList = new ArrayList<>(communities.values());

            objectMapper.writeValue(usersData, usersList);
            objectMapper.writeValue(communitiesData, communityList);

            System.out.println("Todos os dados foram salvos.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados.");
            e.printStackTrace();
        }
    }
    /**
     * Cria um novo usu�rio com as informa��es fornecidas e o adiciona ao sistema.
     *
     * @param login O login do novo usu�rio.
     * @param password A senha do novo usu�rio.
     * @param name O nome do novo usu�rio.
     * @throws InvalidCredentialException Se o login ou a senha forem inv�lidos.
     * @throws InvalidCredentialException Se um usu�rio com o mesmo login j� existir.
     */
    public void createUser(String login, String password, String name){
        if (!users.containsKey(login)) {
            if(login == null) throw new InvalidCredentialException("Login inv�lido.");
            if (password == null) throw new InvalidCredentialException("Senha inv�lida.");
            User user = new User(login, password, name);
            users.put(login, user);
        }
        else throw new InvalidCredentialException("Conta com esse nome j� existe.");
    }
    /**
     * Obt�m um usu�rio com base em seu login.
     *
     * @param login O login do usu�rio a ser obtido.
     * @return O usu�rio correspondente ao login.
     * @throws UserNotFoundException Se o usu�rio com o login especificado n�o for encontrado.
     */
    public User getUser(String login){
        if (users.containsKey(login)) return users.get(login);
        else throw new UserNotFoundException();
    }
    /**
     * Abre uma sess�o para um usu�rio autenticado com base em seu login e senha.
     *
     * @param login O login do usu�rio.
     * @param password A senha do usu�rio.
     * @return O ID da sess�o aberta.
     * @throws InvalidCredentialException Se o login ou a senha forem inv�lidos.
     * @throws InvalidCredentialException Se o login e senha n�o corresponderem a um usu�rio v�lido.
     */
    public String openSession (String login, String password) {
        User user = users.get(login);
        if(user != null && user.verifyPassword(password)){
            Session session = new Session(user);
            sessions.put(session.getID(), session);
            return session.getID();
        }
        else throw new InvalidCredentialException("Login ou senha inv�lidos.");
    }
    /**
     * Obt�m uma sess�o com base em seu ID.
     *
     * @param id O ID da sess�o a ser obtida.
     * @return A sess�o correspondente ao ID.
     * @throws UserNotFoundException Se a sess�o com o ID especificado n�o for encontrada.
     */
    public Session getSession(String id){
        if (sessions.containsKey(id)) return sessions.get(id);
        else throw new UserNotFoundException();
    }
    /**
     * Verifica se um usu�rio com o login especificado existe no sistema.
     *
     * @param login O login do usu�rio a ser verificado.
     * @return `true` se o usu�rio existir, `false` caso contr�rio.
     */
    public boolean verifyUser(String login){
        return users.containsKey(login);
    }
    /**
     * Cria uma nova comunidade com base no nome e na descri��o fornecidos e a adiciona ao sistema.
     *
     * @param session A sess�o do usu�rio que est� criando a comunidade.
     * @param name O nome da nova comunidade.
     * @param description A descri��o da nova comunidade.
     * @throws InvalidCommunityException Se uma comunidade com o mesmo nome j� existir.
     */
    public void createCommunity(String session, String name, String description) {
        if (communities.containsKey(name)) {
            throw new InvalidCommunityException("Comunidade com esse nome j� existe.");
        } else{
            Community community = getSession(session).createCommunity(name, description);
            communities.put(name, community);
        }
    }
    /**
     * Obt�m uma comunidade com base em seu nome.
     *
     * @param name O nome da comunidade a ser obtida.
     * @return A comunidade correspondente ao nome.
     * @throws InvalidCommunityException Se a comunidade com o nome especificado n�o for encontrada.
     */
    public Community getCommunity(String name) {
        if(communities.containsKey(name)) return communities.get(name);
        else throw new InvalidCommunityException("Comunidade n�o existe.");
    }
    /**
     * Exclui uma conta de usu�rio do sistema com base na sess�o do usu�rio.
     *
     * @param id O ID da sess�o do usu�rio que deseja excluir sua conta.
     * @throws UserNotFoundException Se a conta de usu�rio especificada n�o for encontrada.
     */
    public void deleteAccount(String id){
        if (!users.containsKey(getSession(id).getUser().getLogin())) throw new UserNotFoundException();
        User deletedUser = getSession(id).getUser();
        ArrayList<String> deletedCommunities = deletedUser.getMyCommunities();
        users.remove(deletedUser.getLogin(), deletedUser);
        for (String community : deletedCommunities){
            this.communities.remove(community);
        }
        for(User user : users.values()){
            for (String community : deletedCommunities){
                user.getMyCommunities().remove(community);
            }
            user.getMessageBox().removeIf(message -> Objects.equals(message.getRemetente(), deletedUser.getLogin()));
        }
    }

}
