package hestia.backend.exceptions.ServerExceptions;

public abstract class ServerException extends Exception {
    String error;

    public ServerException(String error){
        this.error=error;
    }

    public String getError(){
        return error;
    }

}
