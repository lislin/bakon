package cn.bakon.connector;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.junit.AfterClass;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class UDPTest {
	static boolean running;
	static DatagramSocket serverSocket;
	static DatagramSocket clientSocket;

	static {
		try {
			serverSocket = new DatagramSocket(9876);
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void afterClass() throws IOException {
		serverSocket.close();
		clientSocket.close();
	}

	public static byte[] doResponse(byte[] sendData) throws Exception {
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		System.out.println("SERVER RECEIVED: " + new String(receivePacket.getData()));
		serverSocket.send(new DatagramPacket(
				sendData, sendData.length,
				receivePacket.getAddress(),
				receivePacket.getPort()));
		return receiveData;
	}

	public static void doRequest(byte[] sendData) throws Exception {
		doRequest("localhost", 9876, sendData);
	}

	public static void doRequest(String host, int port, byte[] sendData) throws Exception {
		DatagramPacket sendPacket = new DatagramPacket(
				sendData, sendData.length, InetAddress.getByName(host), port);
		clientSocket.send(sendPacket);
	}

	public static byte[] doReceive() throws Exception {
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		System.out.println("CLIENT RECEIVED: " + new String(receivePacket.getData()));
		return receiveData;
	}

	@Test
	public void send_receive_test() throws Exception {
		doRequest("how are you".getBytes());
		doResponse("fine".getBytes());
		doReceive();
	}

	@Test
	public void request_respone_test() throws Exception {
		ByteBuf buf1 = new Request.AlarmSetting(AddressNO.A, 100).getBytes();
		byte[] requestData = new byte[buf1.readableBytes()];
		buf1.getBytes(0, requestData);

		ByteBuf buf2 = new Response.AlarmSetting(AddressNO.A, false, 100).getBytes();
		byte[] responseData = new byte[buf2.readableBytes()];
		buf2.getBytes(0, responseData);

		doRequest(requestData);

		ByteBuf buf3 = ByteBufAllocator.DEFAULT.buffer().writeBytes(doResponse(responseData));
		Request.AlarmSetting request = (Request.AlarmSetting) Request.parse(buf3);
		assertEquals(AddressNO.A, request.getAddressNO());
		assertEquals(100, request.getThreshold());

		ByteBuf buf4 = ByteBufAllocator.DEFAULT.buffer().writeBytes(doReceive());
		Response.AlarmSetting response = (Response.AlarmSetting) Response.parse(buf4);
		assertEquals(false, response.getError());
		assertEquals(100, response.getThreshold());
	}

	public static void main(String[] args) throws Exception {
		Response.AlarmSetting alarmSetting = (Response.AlarmSetting) sendAndReceive(new Request.AlarmSetting(AddressNO.A, 100));
		System.out.println(alarmSetting);
		System.out.println(alarmSetting.getAddressNO());
		System.out.println(alarmSetting.getError());
		System.out.println(alarmSetting.getThreshold());
		System.out.println();

		Response.AlarmQuery alarmQuery = (Response.AlarmQuery) sendAndReceive(new Request.AlarmQuery(AddressNO.A));
		System.out.println(alarmQuery);
		System.out.println(alarmQuery.getAddressNO());
		System.out.println(alarmQuery.getError());
		System.out.println(alarmQuery.getThreshold());
		System.out.println();

		Response.AlarmSwitch alarmSwitch = (Response.AlarmSwitch) sendAndReceive(new Request.AlarmSwitch(AddressNO.A, true));
		System.out.println(alarmSwitch);
		System.out.println(alarmSwitch.getAddressNO());
		System.out.println(alarmSwitch.getError());
		System.out.println(alarmSwitch.getEnabled());
		System.out.println();

		Response.Password password = (Response.Password) sendAndReceive(new Request.Password(AddressNO.A, 1, 2, 3));
		System.out.println(password);
		System.out.println(password.getAddressNO());
		System.out.println(password.getError());
		System.out.println(password.getNumber1());
		System.out.println(password.getNumber2());
		System.out.println(password.getNumber3());
		System.out.println();

		Response.GroundLevelQuery groundLevelQuery = (Response.GroundLevelQuery) sendAndReceive(new Request.GroundLevelQuery(AddressNO.A));
		System.out.println(groundLevelQuery);
		System.out.println(groundLevelQuery.getAddressNO());
		System.out.println(groundLevelQuery.getError());
		System.out.println(groundLevelQuery.getHighOrLow());
		System.out.println();

		Response.Voltage1Query voltage1Query = (Response.Voltage1Query) sendAndReceive(new Request.Voltage1Query(AddressNO.A));
		System.out.println(voltage1Query);
		System.out.println(voltage1Query.getAddressNO());
		System.out.println(voltage1Query.getError());
		System.out.println(voltage1Query.getClosed());
		System.out.println(voltage1Query.getNegative());
		System.out.println(voltage1Query.getValue());
		System.out.println();

		Response.Voltage2Query voltage2Query = (Response.Voltage2Query) sendAndReceive(new Request.Voltage2Query(AddressNO.A));
		System.out.println(voltage2Query);
		System.out.println(voltage2Query.getAddressNO());
		System.out.println(voltage2Query.getError());
		System.out.println(voltage2Query.getClosed());
		System.out.println(voltage2Query.getNegative());
		System.out.println(voltage2Query.getValue());
		System.out.println();

		Response.Voltage3Query voltage3Query = (Response.Voltage3Query) sendAndReceive(new Request.Voltage3Query(AddressNO.A));
		System.out.println(voltage3Query);
		System.out.println(voltage3Query.getAddressNO());
		System.out.println(voltage3Query.getError());
		System.out.println(voltage3Query.getClosed());
		System.out.println(voltage3Query.getNegative());
		System.out.println(voltage3Query.getValue());
		System.out.println();
	}

	private static Response sendAndReceive(Request request) throws Exception {
		ByteBuf buf1 = request.getBytes();
		byte[] requestData = new byte[buf1.readableBytes()];
		buf1.getBytes(0, requestData);
		render(requestData);

		doRequest("192.168.0.5", 10006, requestData);
		byte[] responseData = doReceive();
		render(responseData);

		ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer().writeBytes(requestData);
		return Response.parse(buf2);
	}

	private static void render(byte[] data) {
		for (byte b : data) {
			System.out.print("0x" + Integer.toHexString(Byte.toUnsignedInt(b)));
			System.out.print(",");
		}
		System.out.println();
	}
}
