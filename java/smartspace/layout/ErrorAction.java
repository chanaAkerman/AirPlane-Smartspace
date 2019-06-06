package smartspace.layout;

public class ErrorAction {
			private String message;

			public ErrorAction() {
			}

			public ErrorAction(String message) {
				super();
				this.message = message;
			}

			public String getMessage() {
				return message;
			}

			public void setMessage(String message) {
				this.message = message;
			}
}
