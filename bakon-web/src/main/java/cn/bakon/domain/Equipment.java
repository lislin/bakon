package cn.bakon.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// 设备定义
@Entity
public class Equipment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
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

	protected Equipment() {
	}

	public Equipment(String position, String type, String host, int port) {
		this.position = position;
		this.type = type;
		this.host = host;
		this.port = port;
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
}
