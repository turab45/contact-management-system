package com.scm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		String name = principal.getName();
		System.out.println(name);
		return "admin/home";
	}
}
