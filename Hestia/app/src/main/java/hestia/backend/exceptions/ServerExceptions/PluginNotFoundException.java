package hestia.backend.exceptions.ServerExceptions;

public class PluginNotFoundException extends DefinedServerException {

    public PluginNotFoundException(String error, String message){
        super(error,message);
    }
}
