package hestia.backend;

public class ComFaultException extends Exception {
    private String error;
    private String message;

    public ComFaultException(String error,String message){
        this.error=error;
        this.message=message;
    }

    public String getError(){
        return error;
    }

    public String getMessage(){
        return message;
    }

}