package cn.bakon.web;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.EquipmentStatus;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface EquipmentStatusRepository extends PagingAndSortingRepository<EquipmentStatus, Integer> {
}
