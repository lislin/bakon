package cn.bakon;

public class TradeException extends RuntimeException {
	private static final long serialVersionUID = 5219261386187183172L;

	public TradeException(String message) {
		super(message);
	}

	public TradeException(Throwable e) {
		super(e);
	}

	public TradeException(String message, Throwable e) {
		super(message, e);
	}
}
