package exception;

public class IllegalUserId extends RuntimeException {

    public IllegalUserId(String message) {
        super(message);
    }
}
