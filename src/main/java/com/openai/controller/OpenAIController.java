package com.openai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.QueryParam;
import com.openai.service.OpenAIService;

@RestController
public class OpenAIController {
	
	
	@Autowired
	private OpenAIService openAIService;
	@PostMapping("/start")
	public String startMinutes(@QueryParam(value = "fileName") String fileName) {
		System.out.println("FileName: " + fileName);
		return openAIService.runMinutes(fileName);
	}


}
