package cn.bakon.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bakon.WebApplication;
import cn.bakon.domain.EquipmentStatus;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentStatusRepositoryTest {
	@Autowired
	EquipmentStatusRepository repository;

	@Before
	public void before() {
		this.repository.deleteAll();
	}

	@Test
	public void findAll() {
		this.repository.save(new EquipmentStatus("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80", "正常"));
		this.repository.save(new EquipmentStatus("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80", "异常"));
		this.repository.save(new EquipmentStatus("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80", "离线"));
		List<EquipmentStatus> records = (List<EquipmentStatus>) this.repository.findAll();
		assertThat(records.size(), is(equalTo(3)));
	}

	@Test
	public void updateStatus() {
		EquipmentStatus status = new EquipmentStatus("1W01", "W", "127.0.0.1", 1, new Date(), "+80", "+-80", "正常");
		this.repository.save(status);

		EquipmentStatus status2 = this.repository.findOne(status.getId());
		status2.setCurrent(true);
		status2.addDuration(100);
		this.repository.save(status2);

		assertThat(this.repository.findByCurrent(true).size(), is(equalTo(1)));
		assertThat(this.repository.findByCurrent(false).size(), is(equalTo(0)));
	}
}