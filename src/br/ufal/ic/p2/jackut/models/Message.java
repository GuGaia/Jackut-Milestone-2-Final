package br.ufal.ic.p2.jackut.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * A classe Recado representa uma mensagem enviada por um remetente.
 *
 * @author Gustavo Gaia
 */
public class Message {
    private String remetente;
    private String message;

    /**
     * Construtor da classe Recado
     */
    public Message(){};
    /**
     * Construtor da classe Recado nas configura��es para armazenamento JSON.
     *
     * @param remetente O remetente da mensagem.
     * @param mensagem O conte�do da mensagem.
     */
    @JsonCreator
    public Message(@JsonProperty("remetente") String remetente, @JsonProperty("mensagem") String mensagem) {
        this.remetente = remetente;
        this.message = mensagem;
    }
    /**
     * Obt�m o remetende da mensagem
     * @return remetende da mensagem
     */
    public String getRemetente() {
        return remetente;
    }
    /**
     * Obt�m a mensagem
     * @return a mensagem
     */
    public String getMessage() {
        return message;
    }

}
