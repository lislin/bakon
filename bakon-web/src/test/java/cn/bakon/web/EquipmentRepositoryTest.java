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

import java.util.function.Consumer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class EquipmentRepositoryTest {
	@Autowired
	EquipmentRepository repository;

	@Test
	public void findsFirstPageOfEquipment() {
		Page<Equipment> equipments = this.repository.findAll(new PageRequest(0, 10));
		assertThat(equipments.getTotalElements(), is(greaterThanOrEqualTo(4L)));
		equipments.forEach(new Consumer<Equipment>() {
			@Override
			public void accept(Equipment t) {
				System.out.println(t);
			}
		});
	}

	@Test
	public void findFirstByCode() {
		Equipment equipment = this.repository.findFirstByCode("1024");
		assertThat(equipment, notNullValue());
		assertThat(equipment.getCode(), is(equalTo("1024")));
	}
}