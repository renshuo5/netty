package com.rens.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ManagerController {

	@RequestMapping(value={"/","/login"},method=RequestMethod.GET)
	public String login(Model model){
		
		return "manage/login";
	}
	@RequestMapping("/manage")
	public String manage(Model model){
		return "manage/index";
	}
}
