package cn.bakon.web;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.Setting;

@RepositoryRestResource(collectionResourceRel = "setting", path = "setting")
public interface SettingRepository extends CrudRepository<Setting, Integer> {
	Setting findFirstByOrderByIdAsc();
}
