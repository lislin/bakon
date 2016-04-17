package cn.bakon.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EquipmentStatusRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	@Column
	private Date time;
	@Column
	private int equipmentId;
	@Column
	private String value;

	protected EquipmentStatusRecord() {
	}

	public EquipmentStatusRecord(int equipmentId, String value) {
		this.time = new Date();
		this.equipmentId = equipmentId;
		this.value = value;
	}

	public long getId() {
		return this.id;
	}

	public Date getTime() {
		return this.time;
	}

	public int getEquipmentId() {
		return this.equipmentId;
	}

	public String getValue() {
		return this.value;
	}
}
