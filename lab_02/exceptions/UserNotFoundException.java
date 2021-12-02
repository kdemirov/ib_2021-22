package lab_02.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String id) {
        super(String.format("User with id %s was not found", id));
    }
}
