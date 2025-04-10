package Exceptions;

public class MatchAlreadyFinishedException extends RuntimeException {

    public MatchAlreadyFinishedException(String message) {
        super(message);
    }
}
