package cn.bakon.web;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.EquipmentStatusRecord;

@RepositoryRestResource(collectionResourceRel = "records", path = "records")
public interface EquipmentStatusRecordRepository extends PagingAndSortingRepository<EquipmentStatusRecord, Long> {
	// 写入状态明细：save
}
