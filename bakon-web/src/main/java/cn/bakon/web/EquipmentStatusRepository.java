package cn.bakon.web;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cn.bakon.domain.EquipmentStatus;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface EquipmentStatusRepository extends
		PagingAndSortingRepository<EquipmentStatus, Long> {
	List<EquipmentStatus> findByCurrent(@Param(value = "current") boolean current);

	@Query("select s from EquipmentStatus s where time>=?1 and time<=?2 and s.type=?3 and status=?4 and position=?5")
	List<EquipmentStatus> findAllByPosition(
			@Param(value = "begin") Date begin,
			@Param(value = "end") Date end,
			@Param(value = "type") String type,
			@Param(value = "status") String status,
			@Param(value = "position") String position);

	@Query("select s from EquipmentStatus s where time>=?1 and time<=?2 and s.type=?3 and status=?4")
	List<EquipmentStatus> findAll(
			@Param(value = "begin") Date begin,
			@Param(value = "end") Date end,
			@Param(value = "type") String type,
			@Param(value = "status") String status);
}
