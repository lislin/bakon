package cn.bakon.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bakon.WebApplication;
import cn.bakon.domain.EquipmentStatusRecord;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentStatusRepositoryTest {
	@Autowired
	EquipmentStatusRecordRepository repository;

	@Test
	public void findByEquipmentId() {
		this.repository.save(new EquipmentStatusRecord(1, "ERROR"));
		this.repository.save(new EquipmentStatusRecord(1, "ERROR"));
		Page<EquipmentStatusRecord> records = this.repository.findByEquipmentId(1, new PageRequest(0, 10));
		assertThat(records.getTotalElements(), is(equalTo(2L)));
	}
}