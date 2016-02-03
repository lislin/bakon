package cn.bakon.internal.impl;

import static org.junit.Assert.*;

import java.io.FileOutputStream;

import org.junit.Test;

import cn.bakon.internal.impl.PropertyServiceImpl;

public class PropertyServiceImplTest {
	@Test
	public void resolve_file_test() throws Exception {
		String localFile = System.currentTimeMillis() + ".log";

		FileOutputStream outputStream = new FileOutputStream(localFile);
		outputStream.write("a=1\nb = 2 \nc=1,2,3".getBytes());
		outputStream.close();

		PropertyServiceImpl properties = new PropertyServiceImpl("pro", localFile);
		assertEquals("1", properties.getString("a"));
		assertEquals("2", properties.getString("b"));
		assertArrayEquals(new String[] { "1", "2", "3" }, properties.getStrings("c"));
	}
}
