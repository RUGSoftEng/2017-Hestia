package hestia.backend.exceptions.ServerExceptions;


public class InvalidStateException extends ServerException {

    String expectedType;
    String valueType;

    public InvalidStateException(String expectedType,String valueType){
        this.expectedType=expectedType;
        this.valueType=valueType;
    }

    public String getExpectedType() {
        return expectedType;
    }

    public String getValueType() {
        return valueType;
    }
}
