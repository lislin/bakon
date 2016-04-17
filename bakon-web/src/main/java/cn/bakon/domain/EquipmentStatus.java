package cn.bakon.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EquipmentStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private int equipmentId;
	@Column
	private Date time;
	@Column
	private String value;

	protected EquipmentStatus() {
	}

	public EquipmentStatus(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getEquipmentId() {
		return this.equipmentId;
	}

	public Date getTime() {
		return this.time;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.time = new Date();
		this.value = value;
	}
}
