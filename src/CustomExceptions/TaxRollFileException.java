package CustomExceptions;

public class TaxRollFileException extends Exception{
    public TaxRollFileException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
