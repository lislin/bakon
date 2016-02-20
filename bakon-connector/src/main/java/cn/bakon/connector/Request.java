package cn.bakon.connector;

import cn.bakon.BakonException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract class Request {
	private AddressNO addressNO;

	public AddressNO getAddressNO() {
		return this.addressNO;
	}

	protected Request(AddressNO addressNO) {
		this.addressNO = addressNO;
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

	public static Request parse(ByteBuf buf) {
		if (buf.readByte() != (byte) 0xCC)
			throw new BakonException("illegal request: " + buf);

		AddressNO addressNO = parseAddressNO(buf.readByte());

		byte type = buf.readByte();

		switch (type) {
		case 0x01:
			return new AlarmSetting(addressNO, buf);
		case 0x02:
			return new AlarmSwitch(addressNO, buf);
		case 0x03:
			return new Password(addressNO, buf);
		case 0x08:
			return new GroundLevelQuery(addressNO, buf);
		case 0x09:
			return new AlarmQuery(addressNO, buf);
		case 0x0a:
			return new Voltage1Query(addressNO, buf);
		case 0x0b:
			return new Voltage2Query(addressNO, buf);
		case 0x0c:
			return new Voltage3Query(addressNO, buf);
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
			return 0x01;
		if (this instanceof AlarmSwitch)
			return 0x02;
		if (this instanceof Password)
			return 0x03;

		throw new BakonException("unknown type of request: " + this);
	}

	public static class AlarmSetting extends Request {
		private int threshold;

		public int getThreshold() {
			return this.threshold;
		}

		public AlarmSetting(AddressNO addressNO, int threshold) {
			super(addressNO);
			this.threshold = threshold;
		}

		public AlarmSetting(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
			this.threshold = buf.readShort();
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeShort(this.threshold);
		}
	}

	public static class AlarmSwitch extends Request {
		private boolean enabled;

		public boolean getEnabled() {
			return this.enabled;
		}

		public AlarmSwitch(AddressNO addressNO, boolean enabled) {
			super(addressNO);
			this.enabled = enabled;
		}

		public AlarmSwitch(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
			buf.readByte();
			this.enabled = buf.readByte() == 0X01;
		}

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeByte(0X00).writeByte(this.enabled ? 0X01 : 0X00);
		}
	}

	public static class Password extends Request {
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

		public Password(AddressNO addressNO, int num1, int num2, int num3) {
			super(addressNO);
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public Password(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
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

	public abstract static class Query extends Request {
		private final static byte[] DATA = new byte[] { 0X00, 0X00 };

		public Query(AddressNO addressNO) {
			super(addressNO);
		}

		@Override
		protected abstract int parseType();

		@Override
		protected ByteBuf renderData(ByteBuf buf) {
			return buf.writeBytes(DATA);
		}
	}

	public static class GroundLevelQuery extends Query {
		public GroundLevelQuery(AddressNO addressNO) {
			super(addressNO);
		}

		public GroundLevelQuery(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
		}

		@Override
		protected int parseType() {
			return 0x08;
		}
	}

	public static class AlarmQuery extends Query {
		public AlarmQuery(AddressNO addressNO) {
			super(addressNO);
		}

		public AlarmQuery(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
		}

		@Override
		protected int parseType() {
			return 0x09;
		}
	}

	public static class Voltage1Query extends Query {
		public Voltage1Query(AddressNO addressNO) {
			super(addressNO);
		}

		public Voltage1Query(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
		}

		@Override
		protected int parseType() {
			return 0x0a;
		}
	}

	public static class Voltage2Query extends Query {
		public Voltage2Query(AddressNO addressNO) {
			super(addressNO);
		}

		public Voltage2Query(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
		}

		@Override
		protected int parseType() {
			return 0x0b;
		}
	}

	public static class Voltage3Query extends Query {
		public Voltage3Query(AddressNO addressNO) {
			super(addressNO);
		}

		public Voltage3Query(AddressNO addressNO, ByteBuf buf) {
			super(addressNO);
		}

		@Override
		protected int parseType() {
			return 0x0c;
		}
	}
}
