package bg.hackbulgaria.xander.web_crawler_app;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class AppTest {

	@Test
	public void testURLs() throws URISyntaxException {
		
		WebCrawler crawler = new WebCrawler();
		
		URI startLocation = new URI("http://ebusiness.free.bg");
	    URI link = crawler.crawl(startLocation, "Револвираща"); 
	    assertEquals(link.toString(), "http://ebusiness.free.bg/cards_bank_cards.html");
	    
	    startLocation = new URI("http://blog.hackbulgaria.com");
	    link = crawler.crawl(startLocation,
	            "Като страничен ефект, особено при момчетата, може да бъде бързо-растяща брада.");
	    assertEquals(link.toString(), "http://blog.hackbulgaria.com/fall-of-the-hackers/");
	    
		startLocation = new URI("http://fmi.wikidot.com");
	    link = crawler.crawl(startLocation, "Докажете, че:");
	    assertEquals(link.toString(), "http://fmi.wikidot.com/anal-examination-1");
	    
	}

}
