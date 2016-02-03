package cn.bakon.internal.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bakon.TradeException;
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
			this.put("trade.processor.threads", 200);
			this.put("trade.processor.redis.host", "ddb092d30928403a.m.cnhza.kvstore.aliyuncs.com");
			this.put("trade.processor.redis.port", 6379);
			this.put("trade.processor.redis.auth", "A2VlCJ8pYj");
			this.put("trade.processor.redis.pool.max", 200);
			this.put("trade.processor.redis.expire.key", "EXPIRES");
			this.put("trade.processor.redis.channels",
					new String[] { "TRADE01", "TRADE02", "TRADE03", "TRADE04" });

			this.put("trade.fapis.url", "http://testfapis.365eche.com:8181");
			this.put("trade.fapis.package", "trade_system.order");
			this.put("trade.fapis.public.key", "c8b697c75a0050c4d8513bf1b73be184");
			this.put("trade.fapis.package.key", "d654c277ed1f325243dcec6e655364a6");
		} else if ("pro".equals(env)) {
			this.put("trade.processor.threads", 200);
			this.put("trade.processor.redis.host", "a01360c702b84335.m.cnhza.kvstore.aliyuncs.com");
			this.put("trade.processor.redis.port", 6379);
			this.put("trade.processor.redis.auth", "B2VlCJ8pYj");
			this.put("trade.processor.redis.pool.max", 200);
			// HACK read from file
			this.put("trade.processor.redis.expire.key", "EXPIRES");
			this.put("trade.processor.redis.channels", new String[] {
					"TRADE01", "TRADE02", "TRADE03", "TRADE04",
					"TRADE05", "TRADE06", "TRADE07", "TRADE08"
			});

			this.put("trade.fapis.url", "http://fapis.chemao.com.cn");
			this.put("trade.fapis.package", "trade_system.order");
			this.put("trade.fapis.public.key", "f48a897be771827d05ba0b722165a09d");
			this.put("trade.fapis.package.key", "f5f07baf6ce02d8aba77276710257e2a");
		}

		// override properties with local config
		if ("pro".equals(env) && localFile != null) {
			try {
				this.resolveProperties(localFile);
			} catch (Exception e) {
				throw new TradeException("resolve properties error", e);
			}
		}

		if ("local".equals(env)) {
			this.put("trade.processor.redis.host", "ops.365eche.com");
			this.put("trade.processor.redis.channels", new String[] { "TRADE-TEST" });
		}

		LOGGER.info("{}&&{}: {}", env, localFile, this.properties);
	}
}
