package cn.bakon.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bakon.domain.Equipment;
import cn.bakon.domain.EquipmentStatus;

@Controller
public class HomeController {
	private final static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private EquipmentRepository equipmentRepository;
	@Autowired
	private EquipmentStatusRepository statusRepository;

	private Timer timer;

	@RequestMapping("/monitor")
	@ResponseBody
	public List<EquipmentStatus> monitor() {
		if (this.timer == null) {
			this.timer = new Timer("MONITOR", true);
			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						doMonitor();
					} catch (Exception e) {
						LOGGER.error("unkown monitor error", e);
					}
				}
			}, 1000, 10000);
		}

		return this.doMonitor();
	}

	@RequestMapping(value = "/api/updateEquipments", method = RequestMethod.POST)
	@ResponseBody
	public void updateEquipments(@RequestBody List<Equipment> equipments) {
		this.equipmentRepository.deleteAll();
		this.equipmentRepository.save(equipments);
	}

	@RequestMapping(value = "/api/getAllStatus", method = RequestMethod.GET)
	@ResponseBody
	public List<EquipmentStatus> getAllStatus() {
		return this.statusRepository.findByCurrent(true);
	}

	private List<EquipmentStatus> doMonitor() {
		List<Equipment> equipments = (List<Equipment>) this.equipmentRepository.findAll();
		List<EquipmentStatus> equipmentStatus = this.statusRepository.findByCurrent(true);
		List<EquipmentStatus> equipmentStatus2 = new ArrayList<EquipmentStatus>();
		for (Equipment equipment : equipments) {
			try {
				// FIXME 查询电压
				// 判断是否正常、异常、离线
				String statusStr = new String[] { "正常", "异常", "离线" }[(int) (System.currentTimeMillis() % 3)];
				String value = "80";
				equipmentStatus2.add(this.checkStatus(
						equipment,
						this.findStatus(equipmentStatus, equipment),
						statusStr, value));
			} catch (Exception e) {
				LOGGER.error("monitor error: " + equipment, e);
			}
		}
		return equipmentStatus2;
	}

	private EquipmentStatus checkStatus(
			Equipment e, EquipmentStatus status, String statusStr, String value) {
		if (status != null && statusStr.equalsIgnoreCase(status.getStatus())) {
			status.addDuration(200);
			status.setCurrent(true);
		} else {
			if (status != null) {
				status.setCurrent(false);
				this.statusRepository.save(status);
			}

			status = new EquipmentStatus(
					e.getPosition(),
					e.getType(),
					e.getHost(),
					e.getPort(),
					new Date(),
					value,
					e.getThreshold(), statusStr);
			status.setCurrent(true);
		}

		this.statusRepository.save(status);
		return status;
	}

	private EquipmentStatus findStatus(List<EquipmentStatus> status, Equipment e) {
		for (EquipmentStatus equipmentStatus : status) {
			// type&host&port
			if (e.getType().equals(equipmentStatus.getType())
					&& e.getHost().equals(equipmentStatus.getHost())
					&& e.getPort() == equipmentStatus.getPort())
				return equipmentStatus;
		}
		return null;
	}
}
