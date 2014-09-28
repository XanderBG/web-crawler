package bg.hackbulgaria.xander.web_crawler_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

	private String startURIAuthority;
	private LinkedList<URL> foundLinks = new LinkedList<>();
	private Set<URL> checkedLinks = new HashSet<>();
	private static final String URL_PATTER = "<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]";
	private String needle;
	private boolean crawling = false;
	private URL needleURL = null;

	public String getStartURIAuthority() {
		return startURIAuthority;
	}

	public void setStartURIAuthority(String startURIAuthority) {
		this.startURIAuthority = startURIAuthority;
	}

	public LinkedList<URL> getFoundLinks() {
		return foundLinks;
	}

	public void setFoundLinks(LinkedList<URL> foundLinks) {
		this.foundLinks = foundLinks;
	}

	public URI crawl(URI link, String needle) {
		try {
			this.needle = needle;

			startURIAuthority = link.getAuthority();

			URL startLocation = link.toURL();
			foundLinks.add(startLocation);
			crawling = true;

			while (foundLinks.size() != 0) {
				if (!crawling) {
					break;
				}
				
				URL currentURL = foundLinks.poll();

				checkedLinks.add(currentURL);

				pageContentSearch(currentURL);
			}

			return needleURL.toURI();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void pageContentSearch(URL startLocation) {
		URLConnection conn;
		try {
			conn = startLocation.openConnection();

			Pattern pattern = Pattern.compile(URL_PATTER);
			Matcher matcher;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				if (searchForNeedle(inputLine)) {
					needleURL = startLocation;
					crawling = false;
					break;
				}

				matcher = pattern.matcher(inputLine);
				if (matcher.find()) {
					String foundLink = matcher.group();

					URL newURL = null;
					try {

						if (foundLink.contains("\"")) {
							newURL = new URL(startLocation,
									foundLink.substring(
											foundLink.indexOf("\"") + 1,
											foundLink.lastIndexOf("\"")));
						} else if (foundLink.contains("'")) {
							newURL = new URL(startLocation,
									foundLink.substring(
											foundLink.indexOf("\'") + 1,
											foundLink.lastIndexOf("\'")));
						} else {
							System.err.println("INVALID URL PARSE: "
									+ inputLine);
						}
						if (checkedLinks.contains(newURL)) {
							continue;
						}
						
						if (newURL.getAuthority().equals(startURIAuthority)) {
							if (!foundLinks.contains(newURL)){
								foundLinks.add(newURL);
							}
						}

					} catch (MalformedURLException mue) {
//						System.err.println("INVALID URL: " + inputLine);
					}
				}
			}

			reader.close();
		} catch (IOException e) {
//			System.err.println("UNABLE TO CONNECT TO URL: "
//					+ startLocation.toString());
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
