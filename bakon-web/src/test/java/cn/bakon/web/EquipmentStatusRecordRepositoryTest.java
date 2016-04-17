package cn.bakon.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bakon.WebApplication;
import cn.bakon.domain.EquipmentStatus;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentStatusRecordRepositoryTest {
	@Autowired
	EquipmentStatusRepository repository;

	@Test
	public void findAll() {
		this.repository.save(new EquipmentStatus(1));
		this.repository.save(new EquipmentStatus(2));
		List<EquipmentStatus> records = (List<EquipmentStatus>) this.repository.findAll();
		assertThat(records.size(), is(equalTo(2)));
	}

	@Test
	public void updateStatus() {
		this.repository.save(new EquipmentStatus(1));
		EquipmentStatus status = this.repository.findOne(1);
		status.setValue("OK");
		this.repository.save(status);
		EquipmentStatus status2 = this.repository.findOne(1);
		assertThat(status2.getTime(), notNullValue());
		assertThat(status2.getValue(), is(equalTo("OK")));
	}
}