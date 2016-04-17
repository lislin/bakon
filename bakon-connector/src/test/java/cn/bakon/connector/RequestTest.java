package cn.bakon.connector;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.bakon.connector.Request.AlarmQuery;
import cn.bakon.connector.Request.AlarmSetting;
import cn.bakon.connector.Request.AlarmSwitch;
import cn.bakon.connector.Request.GroundLevelQuery;
import cn.bakon.connector.Request.Password;
import cn.bakon.connector.Request.Voltage1Query;
import cn.bakon.connector.Request.Voltage2Query;
import cn.bakon.connector.Request.Voltage3Query;
import io.netty.buffer.ByteBuf;

public class RequestTest {
	@Test
	public void AlarmSetting_test() {
		AlarmSetting request = new AlarmSetting(FeatureType.A, 100);
		ByteBuf buf = request.getBytes();
		AlarmSetting request2 = (AlarmSetting) Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
		assertEquals(request.getThreshold(), request2.getThreshold());
	}

	@Test
	public void AlarmSwitch_test() {
		AlarmSwitch request = new AlarmSwitch(FeatureType.A, true);
		ByteBuf buf = request.getBytes();
		AlarmSwitch request2 = (AlarmSwitch) Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
		assertEquals(request.getEnabled(), request2.getEnabled());
	}

	@Test
	public void Password_test() {
		Password request = new Password(FeatureType.A, 1, 2, 3);
		ByteBuf buf = request.getBytes();
		Password request2 = (Password) Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
		assertEquals(request.getNumber1(), request2.getNumber1());
		assertEquals(request.getNumber2(), request2.getNumber2());
		assertEquals(request.getNumber3(), request2.getNumber3());
	}

	@Test
	public void GroundLevelQuery_test() {
		GroundLevelQuery request = new GroundLevelQuery(FeatureType.A);
		ByteBuf buf = request.getBytes();
		Request request2 = Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
	}

	@Test
	public void AlarmQuery_test() {
		AlarmQuery request = new AlarmQuery(FeatureType.A);
		ByteBuf buf = request.getBytes();
		Request request2 = Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
	}

	@Test
	public void Voltage1Query_test() {
		Voltage1Query request = new Voltage1Query(FeatureType.A);
		ByteBuf buf = request.getBytes();
		Request request2 = Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
	}

	@Test
	public void Voltage2Query_test() {
		Voltage2Query request = new Voltage2Query(FeatureType.A);
		ByteBuf buf = request.getBytes();
		Request request2 = Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
	}

	@Test
	public void Voltage3Query_test() {
		Voltage3Query request = new Voltage3Query(FeatureType.A);
		ByteBuf buf = request.getBytes();
		Request request2 = Request.parse(buf);
		assertEquals(request.getFeatureType(), request2.getFeatureType());
	}
}
