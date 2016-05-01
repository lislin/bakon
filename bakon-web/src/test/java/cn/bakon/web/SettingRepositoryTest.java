package cn.bakon.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bakon.WebApplication;
import cn.bakon.domain.Setting;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
public class SettingRepositoryTest {
	@Autowired
	SettingRepository repository;

	@Test
	public void findFirst() {
		Setting setting = this.repository.findFirstByOrderByIdAsc();
		assertThat(setting.getFrequency(), is(equalTo(200)));
		assertThat(setting.getGraph(), is(equalTo("{}")));
	}
}