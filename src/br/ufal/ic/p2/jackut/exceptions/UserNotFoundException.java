package br.ufal.ic.p2.jackut.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("Usu�rio n�o cadastrado.");
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
