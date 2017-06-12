package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class SetupFailedException extends ServerException {
    String field;
    String hint;

    public SetupFailedException(String field,String hint){
        this.field=field;
        this.hint=hint;
    }

    public String getField(){
        return field;
    }

    public String getHint(){
        return hint;
    }
}
