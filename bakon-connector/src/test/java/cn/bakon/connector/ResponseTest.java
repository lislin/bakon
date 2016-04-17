package cn.bakon.connector;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.bakon.connector.Response.AlarmQuery;
import cn.bakon.connector.Response.AlarmSetting;
import cn.bakon.connector.Response.AlarmSwitch;
import cn.bakon.connector.Response.GroundLevelQuery;
import cn.bakon.connector.Response.Password;
import cn.bakon.connector.Response.Voltage1Query;
import cn.bakon.connector.Response.Voltage2Query;
import cn.bakon.connector.Response.Voltage3Query;
import io.netty.buffer.ByteBuf;

public class ResponseTest {
	@Test
	public void AlarmSetting_test() {
		AlarmSetting response = new AlarmSetting(FeatureType.A, true, 100);
		ByteBuf buf = response.getBytes();
		AlarmSetting response2 = (AlarmSetting) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getThreshold(), response2.getThreshold());
	}

	@Test
	public void AlarmSwitch_test() {
		AlarmSwitch response = new AlarmSwitch(FeatureType.A, true, true);
		ByteBuf buf = response.getBytes();
		AlarmSwitch response2 = (AlarmSwitch) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getEnabled(), response2.getEnabled());
	}

	@Test
	public void Password_test() {
		Password response = new Password(FeatureType.A, true, 1, 2, 3);
		ByteBuf buf = response.getBytes();
		Password response2 = (Password) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getNumber1(), response2.getNumber1());
		assertEquals(response.getNumber2(), response2.getNumber2());
		assertEquals(response.getNumber3(), response2.getNumber3());
	}

	@Test
	public void GroundLevelQuery_test() {
		GroundLevelQuery response = new GroundLevelQuery(FeatureType.A, true, true);
		ByteBuf buf = response.getBytes();
		GroundLevelQuery response2 = (GroundLevelQuery) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getHighOrLow(), response2.getHighOrLow());
	}

	@Test
	public void AlarmQuery_test() {
		AlarmQuery response = new AlarmQuery(FeatureType.A, true, 1);
		ByteBuf buf = response.getBytes();
		AlarmQuery response2 = (AlarmQuery) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getThreshold(), response2.getThreshold());
	}

	@Test
	public void Voltage1Query_test() {
		Voltage1Query response = new Voltage1Query(FeatureType.A, true, false, false, 1000);
		ByteBuf buf = response.getBytes();
		Voltage1Query response2 = (Voltage1Query) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getClosed(), response2.getClosed());
		assertEquals(response.getNegative(), response2.getNegative());
		assertEquals(response.getValue(), response2.getValue());
	}

	@Test
	public void Voltage2Query_test() {
		Voltage2Query response = new Voltage2Query(FeatureType.A, true, false, true, 1000);
		ByteBuf buf = response.getBytes();
		Voltage2Query response2 = (Voltage2Query) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getClosed(), response2.getClosed());
		assertEquals(response.getNegative(), response2.getNegative());
		assertEquals(response.getValue(), response2.getValue());
	}

	@Test
	public void Voltage3Query_test() {
		Voltage3Query response = new Voltage3Query(FeatureType.A, false, true, false, 0);
		ByteBuf buf = response.getBytes();
		Voltage3Query response2 = (Voltage3Query) Response.parse(buf);
		assertEquals(response.getFeatureType(), response2.getFeatureType());
		assertEquals(response.getError(), response2.getError());
		assertEquals(response.getClosed(), response2.getClosed());
		assertEquals(response.getNegative(), response2.getNegative());
		assertEquals(response.getValue(), response2.getValue());
	}

	@Test
	public void bit_test() {
		System.out.println(((byte) 2) & 0x0F);
		byte b = (byte) ((8 << 4) | 9);
		System.out.println(b);
		System.out.println(b >> 4);
		System.out.println(b & 0x0F);
		System.out.println(0xf000);
		System.out.println(0x83E8 >> 15);
		System.out.println((0x83E8 & ~(1 << 15)));
		System.out.println(0x03E8 | (1 << 15));

		System.out.println(100 | (1 << 15));

		System.out.println(33768 >> 15);
		System.out.println((33768 & ~(1 << 15)));
		System.out.println((1000 | (1 << 15)));
		System.out.println((short) (-31768 & ~(1 << 15)));
		System.out.println((short) (1000 | (1 << 15)));
		System.out.println((int) -31768);
	}
}
