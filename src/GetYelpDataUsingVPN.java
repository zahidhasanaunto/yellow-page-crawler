import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

public class GetYelpDataUsingVPN {

	private static final String URL = "https://www.yelp.com";

	private ArrayList<String> categoryList = new ArrayList<>();
	private ArrayList<String> userAgents = new ArrayList<>();
	
	private int startFrom = 0;
	private int pagination = 0;
	private String searchCity, status;
	
	private String photo, mainAddress, web, name, profile, address, phone, category;
	private Elements regSearchResult, searchResultContent;
	private Database database;
	
	
	public GetYelpDataUsingVPN() throws IOException{
		getDownloadInfo();
		getUserAgents();
		getCategories();
		database = new Database();
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0; i < categoryList.size(); i++){
			pagination = 10;
			startFrom = 0;
			category = categoryList.get(i);
			searchCity = "New+York,+NY";
			System.out.println(category);
			getYelpData();
		}
		
//		if (status.equals("Complete")) {
//			if (category.equals("null")) {
//				System.out.println("Enter Category: ");
//				category = scanner.nextLine();
//			}
//			if (searchCity.equals("null")) {
//				System.out.println("Enter City: ");
//				searchCity = scanner.nextLine();
//			}
//			if (startFrom == 0) {
//				System.out.println("Enter Start From: ");
//				startFrom = Integer.parseInt(scanner.nextLine());
//			}
//			if (pagination == 0) {
//				System.out.println("Enter Pagination: ");
//				pagination = Integer.parseInt(scanner.nextLine());
//			}
//			System.out.println("Download is starting");
//			getYelpData();
//		}else if (status.equals("Not Complete")) {
//			System.out.println("Download is starting from previous info");
//			getYelpData();
//		}
	}
	public void saveDownloadInfo(String city, String cat, String sts, int strt, int pagi) throws IOException{
		JSONObject object = new JSONObject();
		object.put("city", city);
		object.put("category", cat);
		object.put("start", strt);
		object.put("pagination", pagi);
		object.put("status", sts);
		
		FileWriter fileWriter = new FileWriter("C:/Users/zahid/Desktop/DonaInternational/info.json");
		fileWriter.write(object.toString());
		fileWriter.flush();
		fileWriter.close();
	}
	public void getYelpData() throws IOException {
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
	public void getDownloadInfo(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/info.json"));
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			string = stringBuilder.toString();
			
			JSONObject object = new JSONObject(string);
			
			searchCity = object.getString("city");
			category = object.getString("category");
			status = object.getString("status");
			startFrom = object.getInt("start");
			pagination = object.getInt("pagination");
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				System.out.println("");
				
				String sql = "INSERT INTO yelp(name, category, address, phone, photo, web, mainaddress) "
						+ "VALUES('"+name+"', '"+category+"', '"+address+"', '"+phone+"', '"+photo+"', '"+web+"', '"+mainAddress+"')";
				database.insertData(sql);
			}
			startFrom += pagination;
			saveDownloadInfo(searchCity, category, "Not Complete", startFrom, pagination);
			getData(url);
		}else {
			System.out.println("Download Complete. Please Start With Another Category & City");
			saveDownloadInfo("null", "null", "Complete", 0, 0);
		}
		
	}
	public void getInfo(String url) throws IOException{
		try {
			Random random = new Random();
			int num = random.nextInt(userAgents.size() - 1) + 0;
			
			Document document = Jsoup.connect(url)
			        .timeout(30000)
			        .userAgent(userAgents.get(num))
			        .referrer("http://www.google.com") 
			        .get();
			
			searchResultContent = document.select("div.search-results-content");
			
			regSearchResult = searchResultContent.select("li.regular-search-result");
		} catch (Exception e) {
//			startFrom++;
			saveDownloadInfo(searchCity, category, "Not Complete", startFrom, pagination);
			System.out.println("Internet Connection Problem. Please Wait");
			getInfo(url);
		}
	}
	public void getDetails(String d_url) throws IOException{
		try {
			Random random = new Random();
			int num = random.nextInt(userAgents.size() - 1) + 0;
			
			Document docProfile = Jsoup.connect(d_url)
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
//			startFrom++;
			saveDownloadInfo(searchCity, category, "Not Complete", startFrom, pagination);
			System.out.println("Internet Connection Problem. Please Wait");
			getDetails(d_url);
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
	public void getCategories(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/cate.json"));
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
}
