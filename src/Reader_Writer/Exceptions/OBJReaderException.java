package Reader_Writer.Exceptions;

public class OBJReaderException extends RuntimeException{

    private final String errorName = "OBJReaderException";

    public OBJReaderException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorName() {
        return errorName;
    }
}