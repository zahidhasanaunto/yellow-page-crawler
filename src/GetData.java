import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetData {

	private String inline = "";
	private String id, name, street, city, prov, pcode, dispPhone, category, mUrl, en_logo;
	private ArrayList<String> web = new ArrayList<>();
	private ArrayList<String> categoryList = new ArrayList<>();
	private ArrayList<String> cityList = new ArrayList<>();
	private int count = 1;
	
	public GetData(){
		
		getCategories();
		getCities();
		
		for(int i = 0; i < cityList.size(); i++){
			for(int j = 0; j < categoryList.size(); j++){
				collectData(categoryList.get(j), cityList.get(i));
			}
		}
	}

	public void collectData(String sCategory, String sCity) {
		String[] _sCategory = sCategory.split(" ");
		String[] _sCity = sCity.split(" ");
		if (_sCategory.length > 1) {
			sCategory = sCategory.replace(" ", "+");
		}
		if (_sCity.length > 1) {
			sCity = sCity.replace(" ", "+");
		}
		try {
			URL url = new URL("http://api.yellowapi.com/FindBusiness/?what="+sCategory+"&where="+sCity+"&fmt=JSON&pgLen=1&apikey=a1s2d3f4g5h6j7k8l9k6j5j4");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
	
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);

			if(responsecode != 200){
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			}else {
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
				sc.close();
				
				JSONObject object = new JSONObject(inline);
				JSONArray listings = (JSONArray) object.get("listings");
				for (int i = 0; i < listings.length(); i++) {
					JSONObject data = (JSONObject) listings.get(i);
					
					JSONObject address = (JSONObject) data.get("address");
					JSONObject phone = (JSONObject) data.get("phone");
					JSONArray categories = (JSONArray) data.get("Categories");
					JSONObject cat = (JSONObject) categories.get(0);
					
					id = (String) data.get("id");
					name = (String) data.get("name");
					street = (String) address.get("street");
					city = (String) address.get("city");
					prov = (String) address.get("prov");
					pcode = (String) address.get("pcode");
					dispPhone = (String) phone.get("dispNum");
					category = (String) cat.get("value");
					mUrl = (String) data.get("merchantUrl");
					
					String _id = id;
					String _name = name;
					String _city = city;
					String _prov = prov;
					
					String[] __name = _name.split(" ");
					String[] __city = _city.split(" ");
					
					if (__name.length > 1) {
						_name = _name.replace(" ", "+");
					}
					if (__city.length > 1) {
						_city = _city.replace(" ", "+");
					}
					

					String details = "";
					web.clear();
					
					try {
						URL b_url = new URL("http://api.yellowapi.com/GetBusinessDetails/?prov="+_prov+"&city="+_city+"&bus-name="+_name+"&listingId="+_id+"&fmt=JSON&apikey=a1s2d3f4g5h6j7k8l9k6j5j4&UID=1");
						HttpURLConnection conn2 = (HttpURLConnection)b_url.openConnection();
						conn2.setRequestMethod("GET");
						conn2.connect();
				
						int responsecode2 = conn2.getResponseCode();
						System.out.println("Response code is: " +responsecode2);

						if(responsecode2 != 200){
							throw new RuntimeException("HttpResponseCode: " +responsecode2);
						}else {
							Scanner sc2 = new Scanner(b_url.openStream());
							while(sc2.hasNext())
							{
								details+=sc2.nextLine();
							}
							sc2.close();
							
//							System.out.println(details.toString());
							
							JSONObject b_Details = new JSONObject(details);
							
							if (b_Details.has("logos")) {
								JSONObject logos = (JSONObject) b_Details.get("logos");
								en_logo = (String) logos.get("EN");
							}else {
								System.out.println("NO Logo");
							}
							JSONObject products = (JSONObject) b_Details.get("products");
							JSONArray webUrl = (JSONArray) products.get("webUrl");
							
							if (webUrl.length() > 0) {
								for(int k = 0; k < webUrl.length(); k++){
									String wString = (String) webUrl.get(k);
									web.add(wString);
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println(count);
					System.out.println("ID: "+id);
					System.out.println("Name: "+name);
//					System.out.println("Street: "+street);
					System.out.println("City: "+city);
					System.out.println("Prov: "+prov);
//					System.out.println("PCode: "+pcode);
//					System.out.println("Phone Num: "+dispPhone);
					System.out.println("Category: "+category);
//					System.out.println("Merchant Url: "+mUrl);
					System.out.println(en_logo);
					System.out.println(web.get(0));
					System.out.println("");
					count++;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void getCategories(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/YellowPage/categories.json"));
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
	public void getCities(){
		String string = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/zahid/Desktop/DonaInternational/YellowPage/city_canada.json"));
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			string = stringBuilder.toString();
			
			JSONArray array = new JSONArray(string);
			for(int i = 0; i < array.length(); i++){
				JSONObject object = (JSONObject) array.get(i);
				String str = (String) object.get("city");
				cityList.add(str);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
