package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import java.util.ArrayList;

import static br.ufal.ic.p2.jackut.services.JackutSystemManager.SYSTEM;

/**
 * A classe Facade oferece uma interface para acessar as funcionalidades do sistema Jackut.
 * Ela fornece métodos para criar usuários, gerenciar sessões, editar perfis, interagir com amigos,
 * enviar e ler mensagens, criar comunidades e realizar várias operações relacionadas ao sistema.
 * A classe Facade é projetada para simplificar a interação com o sistema Jackut e fornecer uma
 * maneira conveniente de acessar suas funcionalidades.
 * @author Gustavo Gaia
 */
public class Facade {
    /**
     * Construtor da classe Facade.
     * Inicializa o sistema.
     */
    public Facade() {
    }
    /**
     * Método zerarSistema exclui todos os dados do sistema, limpando os mapas de usuários, sessões e arquivo de dados.
     */
    public void zerarSistema(){
        SYSTEM.cleanSystem();
    }
    /**
     * Cria um novo usuário com as informações fornecidas e o adiciona ao sistema.
     * @param login O login do novo usuário.
     * @param senha A senha do novo usuário.
     * @param nome O nome do novo usuário.
     */
    public void criarUsuario(String login, String senha, String nome){
        SYSTEM.createUser(login, senha, nome);
    }
    /**
     * Obtém o valor de um atributo específico para um usuário desejado a partir de seu login.
     *
     * @param login O login do usuário.
     * @param atributo O atributo desejado ("nome", "senha", "login" ou atributo extra).
     * @return O valor do atributo solicitado.
     */
    public String getAtributoUsuario(String login, String atributo){
        return SYSTEM.getUser(login).getUserAttribute(atributo);
    }
    /**
     * Abre uma sessão para um usuário autenticado.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return O ID da sessão.
     */
    public String abrirSessao (String login, String senha){
        return SYSTEM.openSession(login, senha);
    }
    /**
     * Edita o perfil de um usuário autenticado.
     *
     * @param Id O ID da sessão do usuário.
     * @param atributo O atributo a ser editado ("nome", "senha", "login" ou atributo extra).
     * @param valor O novo valor para o atributo.
     */
    public void editarPerfil(String Id, String atributo, String valor){
        SYSTEM.getSession(Id).editProfile(atributo, valor);
    }
    /**
     * Verifica se um usuário é amigo de outro usuário.
     *
     * @param login O login do primeiro usuário.
     * @param amigo O login do segundo usuário.
     * @return `true` se forem amigos, `false` caso contrário.
     */
    public boolean ehAmigo(String login, String amigo){
        return SYSTEM.getUser(login). isFriend(amigo);
    }
    /**
     * Obtém a lista de amigos de um usuário.
     *
     * @param login O login do usuário.
     * @return Uma representação da lista de amigos.
     */
    public String getAmigos(String login){
        return  SYSTEM.getUser(login).getFriendList();
    }
    /**
     * Adiciona um amigo a um usuário com base em seu ID de sessão e login de amigo.
     *
     * @param id O ID da sessão do usuário.
     * @param login O login do amigo a ser adicionado.
     */
    public void adicionarAmigo(String id, String login) {
        SYSTEM.getSession(id).addFriend(SYSTEM.getUser(login));
    }
    /**
     * Envia um recado de um usuário para outro.
     *
     * @param id O ID da sessão do remetente.
     * @param destinatario O login do destinatário do recado.
     * @param mensagem O conteúdo da mensagem.
     * @throws RuntimeException Se os usuários não forem encontrados.
     */
    public void enviarRecado(String id, String destinatario, String mensagem){
        SYSTEM.getSession(id).messageSender(destinatario, mensagem, new UserMessageStrategy());
    }
    /**
     * Lê o primeiro recado da caixa de mensagens de um usuário.
     *
     * @param id O ID da sessão do usuário.
     * @return O conteúdo do recado lido.
     * @throws RuntimeException Se não houver recados na caixa de mensagens.
     */
    public String lerRecado(String id){
        return SYSTEM.getSession(id).getUser().readMessage();
    }
    /**
     * Cria uma nova comunidade com nome e descrição especificados.
     *
     * @param sessao O ID da sessão do usuário criador da comunidade.
     * @param nome O nome da comunidade.
     * @param descricao A descrição da comunidade.
     */
    public void criarComunidade(String sessao, String nome, String descricao){
        SYSTEM.createCommunity(sessao, nome, descricao);
    }
    /**
     * Encerra o sistema, salvando os dados em um arquivo JSON.
     */
    public void encerrarSistema() {
        SYSTEM.closeSystem();
    }
    /**
     * Obtém a descrição de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return A descrição da comunidade.
     */
    public String getDescricaoComunidade(String nome){
        return SYSTEM.getCommunity(nome).getDescription();
    }
    /**
     * Obtém o nome do dono de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return O nome do dono da comunidade.
     */
    public String getDonoComunidade(String nome){
        return SYSTEM.getCommunity(nome).getManager();
    }
    /**
     * Obtém a lista de membros de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return Uma representação da lista de membros.
     */
    public String getMembrosComunidade(String nome) {
        ArrayList<String> members = SYSTEM.getCommunity(nome).getMembers();
        return "{" + String.join(",", members) + "}";
    }
    /**
     * Obtém a lista de comunidades a que um usuário pertence com base em seu login.
     *
     * @param login O login do usuário.
     * @return Uma representação da lista de comunidades.
     */
    public String getComunidades(String login){
        ArrayList<String> communities = SYSTEM.getUser(login).getMyCommunities();
        return "{" + String.join(",", communities) + "}";
    }
    /**
     * Adiciona um usuário a uma comunidade com base em seu ID de sessão e o nome da comunidade.
     *
     * @param sessao O ID da sessão do usuário.
     * @param nome O nome da comunidade à qual o usuário será adicionado.
     */
    public void adicionarComunidade(String sessao, String nome){
        SYSTEM.getSession(sessao).joinCommunity(SYSTEM.getCommunity(nome));
    }
    /**
     * Lê a primeira mensagem da caixa de mensagens da comunidade com base em seu ID de sessão.
     *
     * @param id O ID da sessão do usuário.
     * @return O conteúdo da mensagem lida.
     * @throws RuntimeException Se não houver mensagens na caixa de mensagens da comunidade.
     */
    public String lerMensagem(String id){
        return SYSTEM.getSession(id).readCommunityMessages();
    }
    /**
     * Envia uma mensagem para uma comunidade com base em seu ID de sessão, o nome da comunidade e o conteúdo da mensagem.
     *
     * @param id O ID da sessão do remetente.
     * @param comunidade O nome da comunidade de destino da mensagem.
     * @param mensagem O conteúdo da mensagem.
     */
    public void enviarMensagem(String id, String comunidade, String mensagem){
        SYSTEM.getSession(id).messageSender(comunidade, mensagem, new CommunityMessageStrategy());
    }
    /**
     * Verifica se um usuário é fã de outro usuário com base em seus logins.
     *
     * @param login O login do primeiro usuário.
     * @param idolo O login do segundo usuário.
     * @return `true` se o primeiro usuário for fã do segundo, `false` caso contrário.
     */
    public boolean ehFa(String login, String idolo){
        return SYSTEM.getUser(login).isFan(idolo);
    }
    /**
     * Adiciona um usuário como fã de outro usuário com base em seus IDs de sessão.
     *
     * @param id O ID da sessão do usuário que se tornará fã.
     * @param idolo O login do usuário a ser seguido como ídolo.
     */
    public void adicionarIdolo(String id, String idolo){
        User idol = SYSTEM.getUser(idolo);
        SYSTEM.getSession(id).addIdol(idol);
    }
    /**
     * Verifica se um usuário é paquera de outro usuário com base em seus IDs de sessão.
     *
     * @param id O ID da sessão do usuário.
     * @param paquera O login do usuário que está sendo verificado como paquera.
     * @return `true` se o primeiro usuário for paquera do segundo, `false` caso contrário.
     */
    public boolean ehPaquera(String id, String paquera){
        return SYSTEM.getSession(id).getUser().isCrush(paquera);
    }
    /**
     * Adiciona um usuário como paquera de outro usuário com base em seus IDs de sessão.
     *
     * @param id O ID da sessão do usuário que está adicionando a paquera.
     * @param paquera O login do usuário que será adicionado como paquera.
     */
    public void adicionarPaquera(String id, String paquera){
        User crush = SYSTEM.getUser(paquera);
        SYSTEM.getSession(id).addCrush(crush);
    }
    /**
     * Obtém a lista de fãs de um usuário com base em seu login.
     *
     * @param login O login do usuário.
     * @return Uma representação da lista de fãs.
     */
    public String getFas(String login){
        ArrayList<String> fans = SYSTEM.getUser(login).getMyRelationships().getFans();
        return "{" + String.join(",", fans) + "}";
    }
    /**
     * Obtém a lista de paqueras de um usuário com base em seu ID de sessão.
     *
     * @param id O ID da sessão do usuário.
     * @return Uma representação da lista de paqueras.
     */
    public String getPaqueras(String id){
        ArrayList<String> paqueras = SYSTEM.getSession(id).getUser().getMyRelationships().getCrush();
        return "{" + String.join(",", paqueras) + "}";
    }
    /**
     * Adiciona um usuário como inimigo de outro usuário com base em seus IDs de sessão.
     *
     * @param id O ID da sessão do usuário que está adicionando o inimigo.
     * @param inimigo O login do usuário que será adicionado como inimigo.
     */
    public void adicionarInimigo(String id, String inimigo){
        User enemy = SYSTEM.getUser(inimigo);
        SYSTEM.getSession(id).addEnemy(enemy.getLogin());
    }
    /**
     * Remove um usuário do sistema com base em seu ID de sessão.
     *
     * @param id O ID da sessão do usuário a ser removido.
     */
    public void removerUsuario(String id){
        SYSTEM.deleteAccount(id);
    }
}


