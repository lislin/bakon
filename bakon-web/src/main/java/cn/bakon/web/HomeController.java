package cn.bakon.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@Autowired
	private EquipmentRepository equipmentRepository;

	@RequestMapping("/monitor")
	public ModelAndView monitor() {
		this.equipmentRepository.findAll();
		return new ModelAndView("monitor");
	}
}
