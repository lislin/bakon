package cn.bakon.web;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.Equipment;

@RepositoryRestResource(collectionResourceRel = "equipments", path = "equipments")
public interface EquipmentRepository extends PagingAndSortingRepository<Equipment, Integer> {
	Equipment findByCode(@Param("code") String code);
}
