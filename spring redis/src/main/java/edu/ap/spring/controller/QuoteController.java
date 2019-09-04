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

	// redirect naar html pagina om authors te adden met postform
	@GetMapping("/author")
	public String getAuthorForm() {
		return "addAuthor";
	}
	
	@PostMapping("/author")
	public String addAuthor(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			Model model) {

		if (this.service.exists("authorcount")) {
			this.service.incr("authorcount");
		}
		// als er nog geen authors in de count zitten dan krijgt deze automatisch nummer
		// 1
		else {
			this.service.setKey("authorcount", "1");
		}

		// waarde in redisdb toevoegen met als KEY de naam en voornaam en authourcount
		// nummer gescheiden door :
		// VALUE is voornaam en achternaam
		this.service.setKey("author:" + firstName + lastName + ":" + this.service.getKey("authorcount"),
				firstName + " " + lastName);

		return "redirect:listauthors";
	}

	@GetMapping("/listauthors")
	public String listAuthors(Model model) {

		ArrayList<String> authorslist = new ArrayList<String>();

		// voor alle elementen in db met key author haal de value en steek in authorslist
		for (String a : this.service.keys("author:*")) {
			authorslist.add(this.service.getKey(a));
		}

		// attribuut toevoegen met als naam authorsModelAttribute en daarin de waarden
		// van authorlist
		model.addAttribute("authorsModelAttribute", authorslist);

		// html page
		return "listAuthors";
	}

	@PostMapping("/quote")
	public String addQuote(@RequestParam("quote") String quote, @RequestParam("author") String author) {

		String[] authorName = author.split(" ");
		String authorKey = this.service.keys("author:" + authorName[0] + authorName[1] + ":*").iterator().next();
		if (this.service.exists("quotecount")) {
			this.service.incr("quotecount");
		} else {
			this.service.setKey("quotecount", "1");
		}

		// opgeslagen key is quote:id van de author: id van de quote
		// value is de quote die is ingevoerd
		this.service.setKey("quote:" + authorKey.split(":")[2] + ":" + this.service.getKey("quotecount"), quote);

		return "redirect:listquotes/" + authorKey.split(":")[2];
	}

	@GetMapping("/quote")
	public String getQuoteForm(Model model) {

		ArrayList<String> authors = new ArrayList<String>();
		for (String a : this.service.keys("author:*")) {
			authors.add(this.service.getKey(a));
		}
		model.addAttribute("authorsModelAttribute", authors);

		return "addQuote";
	}

	@GetMapping("/listquotes/{authorid}")
	public String listQuotesById(@PathVariable("authorid") int authorid, Model model) {

		ArrayList<String> quotes = new ArrayList<String>();

		for (String a : this.service.keys("quote:" + authorid + ":*")) {
			quotes.add(this.service.getKey(a));
		}

		String author = this.service.getKey(this.service.keys("author:*:" + authorid).iterator().next());
		model.addAttribute("quotes", quotes);
		model.addAttribute("author", author);

		return "listQuotes";
	}
}
