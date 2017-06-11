package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class NotFoundException extends ServerException {
    String type;

    public NotFoundException(String error, JsonObject details){
        super(error);
        type = details.get("type").getAsString();
    }

    public String getType() {
        return type;
    }
}
