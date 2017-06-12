package hestia.backend.exceptions;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import hestia.backend.exceptions.ServerExceptions.DatabaseException;
import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.NotFoundException;
import hestia.backend.exceptions.ServerExceptions.ServerException;
import hestia.backend.exceptions.ServerExceptions.SetupFailedException;
import hestia.backend.exceptions.ServerExceptions.UndefinedServerException;

public class ExceptionFactory {
    JsonObject exception;

    public ExceptionFactory(JsonObject exception){
        this.exception=exception;
    }


    public ServerException getException(){
        String exceptionName=exception.get("exception").getAsString();
        JsonObject details=exception.get("details").getAsJsonObject();
        switch(exceptionName){
            case "DatabaseException":
                return new DatabaseException(details.get("type").getAsString(),details.get("message").getAsString());
            case "InvalidStateException":
                return new InvalidStateException(details.get("expected_type").getAsString(),details.get("value_type").getAsString());
            case "NotFoundException":
                return new NotFoundException(details.get("type").getAsString());
            case "SetupFailedException":
                return new SetupFailedException(details.get("field").getAsString(), details.get("hint").getAsString());
            default:
                return new UndefinedServerException();
        }

    }

}
