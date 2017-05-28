package hestia.backend.exceptions.ServerExceptions;

public class DeviceNotFoundException extends DefinedServerException {

    public DeviceNotFoundException(String error, String message) {
        super(error, message);
    }
}
