package Reader_Writer.Exceptions;

public class XMLReaderException extends RuntimeException{

    private final String errorName = "XMLReaderException";

    public XMLReaderException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorName() {
        return errorName;
    }
}