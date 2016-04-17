package cn.bakon.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.EquipmentStatusRecord;

@RepositoryRestResource(collectionResourceRel = "records", path = "records")
public interface EquipmentStatusRecordRepository extends PagingAndSortingRepository<EquipmentStatusRecord, Long> {
	Page<EquipmentStatusRecord> findByEquipmentId(int equipmentId, Pageable papageable);
}
