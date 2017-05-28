package hestia.backend.exceptions.ServerExceptions;

public class CollectionNotFoundException extends DefinedServerException {

    public CollectionNotFoundException(String error,String message){
        super(error,message);
    }
}