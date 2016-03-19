package cn.bakon.connector;

import java.nio.ByteOrder;

import cn.bakon.BakonException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract class Response {
	private AddressNO addressNO;
	private boolean error;

	public AddressNO getAddressNO() {
		return this.addressNO;
	}

	public boolean getError() {
		return this.error;
	}

	protected Response(AddressNO addressNO, boolean error) {
		this.addressNO = addressNO;
		this.error = error;
	}

	protected abstract ByteBuf renderData(ByteBuf buf);

	public ByteBuf getBytes() {
		return this.renderCRC(
				this.renderData(
						ByteBufAllocator.DEFAULT.buffer()
								.writeByte(0xCC)
								.writeByte(this.parseAddressNO())
								.writeByte(this.parseType())))
				.writeByte(0x33);
	}

	public static Response parse(ByteBuf buf) {
		if (buf.readByte() != (byte) 0xCC)
			throw new BakonException("illegal request: " + buf);

		AddressNO addressNO = parseAddressNO(buf.readByte());

		byte type = buf.readByte();

		switch (type) {
		case 0x01:
			return new AlarmSetting(addressNO, true, buf);
		case 0x11:
			return new AlarmSetting(addressNO, false, buf);
		case 0x02:
			return new AlarmSwitch(addressNO, true, buf);
		case 0x12:
			return new AlarmSwitch(addressNO, false, buf);
		case 0x03:
			return new Password(addressNO, true, buf);
		case 0x13:
			return new Password(addressNO, false, buf);
		case 0x08:
			return new GroundLevelQuery(addressNO, true, buf);
		case 0x18:
			return new GroundLevelQuery(addressNO, false, buf);
		case 0x09:
			return new AlarmQuery(addressNO, true, buf);
		case 0x19:
			return new AlarmQuery(addressNO, false, buf);
		case 0x0a:
			return new Voltage1Query(addressNO, true, buf);
		case 0x1a:
			return new Voltage1Query(addressNO, false, buf);
		case 0x0b:
			return new Voltage2Query(addressNO, true, buf);
		case 0x1b:
			return new Voltage2Query(addressNO, false, buf);
		case 0x0c:
			return new Voltage3Query(addressNO, true, buf);
		case 0x1c:
			return new Voltage3Query(addressNO, false, buf);
		default:
			throw new BakonException("unknown type of request: " + type);
		}

		// TODO CRC check
	}

	private ByteBuf renderCRC(ByteBuf buf) {
		byte[] tmp = new byte[5];
		buf.getBytes(0, tmp);
		CRC8 crc8 = new CRC8();
		crc8.update(tmp);
		return buf.writeByte((int) crc8.getValue());
	}

	private byte parseAddressNO() {
		switch (this.addressNO) {
		case A:
			return 0xa;
		case B:
			return 0xb;
		case C:
			return 0xc;
		default:
			throw new BakonException("unknown address: " + this.addressNO);
		}
	}

	private static AddressNO parseAddressNO(byte value) {
		switch (value) {
		case 0xa:
			return AddressNO.A;
		case 0xb:
			return AddressNO.B;
		case 0xc:
			return AddressNO.C;
		default:
			throw new BakonException("unknown address: " + value);
		}
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

		public AlarmSetting(AddressNO addressNO, boolean error, int threshold) {
			super(addressNO, error);
			// TODO assert less than 500
			this.threshold = threshold;
		}

		public AlarmSetting(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
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

		public AlarmSwitch(AddressNO addressNO, boolean error, boolean enabled) {
			super(addressNO, error);
			this.enabled = enabled;
		}

		public AlarmSwitch(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
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

		public Password(AddressNO addressNO, boolean error, int num1, int num2, int num3) {
			super(addressNO, error);
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public Password(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
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
		public Query(AddressNO addressNO, boolean error) {
			super(addressNO, error);
		}

		@Override
		protected abstract int parseType();
	}

	public static class GroundLevelQuery extends Query {
		private boolean highOrLow;

		public boolean getHighOrLow() {
			return this.highOrLow;
		}

		public GroundLevelQuery(AddressNO addressNO, boolean error, boolean highOrLow) {
			super(addressNO, error);
			this.highOrLow = highOrLow;
		}

		public GroundLevelQuery(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
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

		public AlarmQuery(AddressNO addressNO, boolean error, int threshold) {
			super(addressNO, error);
			this.threshold = threshold;
		}

		public AlarmQuery(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
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
		private boolean negative;
		private int value;
		private boolean closed;

		public boolean getNegative() {
			return this.negative;
		}

		public int getValue() {
			return this.value;
		}

		public boolean getClosed() {
			return this.closed;
		}

		public VoltageQuery(AddressNO addressNO, boolean error, boolean closed, boolean negative, int value) {
			super(addressNO, error);
			this.closed = closed;
			this.negative = negative;
			this.value = value;
		}

		public VoltageQuery(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			int v = buf.readChar();
			buf.order(ByteOrder.BIG_ENDIAN);

			this.closed = v == 0xf000;
			if (this.closed)
				return;

			this.negative = v >> 15 == 1;
			this.value = v & ~(1 << 15);
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			buf.order(ByteOrder.LITTLE_ENDIAN);
			if (this.closed)
				buf.writeChar(0xf000);
			else
				buf.writeChar(
						this.negative
								? this.value | (1 << 15)
								: this.value);
			return buf.order(ByteOrder.BIG_ENDIAN);
		}
	}

	public static class Voltage1Query extends VoltageQuery {
		public Voltage1Query(AddressNO addressNO, boolean error, boolean closed, boolean negative, int value) {
			super(addressNO, error, closed, negative, value);
		}

		public Voltage1Query(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0a : 0x1a;
		}
	}

	public static class Voltage2Query extends VoltageQuery {
		public Voltage2Query(AddressNO addressNO, boolean error, boolean closed, boolean negative, int value) {
			super(addressNO, error, closed, negative, value);
		}

		public Voltage2Query(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0b : 0x1b;
		}
	}

	public static class Voltage3Query extends VoltageQuery {
		public Voltage3Query(AddressNO addressNO, boolean error, boolean closed, boolean negative, int value) {
			super(addressNO, error, closed, negative, value);
		}

		public Voltage3Query(AddressNO addressNO, boolean error, ByteBuf buf) {
			super(addressNO, error, buf);
		}

		@Override
		protected int parseType() {
			return this.getError() ? 0x0c : 0x1c;
		}
	}
}
