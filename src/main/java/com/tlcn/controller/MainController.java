package com.tlcn.controller;


import com.tlcn.dto.ModelShowNotify;
import com.tlcn.service.NotifyEventService;
import com.tlcn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@Component
public class MainController {
	private final UserService userService;
    private final NotifyEventService notifyEventService;
	
	
	@Autowired
    public MainController(UserService userService, NotifyEventService notifyEventService) {
		super();
        this.userService = userService;
        this.notifyEventService = notifyEventService;
    }
	@RequestMapping(value="/show-all-notify", method = RequestMethod.GET)
	public String showAllNotify(Model model){
		model.addAttribute("MODE", "MODE_SHOW_ALL_NOTIFY");
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(userService.GetUser());
		if(listNotify.size() < 15)
			model.addAttribute("listNotify", listNotify);
		else
			model.addAttribute("listNotify", listNotify.subList(0, 15));
		return "Index";
	}
	@RequestMapping(value="/show-all-notify", method = RequestMethod.POST)
	@ResponseBody
	public String getMoreNotify(@RequestParam("numberNotify") int numberNotify){
		return notifyEventService.getMoreNotify(numberNotify, userService.GetUser());
	}
	
	// page Login
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String Login(){
		return "Login";
	}
	
	// page warnning out time Session
	@RequestMapping(value="/invalidSession", method = RequestMethod.GET)
	public String invalidSession(){
		return "invalidSession";
	}
	//page noitify access denied
	@RequestMapping(value="/accessDenied", method = RequestMethod.GET)
	public String accessDenied(){
		return "accessDenied";
	}
	
	@RequestMapping(value="/hackerDetected", method = RequestMethod.GET)
	public String hackerDetected(){
		return "hackerDetected";
	}
}
