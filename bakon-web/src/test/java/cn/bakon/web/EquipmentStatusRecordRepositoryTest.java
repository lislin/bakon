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

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentStatusRecordRepositoryTest {
	@Autowired
	EquipmentStatusRecordRepository repository;

	@Test
	public void findAll() {
		this.repository.save(new EquipmentStatusRecord("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80"));
		this.repository.save(new EquipmentStatusRecord("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80"));
		Page<EquipmentStatusRecord> records = this.repository.findAll(new PageRequest(0, 10));
		assertThat(records.getTotalElements(), is(equalTo(2L)));
	}
}