package cn.bakon.web;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.Equipment;

@RepositoryRestResource(collectionResourceRel = "equipments", path = "equipments")
public interface EquipmentRepository extends PagingAndSortingRepository<Equipment, Integer> {
	// 保存设计：deleteAll,save(list)
	// 轮询状态：findAll
}
