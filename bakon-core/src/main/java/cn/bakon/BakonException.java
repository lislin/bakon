package cn.bakon;

public class BakonException extends RuntimeException {
	private static final long serialVersionUID = 5219261386187183172L;

	public BakonException(String message) {
		super(message);
	}

	public BakonException(Throwable e) {
		super(e);
	}

	public BakonException(String message, Throwable e) {
		super(message, e);
	}
}
