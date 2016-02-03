package cn.bakon.internal.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bakon.BakonException;
import cn.bakon.internal.PropertyService;

public class PropertyServiceImpl implements PropertyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

	private Map<String, Object> properties;

	public String getString(String key) {
		return (String) this.properties.get(key);
	}

	public int getInt(String key) {
		return (Integer) this.properties.get(key);
	}

	public Integer getInteger(String key) {
		return (Integer) this.properties.get(key);
	}

	public String[] getStrings(String key) {
		return (String[]) this.properties.get(key);
	}

	protected void put(String key, Object value) {
		this.properties.put(key, value);
	}

	protected void resolveProperties(String localFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(new File(localFile)));
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] item = line.trim().split("=");
				this.put(item[0].trim(),
						item.length > 1 ? this.parseValue(item[1].trim()) : null);
			}
		} finally {
			reader.close();
		}
	}

	protected Object parseValue(String value) {
		if (value.contains(","))
			return value.split(",");
		return value;
	}

	public PropertyServiceImpl() {
		this("local");
	}

	public PropertyServiceImpl(String env) {
		this(env, null);
	}

	public PropertyServiceImpl(String env, String localFile) {
		this.properties = new HashMap<String, Object>();
		this.internalPrepare(env, localFile);
	}

	protected void internalPrepare(String env, String localFile) {
		if ("local".equals(env) || "daily".equals(env)) {

		} else if ("pro".equals(env)) {

		}

		// override properties with local config
		if ("pro".equals(env) && localFile != null) {
			try {
				this.resolveProperties(localFile);
			} catch (Exception e) {
				throw new BakonException("resolve properties error", e);
			}
		}

		if ("local".equals(env)) {

		}

		LOGGER.info("{}&&{}: {}", env, localFile, this.properties);
	}
}
