package cn.bakon.internal;

import cn.bakon.TradeParameterException;

public class Asserts {
	public static void parameterNotNull(String name, Object value) {
		if (value == null)
			throw new TradeParameterException(String.format("%s should not be null", name));
	}
}
