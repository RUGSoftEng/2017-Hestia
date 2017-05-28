package hestia.backend.exceptions.ServerExceptions;

public class DefinedServerException extends Exception {
    private String error;

    public DefinedServerException(String error, String message){
        super(message);
        this.error=error;
    }

    public String getError(){
        return error;
    }


}