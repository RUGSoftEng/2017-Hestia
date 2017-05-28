package hestia.backend.exceptions.ServerExceptions;


public class ServerException extends Exception {
    String message;

    public ServerException(String message){
        this.message=message;
    }

}
