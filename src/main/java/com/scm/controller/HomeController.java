package com.scm.controller;

import java.util.Random;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepository;
import com.scm.entities.UserEntity;
import com.scm.helper.AlertMessage;
import com.scm.service.MailSenderService;

@Controller
public class HomeController {
	
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private MailSenderService mailSenderService;

	Random random = new Random(1000);
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About Us");
		return "about";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("title", "Register");
		model.addAttribute("user",new UserEntity());
		return "register";
	}
	
	@PostMapping("/register_user")
	public String registerUser(@Valid @ModelAttribute("user") UserEntity user,BindingResult result,
			@RequestParam(value = "aggrement", defaultValue = "false") boolean agreement, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}

		if (agreement == true) {

			user.setImage("default.jpg");
			user.setRoll("ROLE_NORMAL");
			user.setStatus("ACTIVE");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			userRepository.save(user);
			
			
			model.addAttribute("user", new UserEntity());
			model.addAttribute("alertClass", "alert-success");
			model.addAttribute("message", "You have registered successfully. Login now.");

			return "register";
		}

		System.out.println("AGREEMENT  " + agreement);
		System.out.println("USER  " + user);

		model.addAttribute("alertClass", "alert-danger");
		model.addAttribute("message", "You must agree terms & conditions.");

		return "register";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword(Model model) {
		
		model.addAttribute("title", "Forgot password");
		return "forgot_password";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email ,Model model, HttpSession session) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity != null) {
			/* GeneratedValue 5 Digits otp */
			
			String otp= new Random().nextInt(999999)+"";
			
			mailSenderService.sendSimpleEmail(email, "Your OTP", "Here is your one time password for forgot password "+otp);
			
			
			model.addAttribute("title", "Verify OTP");
			session.setAttribute("otp", otp);
			session.setAttribute("alert", new AlertMessage("alert alert-success","We have sent a 5 digit OTP to your registered email address. Please verify it."));
			session.setAttribute("user", userEntity);
			return "verify_otp";
		}
		
		session.setAttribute("alert", new AlertMessage("alert alert-danger","Looks like this email is not registered. Please enter your correct email address or sign up."));
		return "redirect:/forgot-password";
		
	}
	
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") String otp, HttpSession session) {
		String oldOtp = (String) session.getAttribute("otp");
		
		if (otp.equals(oldOtp)) {
			System.out.println("OTP verified successfully.");
			return "change_password";
		}
		session.setAttribute("alert", new AlertMessage("alert alert-danger","Your entered OTP doesn't match. Plese enter the correct one."));
		return "verify_otp";
	}
	
	@PostMapping("/change-password")
	public String chagePassword(@RequestParam("password") String password,HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("user");
		
		userEntity.setPassword(passwordEncoder.encode(password));
		userRepository.save(userEntity);
		
		session.setAttribute("alert", new AlertMessage("alert alert-success","Your password has been changed successfully."));
		return "redirect:/signin";
	}
}
