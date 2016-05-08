package cn.bakon.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Setting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	// 监控图序列化结果
	@Column(length = 102400)
	private String graph;
	// 扫描频率
	@Column
	private int frequency;
	@ElementCollection
	private List<String> recordStatus;

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public List<String> getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(List<String> recordStatus) {
		this.recordStatus = recordStatus;
	}
}
