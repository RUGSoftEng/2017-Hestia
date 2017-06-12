package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class DatabaseException extends ServerException {

    String type;
    String message;

    public DatabaseException(String type,String message){
        this.type=type;
        this.message=message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
