package cn.bakon.connector;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class UDPTest {
	static boolean running;
	static DatagramSocket serverSocket;
	static DatagramSocket clientSocket;

	@BeforeClass
	public static void beforeClass() throws IOException {
		serverSocket = new DatagramSocket(9876);
		clientSocket = new DatagramSocket();
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
		DatagramPacket sendPacket = new DatagramPacket(
				sendData, sendData.length, InetAddress.getByName("localhost"), 9876);
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

		ByteBuf buf2 = new Response.AlarmSetting(false, 100).getBytes();
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
}
