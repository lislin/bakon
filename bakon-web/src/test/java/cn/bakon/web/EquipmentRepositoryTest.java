package cn.bakon.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bakon.WebApplication;
import cn.bakon.domain.Equipment;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentRepositoryTest {

	@Autowired
	EquipmentRepository repository;

	@Test
	public void findsFirstPageOfCities() {
		Page<Equipment> cities = this.repository.findAll(new PageRequest(0, 10));
		assertThat(cities.getTotalElements(), is(greaterThanOrEqualTo(4L)));
	}

	@Test
	public void findByCode() {
		Equipment equipment = this.repository.findByCode("1024");
		assertThat(equipment, notNullValue());
		assertThat(equipment.getCode(), is(equalTo("1024")));
	}
}