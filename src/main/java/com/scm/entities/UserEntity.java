package com.scm.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "USER")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	@NotBlank(message = "User name can't be null.")
	private String name;
	@Column(unique = true)
	@Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Must be a well formed email address.")
	private String email;
	@NotBlank(message = "Please provide a suitable password.")
	private String password;
	private String image;
	private String status;
	private String roll;
	


	public Integer getId() {
		return userId;
	}

	public void setId(Integer id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	


	public String getRoll() {
		return roll;
	}

	public void setRoll(String roll) {
		this.roll = roll;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + ", image="
				+ image + ", status=" + status + ", roll=" + roll + "]";
	}
	
	
	
	
	
}
