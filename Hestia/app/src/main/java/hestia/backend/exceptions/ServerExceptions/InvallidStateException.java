package hestia.backend.exceptions.ServerExceptions;

public class InvallidStateException extends DefinedServerException {

    public InvallidStateException(String error ,String message){
        super(error,message);
    }
}
