package cn.bakon;

public class TradeParameterException extends TradeException {
	private static final long serialVersionUID = 5365163388371376704L;

	public TradeParameterException(String message) {
		super(message);
	}

	public TradeParameterException(String message, Throwable e) {
		super(message, e);
	}

}
