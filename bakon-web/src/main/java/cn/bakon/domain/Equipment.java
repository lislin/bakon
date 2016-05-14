package cn.bakon.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
	// +-80 [+80, -80]
	@ElementCollection()
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> threshold;

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

	public List<String> getThreshold() {
		return this.threshold;
	}

	public String getThresholdPhase() {
		if ("D".equalsIgnoreCase(type)) {
			if (this.threshold.size() == 0)
				return "未设置";
			if (this.threshold.size() == 1)
				return "+" + this.threshold.get(0) + "v";
			if (this.threshold.size() >= 2)
				return "+" + this.threshold.get(0) + "v;-" + this.threshold.get(1) + "v";
		}
		return Arrays.toString(this.threshold.toArray());
	}
}
