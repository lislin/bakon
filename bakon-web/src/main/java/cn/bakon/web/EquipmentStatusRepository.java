package cn.bakon.web;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.EquipmentStatus;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface EquipmentStatusRepository extends
		PagingAndSortingRepository<EquipmentStatus, Long>,
		QueryDslPredicateExecutor<EquipmentStatus> {
	// 查询当前所有状态
	List<EquipmentStatus> findByCurrent(boolean current);
}
