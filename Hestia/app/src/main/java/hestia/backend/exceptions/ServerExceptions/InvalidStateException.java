package hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

public class InvalidStateException extends ServerException {

    String expectedType;
    String valueType;

    public InvalidStateException(String error, JsonObject details){
        super(error);
        expectedType= details.get("expected_type").getAsString();
        valueType= details.get("value_type").getAsString();
    }

    public String getExpectedType() {
        return expectedType;
    }

    public String getValueType() {
        return valueType;
    }
}
