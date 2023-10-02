package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
/**
 * A classe `JackutSystemManager` é responsável por gerenciar todo o sistema Jackut.
 * Ela mantém os mapas de usuários, sessões e comunidades, e é projetada para inicializar
 * e carregar os dados do sistema a partir de arquivos JSON, bem como salvar os dados em
 * arquivos JSON quando o sistema é encerrado.
 *
 * A classe `JackutSystemManager` é implementada como um singleton para garantir que haja apenas
 * uma instância do gerenciador do sistema em execução.
 */
public class JackutSystemManager {

    private Map<String, User> users; // Mapa para armazenar usuários
    private Map<String, Session> sessions; // Mapa para armazenar sessões
    private Map<String, Community> communities; //Mapa de Comunidades
    private File usersData, communitiesData;// Arquivos para armazenar dados em JSON
    public static final JackutSystemManager SYSTEM = new JackutSystemManager();// Instância única do sistema
    /**
     * Construtor privado da classe `JackutSystemManager`.
     * Inicializa os mapas de usuários e sessões e carrega os dados existentes do sistema, se disponíveis.
     * Este construtor é privado para garantir que apenas uma instância seja criada.
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
     * Os dados carregados incluem informações sobre usuários, sessões e comunidades.
     * Isso é feito durante a inicialização do sistema.
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
     * Método `cleanSystem` exclui todos os dados do sistema, limpando os mapas de usuários, sessões
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
     * Método `closeSystem` encerra o sistema, salvando os dados em arquivos JSON.
     * Isso é feito quando o sistema é encerrado.
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
     * Cria um novo usuário com as informações fornecidas e o adiciona ao sistema.
     *
     * @param login O login do novo usuário.
     * @param password A senha do novo usuário.
     * @param name O nome do novo usuário.
     * @throws InvalidCredentialException Se o login ou a senha forem inválidos.
     * @throws InvalidCredentialException Se um usuário com o mesmo login já existir.
     */
    public void createUser(String login, String password, String name){
        if (!users.containsKey(login)) {
            if(login == null) throw new InvalidCredentialException("Login inválido.");
            if (password == null) throw new InvalidCredentialException("Senha inválida.");
            User user = new User(login, password, name);
            users.put(login, user);
        }
        else throw new InvalidCredentialException("Conta com esse nome já existe.");
    }
    /**
     * Obtém um usuário com base em seu login.
     *
     * @param login O login do usuário a ser obtido.
     * @return O usuário correspondente ao login.
     * @throws UserNotFoundException Se o usuário com o login especificado não for encontrado.
     */
    public User getUser(String login){
        if (users.containsKey(login)) return users.get(login);
        else throw new UserNotFoundException();
    }
    /**
     * Abre uma sessão para um usuário autenticado com base em seu login e senha.
     *
     * @param login O login do usuário.
     * @param password A senha do usuário.
     * @return O ID da sessão aberta.
     * @throws InvalidCredentialException Se o login ou a senha forem inválidos.
     * @throws InvalidCredentialException Se o login e senha não corresponderem a um usuário válido.
     */
    public String openSession (String login, String password) {
        User user = users.get(login);
        if(user != null && user.verifyPassword(password)){
            Session session = new Session(user);
            sessions.put(session.getID(), session);
            return session.getID();
        }
        else throw new InvalidCredentialException("Login ou senha inválidos.");
    }
    /**
     * Obtém uma sessão com base em seu ID.
     *
     * @param id O ID da sessão a ser obtida.
     * @return A sessão correspondente ao ID.
     * @throws UserNotFoundException Se a sessão com o ID especificado não for encontrada.
     */
    public Session getSession(String id){
        if (sessions.containsKey(id)) return sessions.get(id);
        else throw new UserNotFoundException();
    }
    /**
     * Verifica se um usuário com o login especificado existe no sistema.
     *
     * @param login O login do usuário a ser verificado.
     * @return `true` se o usuário existir, `false` caso contrário.
     */
    public boolean verifyUser(String login){
        return users.containsKey(login);
    }
    /**
     * Cria uma nova comunidade com base no nome e na descrição fornecidos e a adiciona ao sistema.
     *
     * @param session A sessão do usuário que está criando a comunidade.
     * @param name O nome da nova comunidade.
     * @param description A descrição da nova comunidade.
     * @throws InvalidCommunityException Se uma comunidade com o mesmo nome já existir.
     */
    public void createCommunity(String session, String name, String description) {
        if (communities.containsKey(name)) {
            throw new InvalidCommunityException("Comunidade com esse nome já existe.");
        } else{
            Community community = getSession(session).createCommunity(name, description);
            communities.put(name, community);
        }
    }
    /**
     * Obtém uma comunidade com base em seu nome.
     *
     * @param name O nome da comunidade a ser obtida.
     * @return A comunidade correspondente ao nome.
     * @throws InvalidCommunityException Se a comunidade com o nome especificado não for encontrada.
     */
    public Community getCommunity(String name) {
        if(communities.containsKey(name)) return communities.get(name);
        else throw new InvalidCommunityException("Comunidade não existe.");
    }
    /**
     * Exclui uma conta de usuário do sistema com base na sessão do usuário.
     *
     * @param id O ID da sessão do usuário que deseja excluir sua conta.
     * @throws UserNotFoundException Se a conta de usuário especificada não for encontrada.
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
