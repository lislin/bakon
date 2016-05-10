package cn.bakon.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysema.query.types.Predicate;

import cn.bakon.domain.Equipment;
import cn.bakon.domain.EquipmentStatus;

@Controller
public class HomeController {
	@Autowired
	private EquipmentRepository equipmentRepository;
	@Autowired
	private EquipmentStatusRepository statusRepository;

	@RequestMapping("/monitor")
	public ModelAndView monitor() {
		this.equipmentRepository.findAll();
		return new ModelAndView("monitor");
	}

	@RequestMapping(value = "/api/updateEquipments", method = RequestMethod.POST)
	@ResponseBody
	public void updateEquipments(@RequestBody List<Equipment> equipments) {
		this.statusRepository.save(new EquipmentStatus("T01", "T", "127.0.0.1", 1, new Date(), "80", "80", "正常"));
		this.statusRepository.save(new EquipmentStatus("W01", "W", "127.0.0.1", 1, new Date(), "80", "80", "异常"));
		this.statusRepository.save(new EquipmentStatus("W02", "W", "127.0.0.1", 2, new Date(), "80", "80", "异常"));
		this.equipmentRepository.deleteAll();
		this.equipmentRepository.save(equipments);
	}

	@RequestMapping(value = "/api/getAllStatus", method = RequestMethod.GET)
	@ResponseBody
	public List<EquipmentStatus> getAllStatus() {
		return this.statusRepository.findByCurrent(true);
	}
}
