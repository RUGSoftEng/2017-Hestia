package hestia.backend.exceptions.ServerExceptions;

/**
 * Created by chris on 28-5-2017.
 */

public class PluginNotFoundException extends DefinedServerException {

    public PluginNotFoundException(String error, String message){
        super(error,message);
    }
}
