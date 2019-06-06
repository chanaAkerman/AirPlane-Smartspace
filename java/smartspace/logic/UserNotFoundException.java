package smartspace.logic;
public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 4350348440627784349L;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
