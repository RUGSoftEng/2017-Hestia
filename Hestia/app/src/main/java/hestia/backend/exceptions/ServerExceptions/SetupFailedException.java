package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class SetupFailedException extends ServerException {
    String field;
    String hint;

    public SetupFailedException(String error, JsonObject details){
        super(error);
        field=details.get("field").getAsString();
        hint=details.get("hint").getAsString();
    }

    public String getField(){
        return field;
    }

    public String getHint(){
        return hint;
    }
}
