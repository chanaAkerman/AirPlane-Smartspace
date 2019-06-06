package smartspace.logic;
public class ActionNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5012194878325614051L;

	public ActionNotFoundException() {
	}

	public ActionNotFoundException(String message) {
		super(message);
	}

	public ActionNotFoundException(Throwable cause) {
		super(cause);
	}

	public ActionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}