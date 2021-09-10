package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepository;
import com.scm.entities.ContactEntity;
import com.scm.entities.UserEntity;
import com.scm.helper.AlertMessage;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {

		String name = principal.getName();
		System.out.println(name);
		model.addAttribute("title", "User Dashboard");
		return "normal/index";
	}

	// Show the contact form
	@GetMapping("/add-contact")
	public String addContact(Model model, Principal principal) {

		String name = principal.getName();
		System.out.println(name);

		model.addAttribute("title", "Add New Contact");
		model.addAttribute("contact", new ContactEntity());
		return "normal/add_contact";
	}

	// Add a new contact

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") ContactEntity contactEntity, BindingResult result,
			@RequestParam("lname") String lastName, @RequestParam("profileImg") MultipartFile file, Model model,
			Principal principal) {
		if (result.hasErrors()) {
			System.out.println(result);
			model.addAttribute("title", "Add New Contact");
			model.addAttribute("contact", contactEntity);
			return "normal/add_contact";
		}
		try {

			if (!file.isEmpty()) {
				contactEntity.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} else {
				contactEntity.setImage("default.jpg");
			}

			UserEntity userEntity = userRepository.findByEmail(principal.getName());

			contactEntity.setName(contactEntity.getName() + " " + lastName);
			contactEntity.setUser(userEntity);

			contactRepository.save(contactEntity);

			model.addAttribute("alertClass", "alert-success");
			model.addAttribute("message", "Contact has been added successfully.");
			model.addAttribute("contact", new ContactEntity());

		} catch (Exception e) {
			model.addAttribute("alertClass", "alert-danger");
			model.addAttribute("message", "Something went wrong.");
		}
		return "normal/add_contact";
	}

	@GetMapping("/contacts")
	public String viewContacts(Model model, Principal principal) {

		model.addAttribute("title", "User Contacts");
		UserEntity userEntity = userRepository.findByEmail(principal.getName());
		List<ContactEntity> userContacts = contactRepository.findContactsByUser(userEntity.getId());

		model.addAttribute("contacts", userContacts);
		return "normal/view_contacts";
	}

	@GetMapping("/contact/{id}")
	public String viewSingleContact(@PathVariable("id") Integer id, Model model) {
		ContactEntity contactEntity = contactRepository.findById(id).get();

		model.addAttribute("title", contactEntity.getName());
		model.addAttribute("contact", contactEntity);
		return "normal/view_single_contact";
	}

	@GetMapping("/contact/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id, Model model) {
		ContactEntity contactEntity = contactRepository.findById(id).get();

		contactRepository.delete(contactEntity);

		model.addAttribute("alertClass", "alert-success");
		model.addAttribute("message", "Your contact has been deleted.");

		return "redirect:/user/contacts";
	}

	@GetMapping("/contact/update/{id}")
	public String updateContact(@PathVariable("id") Integer id, Model model) {
		ContactEntity contactEntity = contactRepository.findById(id).get();

		model.addAttribute("title", contactEntity.getName());
		model.addAttribute("contact", contactEntity);
		if (contactEntity.getName().split(" ").length > 1) {
			model.addAttribute("fname", contactEntity.getName().split(" ")[0]);
			model.addAttribute("lname", contactEntity.getName().split(" ")[1]);
		} else
			model.addAttribute("fname", contactEntity.getName());
		return "normal/update_contact";
	}

	// update-contact

	@PostMapping("/{id}/update")
	public String updateContact(@ModelAttribute("contact") ContactEntity contactEntity,
			@RequestParam("lname") String lastName, @RequestParam("profileImg") MultipartFile file, HttpSession session, Principal principal, Model model) {

		try {
			ContactEntity oldContact = contactRepository.findById(contactEntity.getId()).get();

			if (!file.isEmpty()) {
				contactEntity.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}else
				contactEntity.setImage(oldContact.getImage());

			UserEntity userEntity = userRepository.findByEmail(principal.getName());

			contactEntity.setName(contactEntity.getName() + " " + lastName);
			contactEntity.setUser(userEntity);

			System.out.println("Contact : " + contactEntity);
			contactRepository.save(contactEntity);

			session.setAttribute("alert", new AlertMessage("alert alert-success","Contact has been updated successfully."));
			

		} catch (Exception e) {
			model.addAttribute("alertClass", "alert-danger");
			model.addAttribute("message", "Something went wrong.");
		}

		System.out.println("ENDING UPDATE ...");
		return "redirect:/user/contact/"+contactEntity.getId();
	}
	
	
	@GetMapping("/profile")
	public String userProfile(Model model, Principal principal) {
		UserEntity userEntity = userRepository.findByEmail(principal.getName());
		
		model.addAttribute("user", userEntity);
		model.addAttribute("title", "My Profile");
		return "normal/profile";
	}
	
	@PostMapping("/profile/update")
	public String processUpdateProfile(@ModelAttribute("user") UserEntity userEntity, @RequestParam("profileImg") MultipartFile file,@RequestParam("lname") String lName,Model model, HttpSession session) {
		UserEntity oldUser = userRepository.findById(userEntity.getId()).get();
		
		try {
			if (!file.isEmpty()) {
				oldUser.setImage(file.getOriginalFilename());
				
				File saveFile = new ClassPathResource("static/img/user").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			oldUser.setName(userEntity.getName()+" "+lName);
			oldUser.setEmail(userEntity.getEmail());
			
			userRepository.save(oldUser);
			
			if (oldUser.getName().split(" ").length > 1) {
				model.addAttribute("fname", oldUser.getName().split(" ")[0]);
				model.addAttribute("lname", oldUser.getName().split(" ")[1]);
			} else
				model.addAttribute("fname", oldUser.getName());
			
			session.setAttribute("alert", new AlertMessage("alert alert-success","Contact has been updated successfully."));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alert", new AlertMessage("alert alert-danger","Somwthing went wrong."));
		}
		
		return "redirect:/user/profile";
	}
	
	
	@GetMapping("/settings")
	public String settings() {
		
		UserEntity userEntity = userRepository.getById(1);
		
		System.out.println(bCryptPasswordEncoder);
		
		return "normal/change_password";
	}
	
	@PostMapping("/change_password")
	public String chnagePassword(@RequestParam("old-password") String oldPassword, @RequestParam("new-password") String newPassword, Principal principal, HttpSession session) {
		
		UserEntity user = userRepository.findByEmail(principal.getName());
		
		
		if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			
			// change the password
			
			user.setPassword(bCryptPasswordEncoder.encode(newPassword));
			
			userRepository.save(user);
			
			session.setAttribute("alert", new AlertMessage("alert alert-success","Your password has been changed successfully."));
			
			return "redirect:/user/settings";
			
		}
		
		session.setAttribute("alert", new AlertMessage("alert alert-danger","Your password doesn't match old password."));
		return "redirect:/user/settings";
	}
}
