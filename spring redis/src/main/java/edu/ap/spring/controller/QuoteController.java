package edu.ap.spring.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.ap.spring.redis.RedisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuoteController {

	@Autowired
	private RedisService service;
	//pattern: Question : Answer

	// redirect naar html pagina om authors te adden met postform
	@GetMapping("")
	public String askQuestion() {
		return "index";
	}
	
}
