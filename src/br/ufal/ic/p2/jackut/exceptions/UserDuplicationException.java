package br.ufal.ic.p2.jackut.exceptions;

public class UserDuplicationException extends RuntimeException {
    public UserDuplicationException(){
        super("Usuário já existe.");
    }
    public UserDuplicationException(String message){
        super(message);
    }
}
