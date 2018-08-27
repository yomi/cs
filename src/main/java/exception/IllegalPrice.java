package exception;

public class IllegalPrice extends RuntimeException {

    public IllegalPrice(String message) {
        super(message);
    }
}
