package edu.ap.spring.controller;

import java.util.ArrayList;
import java.util.Random;

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

	ArrayList<String> answerList = new ArrayList<String>();

	@Autowired
	private RedisService service;
	//pattern: Question : Answer

	Random rdmgenerator;

	// redirect naar html pagina om authors te adden met postform
	@GetMapping("")
	public String getQuestionForm(Model model) {
		answerList.add( "It is certain.");
		answerList.add( "It is decidedly so.");
		answerList.add( "Without a doubt.");
		answerList.add( "Yes - definitely.");
		answerList.add( " As I see it, yes.");
		answerList.add( "Most likely.");
		answerList.add( "Outlook good.");
		answerList.add( "Ask again later.");
		answerList.add( "Cannot predict now.");
		answerList.add( " My reply is no.");
		answerList.add( " Outlook not so good.");
		answerList.add( " Very doubtful.");
		model.addAttribute("answerAttribute", answerList.get(rdmgenerator.nextInt(answerList.size())));
		return "index";
	}

	@PostMapping("")
	public String AskQuestion(@RequestParam("questionText") String questionText, @RequestParam("answerText") String answerText, Model model) {

		this.service.setKey("question:" + questionText, answerText);
		return "redirect:answer/"+questionText;
	}

	@GetMapping("/answer/{questionText}")
	public String answer(@PathVariable("questionText") int questionText, Model model) {
		String answer = this.service.keys("question:" + questionText + ":*").iterator().next();
		model.addAttribute("question", questionText);
		model.addAttribute("answer", answer);
		return "";
	}
	
}
