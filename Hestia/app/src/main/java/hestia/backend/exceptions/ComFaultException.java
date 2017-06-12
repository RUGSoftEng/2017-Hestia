package hestia.backend.exceptions;

public class ComFaultException extends Exception {
    private String error;
    private String message;
    
    public ComFaultException(String error){
        this.error=error;
        this.message= "An error occured";

    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}