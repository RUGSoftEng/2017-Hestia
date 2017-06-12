package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class NotFoundException extends ServerException {
    String type;

    public NotFoundException(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
