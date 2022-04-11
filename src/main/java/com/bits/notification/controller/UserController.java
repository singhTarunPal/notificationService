package com.bits.notification.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class UserController {
	
	@GetMapping("/api/notification/about")
	public String about() {
		return "Notification Service - By Tarun Pal Singh";
    }   
	
}
