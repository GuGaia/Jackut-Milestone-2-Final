package br.ufal.ic.p2.jackut.exceptions;

public class InvalidFunctionException extends RuntimeException {
    public InvalidFunctionException(){
        super("Função Inválida");
    }
    public InvalidFunctionException(String message){
        super(message);
    }
}

