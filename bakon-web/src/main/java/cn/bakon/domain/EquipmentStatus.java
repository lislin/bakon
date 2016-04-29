package cn.bakon.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

// 设备当前状态
@Entity
public class EquipmentStatus extends EquipmentStatusRecord {
	private static final long serialVersionUID = 1L;

	// 是否是当前状态
	@Column
	private boolean current;
	// 当前状态的持续时间、毫秒
	@Column
	private int duration;
	// 正常 异常 离线
	@Column
	private String status;

	protected EquipmentStatus() {
	}

	public EquipmentStatus(
			String position, String type, String host, int port,
			Date time, String value, String threshold,
			String status) {
		super(position, type, host, port, time, value, threshold);
		this.status = status;
	}

	public boolean getCurrent() {
		return this.current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public int getDuration() {
		return this.duration;
	}

	public void addDuration(int millis) {
		this.duration += millis;
	}

	public String getStatus() {
		return this.status;
	}
}
