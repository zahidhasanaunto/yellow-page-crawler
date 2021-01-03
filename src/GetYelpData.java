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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetYelpData {

	private static final String URL = "https://www.yelp.com";

	private ArrayList<String> categoryList = new ArrayList<>();
	private ArrayList<Proxy> proxyList = new ArrayList<>();
	private ArrayList<String> userAgents = new ArrayList<>();
	
	private int c;
	private boolean ipCheck = false;
	private int startFrom = 0;
	private int pagination = 10;

	private String photo, mainAddress, web, name, profile, address, phone, category;
	private Elements regSearchResult, searchResultContent;
	private Database database;
	private String searchCity;
	
	public GetYelpData() throws IOException{
		
		getProxies();
		getUserAgents();
		getCategories();
		database = new Database();
		
		shuffleIpList(proxyList);
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
		
//		for(int i = 0; i < categoryList.size(); i++){
//			category = categoryList.get(i);
//			String[] _cat = category.split(" ");
//			String __cat = "";
//			if (_cat.length > 1) {
//				__cat = category.replace(" ", "+");
//			}
//			String url = "https://www.yelp.com/search?find_desc="+__cat+"&find_loc=New+York,+NY&start=";
//			getData(url);
//		}
	}
	public void getData(String url) throws IOException {

		String url2 = url + ""+startFrom+"";
		System.out.println(url2);
		getInfo(0, url2);
		
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
				getDetails(c, d_url);
				
				System.out.println(name);
//				System.out.println(category);
//				System.out.println(address);
				System.out.println(phone);
//				System.out.println(profile);
//				System.out.println(photo);
//				System.out.println(web);
//				System.out.println(mainAddress);
				System.out.println("");
				
				String sql = "INSERT INTO yelp(name, category, address, phone, photo, web, mainaddress) "
						+ "VALUES('"+name+"', '"+category+"', '"+address+"', '"+phone+"', '"+photo+"', '"+web+"', '"+mainAddress+"')";
				database.insertData(sql);
			}
			startFrom += pagination;
			getData(url);
		}
	}
	
	public void getInfo(int n, String url){
		checkIp(n, url);
		if (ipCheck) {
			try {
				Random random = new Random();
				int num = random.nextInt(userAgents.size() - 1) + 0;
				
				Document document = Jsoup.connect(url)
						.proxy(proxyList.get(c))
				        .timeout(30000)
				        .userAgent(userAgents.get(num))
				        .referrer("http://www.google.com") 
				        .get();
				
				searchResultContent = document.select("div.search-results-content");
				
				regSearchResult = searchResultContent.select("li.regular-search-result");
			} catch (Exception e) {
				n++;
				getInfo(n, url);
			}
		}else {
			System.out.println("Connection Problem");
		}
	}
	
	public void getDetails(int n, String d_url){
		checkIp(n, d_url);
		if (ipCheck) {
			try {
				Random random = new Random();
				int num = random.nextInt(userAgents.size() - 1) + 0;
				
				Document docProfile = Jsoup.connect(d_url)
						.proxy(proxyList.get(c))
				        .timeout(30000)
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
				n++;
				getDetails(n, d_url);
			}
		}else {
			System.out.println("Connection Problem");
		}
	}
	
	public void shuffleIpList(ArrayList<Proxy> proxieList){
		int n = proxieList.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(proxieList, i, change);
        }
	}
	
	public void swap(ArrayList<Proxy> proxieList, int i, int change){
		Proxy temp = proxieList.get(i);
        proxieList.set(i, proxieList.get(change));
        proxieList.set(change, temp);
	}
	
	public void checkIp(int count, String url){
		ipCheck = false;
		if (count < proxyList.size()) {
			try {			
				Random random = new Random();
				int num = random.nextInt(userAgents.size() - 1) + 0;
				
				Connection.Response response = Jsoup.connect(url)
						.proxy(proxyList.get(count))
			    		.userAgent(userAgents.get(num))
	                    .timeout(30000)
	                    .execute();
				
				int statusCode = response.statusCode();
				
				if (statusCode == 200) {
					
					c = count;
//					System.out.println(c);
					ipCheck = true;
				}else {
					count++;
//					System.out.println("Here");
					checkIp(count ,url);
				}
			} catch (IOException e) {
				count++;
//				System.out.println("Here Also");
				checkIp(count ,url);
			}
		}else {
			count = 0;
			checkIp(count, url);
		}
	}
	public void getCategories(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/categories.json"));
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			string = stringBuilder.toString();
			
			JSONObject object = new JSONObject(string);
			JSONArray array = (JSONArray) object.get("categories");
			
			for(int i = 0; i < array.length(); i++){
				JSONObject jsonObject = (JSONObject) array.get(i);
				String str = (String) jsonObject.get("category");
				categoryList.add(str);
			}
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getProxies(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/proxylist.json"));
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
				String proxy = object.getString("proxy");
				String p = object.getString("port");
				int port = Integer.parseInt(p);
				
				Proxy proxy2 = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxy, port));
				proxyList.add(proxy2);
			}
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
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
