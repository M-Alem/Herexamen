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
	@GetMapping("/ask")
	public String getQuestionForm(Model model) {
		return "index";
	}

	@PostMapping("/ask")
	public String AskQuestion(@RequestParam("questionText") String questionText, Model model) {
		/*answerList.add( "It is certain.");
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
		answerList.add( " Very doubtful.");*/
		//answerList.get(rdmgenerator.nextInt(answerList.size()));
		String answerText =  " Very doubtful.";
		
		if (this.service.exists("authorcount")) {
			this.service.incr("authorcount");
		}
		else {
			this.service.setKey("authorcount", "1");
		}

		if (this.service.exists("quotecount")) {
			this.service.incr("quotecount");
		}
		else {
			this.service.setKey("quotecount", "1");
		}

		this.service.setKey("question:" + questionText + ":" + this.service.getKey("authorcount"), questionText);
		this.service.setKey("answer:" + this.service.getKey("authorcount") + ":" + this.service.getKey("quotecount"), answerText);
		String authorKey = this.service.keys("question:" + questionText + ":*").iterator().next();
		return "redirect:answer/"+authorKey.split(":")[2];
	}

	@GetMapping("/answer/{authorid}")
	public String answer(@PathVariable("authorid") int authorid,Model model) {
		String question = this.service.keys("question:*" + ":" + authorid).iterator().next();
		String answer = "Most likely.";
		model.addAttribute("question", question);
		model.addAttribute("answer", answer);
		return "answer";
	}
	
}
