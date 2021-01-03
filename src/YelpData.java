import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YelpData {

	private static final String URL = "https://www.yelp.com";
	
	private ArrayList<String> userAgents = new ArrayList<>();
	private int startFrom = 0;
	private int pagination = 10;
	
	private String photo, mainAddress, web, name, profile, address, phone, category;
	private Elements regSearchResult, searchResultContent;
	private Database database;
	private String searchCity;
	
	Proxy proxy;
	
	public YelpData() throws IOException{
		getUserAgents();
		database = new Database();
//		System.setProperty("http.proxyHost", "127.0.0.1");
//		System.setProperty("http.proxyPort", "3306");
		
		proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("43.250.80.209", 8080));
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter City: ");
		searchCity = scanner.nextLine();
		
		System.out.println("Enter Category: ");
		category = scanner.nextLine();
		
		System.out.println("Enter Start From: ");
		startFrom = Integer.parseInt(scanner.nextLine());
		
		System.out.println("Enter Pagination: ");
		pagination = Integer.parseInt(scanner.nextLine());
		
		System.out.println("Getting Data");
		
		String[] _cat = category.split(" ");
		String __cat = "";
		if (_cat.length > 1) {
			__cat = category.replace(" ", "+");
		}else {
			__cat = category;
		}
		String url = "https://www.yelp.com/search?find_desc="+__cat+"&find_loc="+searchCity+"&start=";
		getData(url);
		
	}
	public void getData(String url) throws IOException {

		String url2 = url + ""+startFrom+"";
		System.out.println(url2);
		getInfo(url2);
		
		if (!searchResultContent.isEmpty()) {
			for(Element element : regSearchResult){
				Elements mediaStory = element.select("div.media-story");
				Elements title = mediaStory.select("h3 span a");
				Elements secAttribute = element.select("div.secondary-attributes");
				
				name = title.select("span").text().toString();
				profile = title.attr("href");
				address = secAttribute.select("div.service-area").text().toString();
				phone = secAttribute.select("span.biz-phone").text().toString();
				
				String d_url = URL + profile;
				getDetails(d_url);
				
				System.out.println(name);
				System.out.println(phone);
				System.out.println("");
				
				String sql = "INSERT INTO yelp(name, category, address, phone, photo, web, mainaddress) "
						+ "VALUES('"+name+"', '"+category+"', '"+address+"', '"+phone+"', '"+photo+"', '"+web+"', '"+mainAddress+"')";
				database.insertData(sql);
			}
			startFrom += pagination;
			getData(url);
		}else {
			System.out.println("Page is Empty");
		}
	}
	public void getDetails(String d_url){
		try {
			Random random = new Random();
			int num = random.nextInt(userAgents.size() - 1) + 0;
			
			Document docProfile = Jsoup.connect(d_url)
			        .timeout(30000).proxy(proxy)
			        .userAgent(userAgents.get(num))
			        .referrer("http://www.google.com") 
			        .get();
			
			Elements images = docProfile.select("div.showcase-photo-box a img");
			if (!images.isEmpty()) {
				photo = images.attr("src").toString();
			}else {
				photo = "";
			}
			
						
			Elements mapText = docProfile.select("div.mapbox-text");
			if (!mapText.isEmpty()) {
				mainAddress = mapText.select("address").text().toString();			
				Elements webSite = mapText.select("ul li");
				if (!webSite.isEmpty()) {
					web = webSite.get(2).select("span a").text().toString();
					if (web.contains("more stations")) {
						web = "";
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("Exception: GetDetails");
			getDetails(d_url);
		}
	}
	public void getInfo(String url){
	
		Random random = new Random();
		int num = random.nextInt(userAgents.size() - 1) + 0;
		
		Document document;
		try {
			document = Jsoup.connect(url)
			        .timeout(30000).proxy(proxy)
			        .userAgent(userAgents.get(num))
			        .referrer("http://www.google.com") 
			        .get();
			searchResultContent = document.select("div.search-results-content");
			
			regSearchResult = searchResultContent.select("li.regular-search-result");
		} catch (IOException e) {
			System.out.println("Exception: GetInfo");
			getInfo(url);
		
		}
		
		
	}
	
	public void getUserAgents(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/agentlist.json"));
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			string = stringBuilder.toString();
			
			JSONArray jsonArray = new JSONArray(string);
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject object = (JSONObject) jsonArray.get(i);
				String agent = object.getString("agent");
				
				userAgents.add(agent);
			}
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
