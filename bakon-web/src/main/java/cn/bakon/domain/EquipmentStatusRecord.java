package cn.bakon.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// 每一次查询的设备状态记录
@Entity
public class EquipmentStatusRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	// 1W03
	@Column
	private String position;
	// E/W/T
	@Column
	private String type;
	// Device IP
	@Column
	private String host;
	// 1/2/3
	@Column
	private int port;
	@Column
	private Date time;
	// +80
	@Column
	private String value;
	// +-80
	@Column
	private String threshold;

	protected EquipmentStatusRecord() {
	}

	public EquipmentStatusRecord(
			String position, String type, String host, int port,
			Date time, String value, String threshold) {
		this.position = position;
		this.type = type;
		this.host = host;
		this.port = port;

		this.time = time;
		this.value = value;
		this.threshold = threshold;
	}

	public long getId() {
		return this.id;
	}

	public String getPosition() {
		return this.position;
	}

	public String getType() {
		return this.type;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public Date getTime() {
		return this.time;
	}

	public String getValue() {
		return this.value;
	}

	public String getThreshold() {
		return this.threshold;
	}
}
