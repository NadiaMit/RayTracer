package Reader_Writer.Exceptions;

public class PNGConverterException extends RuntimeException{

    private final String errorName = "PNGWriterException";

    public PNGConverterException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorName() {
        return errorName;
    }
}