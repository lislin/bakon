package cn.bakon.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Equipment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private String description;

	@Column
	private String deviceHost;
	@Column
	private String featureType;
	@Column
	private String port;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s", this.id, this.code, this.name);
	}
}
