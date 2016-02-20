package cn.bakon.connector;

import cn.bakon.BakonException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract class Response {
	private boolean error;

	public boolean getError() {
		return this.error;
	}

	protected Response(boolean error) {
		this.error = error;
	}

	protected abstract ByteBuf renderData(ByteBuf buf);

	public ByteBuf getBytes() {
		return this.renderCRC(
				this.renderData(
						ByteBufAllocator.DEFAULT.buffer()
								.writeByte(0xCC)
								.writeByte(this.parseType())))
				.writeByte(0x33);
	}

	public static Response parse(ByteBuf buf) {
		if (buf.readByte() != (byte) 0xCC)
			throw new BakonException("illegal request: " + buf);

		byte type = buf.readByte();

		switch (type) {
		case 0x01:
			return new AlarmSetting(true, buf);
		case 0x11:
			return new AlarmSetting(false, buf);
		case 0x02:
			return new AlarmSwitch(true, buf);
		case 0x12:
			return new AlarmSwitch(false, buf);
		case 0x03:
			return new Password(true, buf);
		case 0x13:
			return new Password(false, buf);
		case 0x08:
			return new GroundLevelQuery(true, buf);
		case 0x18:
			return new GroundLevelQuery(false, buf);
		case 0x09:
			return new AlarmQuery(true, buf);
		case 0x19:
			return new AlarmQuery(false, buf);
		case 0x0a:
			return new Voltage1Query(true, buf);
		case 0x1a:
			return new Voltage1Query(false, buf);
		case 0x0b:
			return new Voltage2Query(true, buf);
		case 0x1b:
			return new Voltage2Query(false, buf);
		case 0x0c:
			return new Voltage3Query(true, buf);
		case 0x1c:
			return new Voltage3Query(false, buf);
		default:
			throw new BakonException("unknown type of request: " + type);
		}

		// TODO CRC check
	}

	private ByteBuf renderCRC(ByteBuf buf) {
		byte[] tmp = new byte[4];
		buf.getBytes(0, tmp);
		CRC8 crc8 = new CRC8();
		crc8.update(tmp);
		return buf.writeByte((int) crc8.getValue());
	}

	protected int parseType() {
		if (this instanceof AlarmSetting)
			return this.getError() ? 0x01 : 0x11;
		if (this instanceof AlarmSwitch)
			return this.getError() ? 0x02 : 0x12;
		if (this instanceof Password)
			return this.getError() ? 0x03 : 0x13;

		throw new BakonException("unknown type of request: " + this);
	}

	public static class AlarmSetting extends Response {
		private int threshold;

		public int getThreshold() {
			return this.threshold;
		}

		public AlarmSetting(boolean error, int threshold) {
			super(error);
			this.threshold = threshold;
		}

		public AlarmSetting(boolean error, ByteBuf buf) {
			super(error);
			this.threshold = buf.readShort();
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeShort(this.threshold);
		}
	}

	public static class AlarmSwitch extends Response {
		private boolean enabled;

		public boolean getEnabled() {
			return this.enabled;
		}

		public AlarmSwitch(boolean error, boolean enabled) {
			super(error);
			this.enabled = enabled;
		}

		public AlarmSwitch(boolean error, ByteBuf buf) {
			super(error);
			buf.readByte();
			this.enabled = buf.readByte() == 0X01;
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeByte(0X00).writeByte(this.enabled ? 0X01 : 0X00);
		}
	}

	public static class Password extends Response {
		private int num1;
		private int num2;
		private int num3;

		public int getNumber1() {
			return this.num1;
		}

		public int getNumber2() {
			return this.num2;
		}

		public int getNumber3() {
			return this.num3;
		}

		public Password(boolean error, int num1, int num2, int num3) {
			super(error);
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public Password(boolean error, ByteBuf buf) {
			super(error);
			byte h = buf.readByte();
			// int high = h >> 4;//TODO assert 0
			this.num1 = h & 0x0F;
			byte l = buf.readByte();
			this.num2 = l >> 4;
			this.num3 = l & 0x0F;

		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeByte(this.num1).writeByte((this.num2 << 4) | this.num3);
		}
	}

	public abstract static class Query extends Response {
		public Query(boolean error) {
			super(error);
		}

		@Override
		protected abstract int parseType();
	}

	public static class GroundLevelQuery extends Query {
		private boolean highOrLow;

		public boolean getHighOrLow() {
			return this.highOrLow;
		}

		public GroundLevelQuery(boolean error, boolean highOrLow) {
			super(error);
			this.highOrLow = highOrLow;
		}

		public GroundLevelQuery(boolean error, ByteBuf buf) {
			super(error);
			buf.readByte();
			this.highOrLow = buf.readByte() == 0X01;
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x08 : 0x18;
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeByte(0X00).writeByte(this.highOrLow ? 0X01 : 0X00);
		}
	}

	public static class AlarmQuery extends Query {
		private int threshold;

		public int getThreshold() {
			return this.threshold;
		}

		public AlarmQuery(boolean error, int threshold) {
			super(error);
			this.threshold = threshold;
		}

		public AlarmQuery(boolean error, ByteBuf buf) {
			super(error);
			this.threshold = buf.readShort();
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x09 : 0x19;
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeShort(this.threshold);
		}
	}

	public abstract static class VoltageQuery extends Query {
		private int value;

		public int getValue() {
			return this.value;
		}

		public VoltageQuery(boolean error, int value) {
			super(error);
			this.value = value;
		}

		public VoltageQuery(boolean error, ByteBuf buf) {
			super(error);
			// FIXME read Voltage
			this.value = buf.readShort();

			// byte b = buf.readByte();
			// var low = b & 0x0F;
			// var high = b >> 4;
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			// FIXME write Voltage
			return buf.writeShort(this.value);
		}
	}

	public static class Voltage1Query extends VoltageQuery {
		public Voltage1Query(boolean error, int value) {
			super(error, value);
		}

		public Voltage1Query(boolean error, ByteBuf buf) {
			super(error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0a : 0x1a;
		}
	}

	public static class Voltage2Query extends VoltageQuery {
		public Voltage2Query(boolean error, int value) {
			super(error, value);
		}

		public Voltage2Query(boolean error, ByteBuf buf) {
			super(error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0b : 0x1b;
		}
	}

	public static class Voltage3Query extends VoltageQuery {
		public Voltage3Query(boolean error, int value) {
			super(error, value);
		}

		public Voltage3Query(boolean error, ByteBuf buf) {
			super(error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0c : 0x1c;
		}
	}
}
