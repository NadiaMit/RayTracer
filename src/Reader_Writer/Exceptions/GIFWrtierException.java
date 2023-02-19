package Reader_Writer.Exceptions;

public class GIFWrtierException extends RuntimeException{

    private final String errorName = "GIFWrtierException";

    public GIFWrtierException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorName() {
        return errorName;
    }
}