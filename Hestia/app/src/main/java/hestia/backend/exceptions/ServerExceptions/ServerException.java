package hestia.backend.exceptions.ServerExceptions;

public abstract class ServerException extends Exception {
    String message;

    public ServerException(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
