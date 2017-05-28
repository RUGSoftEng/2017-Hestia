package hestia.backend.exceptions;

public class ServerFaultException extends Exception {
    String message;

    public ServerFaultException(String message){
        this.message=message;
    }

    public String getMessage(){
        return this.message;
    }

}
