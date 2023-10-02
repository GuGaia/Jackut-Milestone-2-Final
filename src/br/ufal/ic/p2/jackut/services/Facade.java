package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import java.util.ArrayList;

import static br.ufal.ic.p2.jackut.services.JackutSystemManager.SYSTEM;

/**
 * A classe Facade oferece uma interface para acessar as funcionalidades do sistema Jackut.
 * Ela fornece m�todos para criar usu�rios, gerenciar sess�es, editar perfis, interagir com amigos,
 * enviar e ler mensagens, criar comunidades e realizar v�rias opera��es relacionadas ao sistema.
 * A classe Facade � projetada para simplificar a intera��o com o sistema Jackut e fornecer uma
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
     * M�todo zerarSistema exclui todos os dados do sistema, limpando os mapas de usu�rios, sess�es e arquivo de dados.
     */
    public void zerarSistema(){
        SYSTEM.cleanSystem();
    }
    /**
     * Cria um novo usu�rio com as informa��es fornecidas e o adiciona ao sistema.
     * @param login O login do novo usu�rio.
     * @param senha A senha do novo usu�rio.
     * @param nome O nome do novo usu�rio.
     */
    public void criarUsuario(String login, String senha, String nome){
        SYSTEM.createUser(login, senha, nome);
    }
    /**
     * Obt�m o valor de um atributo espec�fico para um usu�rio desejado a partir de seu login.
     *
     * @param login O login do usu�rio.
     * @param atributo O atributo desejado ("nome", "senha", "login" ou atributo extra).
     * @return O valor do atributo solicitado.
     */
    public String getAtributoUsuario(String login, String atributo){
        return SYSTEM.getUser(login).getUserAttribute(atributo);
    }
    /**
     * Abre uma sess�o para um usu�rio autenticado.
     *
     * @param login O login do usu�rio.
     * @param senha A senha do usu�rio.
     * @return O ID da sess�o.
     */
    public String abrirSessao (String login, String senha){
        return SYSTEM.openSession(login, senha);
    }
    /**
     * Edita o perfil de um usu�rio autenticado.
     *
     * @param Id O ID da sess�o do usu�rio.
     * @param atributo O atributo a ser editado ("nome", "senha", "login" ou atributo extra).
     * @param valor O novo valor para o atributo.
     */
    public void editarPerfil(String Id, String atributo, String valor){
        SYSTEM.getSession(Id).editProfile(atributo, valor);
    }
    /**
     * Verifica se um usu�rio � amigo de outro usu�rio.
     *
     * @param login O login do primeiro usu�rio.
     * @param amigo O login do segundo usu�rio.
     * @return `true` se forem amigos, `false` caso contr�rio.
     */
    public boolean ehAmigo(String login, String amigo){
        return SYSTEM.getUser(login). isFriend(amigo);
    }
    /**
     * Obt�m a lista de amigos de um usu�rio.
     *
     * @param login O login do usu�rio.
     * @return Uma representa��o da lista de amigos.
     */
    public String getAmigos(String login){
        return  SYSTEM.getUser(login).getFriendList();
    }
    /**
     * Adiciona um amigo a um usu�rio com base em seu ID de sess�o e login de amigo.
     *
     * @param id O ID da sess�o do usu�rio.
     * @param login O login do amigo a ser adicionado.
     */
    public void adicionarAmigo(String id, String login) {
        SYSTEM.getSession(id).addFriend(SYSTEM.getUser(login));
    }
    /**
     * Envia um recado de um usu�rio para outro.
     *
     * @param id O ID da sess�o do remetente.
     * @param destinatario O login do destinat�rio do recado.
     * @param mensagem O conte�do da mensagem.
     * @throws RuntimeException Se os usu�rios n�o forem encontrados.
     */
    public void enviarRecado(String id, String destinatario, String mensagem){
        SYSTEM.getSession(id).messageSender(destinatario, mensagem, new UserMessageStrategy());
    }
    /**
     * L� o primeiro recado da caixa de mensagens de um usu�rio.
     *
     * @param id O ID da sess�o do usu�rio.
     * @return O conte�do do recado lido.
     * @throws RuntimeException Se n�o houver recados na caixa de mensagens.
     */
    public String lerRecado(String id){
        return SYSTEM.getSession(id).getUser().readMessage();
    }
    /**
     * Cria uma nova comunidade com nome e descri��o especificados.
     *
     * @param sessao O ID da sess�o do usu�rio criador da comunidade.
     * @param nome O nome da comunidade.
     * @param descricao A descri��o da comunidade.
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
     * Obt�m a descri��o de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return A descri��o da comunidade.
     */
    public String getDescricaoComunidade(String nome){
        return SYSTEM.getCommunity(nome).getDescription();
    }
    /**
     * Obt�m o nome do dono de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return O nome do dono da comunidade.
     */
    public String getDonoComunidade(String nome){
        return SYSTEM.getCommunity(nome).getManager();
    }
    /**
     * Obt�m a lista de membros de uma comunidade com base em seu nome.
     *
     * @param nome O nome da comunidade.
     * @return Uma representa��o da lista de membros.
     */
    public String getMembrosComunidade(String nome) {
        ArrayList<String> members = SYSTEM.getCommunity(nome).getMembers();
        return "{" + String.join(",", members) + "}";
    }
    /**
     * Obt�m a lista de comunidades a que um usu�rio pertence com base em seu login.
     *
     * @param login O login do usu�rio.
     * @return Uma representa��o da lista de comunidades.
     */
    public String getComunidades(String login){
        ArrayList<String> communities = SYSTEM.getUser(login).getMyCommunities();
        return "{" + String.join(",", communities) + "}";
    }
    /**
     * Adiciona um usu�rio a uma comunidade com base em seu ID de sess�o e o nome da comunidade.
     *
     * @param sessao O ID da sess�o do usu�rio.
     * @param nome O nome da comunidade � qual o usu�rio ser� adicionado.
     */
    public void adicionarComunidade(String sessao, String nome){
        SYSTEM.getSession(sessao).joinCommunity(SYSTEM.getCommunity(nome));
    }
    /**
     * L� a primeira mensagem da caixa de mensagens da comunidade com base em seu ID de sess�o.
     *
     * @param id O ID da sess�o do usu�rio.
     * @return O conte�do da mensagem lida.
     * @throws RuntimeException Se n�o houver mensagens na caixa de mensagens da comunidade.
     */
    public String lerMensagem(String id){
        return SYSTEM.getSession(id).readCommunityMessages();
    }
    /**
     * Envia uma mensagem para uma comunidade com base em seu ID de sess�o, o nome da comunidade e o conte�do da mensagem.
     *
     * @param id O ID da sess�o do remetente.
     * @param comunidade O nome da comunidade de destino da mensagem.
     * @param mensagem O conte�do da mensagem.
     */
    public void enviarMensagem(String id, String comunidade, String mensagem){
        SYSTEM.getSession(id).messageSender(comunidade, mensagem, new CommunityMessageStrategy());
    }
    /**
     * Verifica se um usu�rio � f� de outro usu�rio com base em seus logins.
     *
     * @param login O login do primeiro usu�rio.
     * @param idolo O login do segundo usu�rio.
     * @return `true` se o primeiro usu�rio for f� do segundo, `false` caso contr�rio.
     */
    public boolean ehFa(String login, String idolo){
        return SYSTEM.getUser(login).isFan(idolo);
    }
    /**
     * Adiciona um usu�rio como f� de outro usu�rio com base em seus IDs de sess�o.
     *
     * @param id O ID da sess�o do usu�rio que se tornar� f�.
     * @param idolo O login do usu�rio a ser seguido como �dolo.
     */
    public void adicionarIdolo(String id, String idolo){
        User idol = SYSTEM.getUser(idolo);
        SYSTEM.getSession(id).addIdol(idol);
    }
    /**
     * Verifica se um usu�rio � paquera de outro usu�rio com base em seus IDs de sess�o.
     *
     * @param id O ID da sess�o do usu�rio.
     * @param paquera O login do usu�rio que est� sendo verificado como paquera.
     * @return `true` se o primeiro usu�rio for paquera do segundo, `false` caso contr�rio.
     */
    public boolean ehPaquera(String id, String paquera){
        return SYSTEM.getSession(id).getUser().isCrush(paquera);
    }
    /**
     * Adiciona um usu�rio como paquera de outro usu�rio com base em seus IDs de sess�o.
     *
     * @param id O ID da sess�o do usu�rio que est� adicionando a paquera.
     * @param paquera O login do usu�rio que ser� adicionado como paquera.
     */
    public void adicionarPaquera(String id, String paquera){
        User crush = SYSTEM.getUser(paquera);
        SYSTEM.getSession(id).addCrush(crush);
    }
    /**
     * Obt�m a lista de f�s de um usu�rio com base em seu login.
     *
     * @param login O login do usu�rio.
     * @return Uma representa��o da lista de f�s.
     */
    public String getFas(String login){
        ArrayList<String> fans = SYSTEM.getUser(login).getMyRelationships().getFans();
        return "{" + String.join(",", fans) + "}";
    }
    /**
     * Obt�m a lista de paqueras de um usu�rio com base em seu ID de sess�o.
     *
     * @param id O ID da sess�o do usu�rio.
     * @return Uma representa��o da lista de paqueras.
     */
    public String getPaqueras(String id){
        ArrayList<String> paqueras = SYSTEM.getSession(id).getUser().getMyRelationships().getCrush();
        return "{" + String.join(",", paqueras) + "}";
    }
    /**
     * Adiciona um usu�rio como inimigo de outro usu�rio com base em seus IDs de sess�o.
     *
     * @param id O ID da sess�o do usu�rio que est� adicionando o inimigo.
     * @param inimigo O login do usu�rio que ser� adicionado como inimigo.
     */
    public void adicionarInimigo(String id, String inimigo){
        User enemy = SYSTEM.getUser(inimigo);
        SYSTEM.getSession(id).addEnemy(enemy.getLogin());
    }
    /**
     * Remove um usu�rio do sistema com base em seu ID de sess�o.
     *
     * @param id O ID da sess�o do usu�rio a ser removido.
     */
    public void removerUsuario(String id){
        SYSTEM.deleteAccount(id);
    }
}


