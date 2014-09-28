package bg.hackbulgaria.xander.web_crawler_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlingThread extends Thread{
	private static final String URL_PATTER = "<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]";
	private String needle;
	
	private void pageContentSearch(URL startLocation, String needle, boolean crawling) {
		URLConnection conn;
		this.needle = needle;
		try {
			conn = startLocation.openConnection();
			
			Pattern pattern = Pattern.compile(URL_PATTER);
			Matcher matcher;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				if (searchForNeedle(inputLine)) {
					System.out.println("NEEDLE FOUND!");
					crawling = false;
					break;
				}

				matcher = pattern.matcher(inputLine);
				if (matcher.find()) {
					String foundLink = matcher.group();
					
					
				}
			}

			reader.close();
		} catch (IOException e) {
			System.err.println("INVALID URL: "  + startLocation.toString());
		}
	}
	
	private boolean searchForNeedle(String pageContent) {

		if (pageContent.indexOf(needle) == -1) {
			return false;
		} else {
			return true;
		}

	}
}
