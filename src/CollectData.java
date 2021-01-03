import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CollectData {

	public CollectData(){
		String inline = "";
		String bDetails = "";
		
		try
		{
			URL url = new URL("http://api.yellowapi.com/FindBusiness/?what=electronic&where=toronto&fmt=JSON&pgLen=30&apikey=a1s2d3f4g5h6j7k8l9k6j5j4");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
	
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);

			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
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
					
					String id = (String) data.get("id");
					String name = (String) data.get("name");
					String street = (String) address.get("street");
					String city = (String) address.get("city");
					String prov = (String) address.get("prov");
					String pcode = (String) address.get("pcode");
					String dispPhone = (String) phone.get("dispNum");
					String category = (String) cat.get("value");
					String mUrl = (String) data.get("merchantUrl");
					
					System.out.println(id);
					System.out.println(name);
					System.out.println(street);
					System.out.println(city);
					System.out.println(prov);
					System.out.println(pcode);
					System.out.println(dispPhone);
					System.out.println(category);
					System.out.println(mUrl);
					
					String[] nameSplit = name.split(" ");
					String[] citySplit = city.split(" ");
					if (nameSplit.length > 1) {
						name = name.replace(" ", "+");
					}
					if (citySplit.length > 1) {
						city = city.replace(" ", "+");
					}
					
					URL b_Url = new URL("http://api.yellowapi.com/GetBusinessDetails/?prov="+prov+"&city="+city+"&bus-name="+name+"&listingId="+id+"&fmt=JSON&apikey=a1s2d3f4g5h6j7k8l9k6j5j4&UID=1");
					HttpURLConnection conn2 = (HttpURLConnection)b_Url.openConnection();
					conn2.setRequestMethod("GET");
					conn2.connect();
			
					int responsecode2 = conn2.getResponseCode();
					System.out.println("Response code is: " +responsecode2);
					if(responsecode2 != 200)
						throw new RuntimeException("HttpResponseCode: " +responsecode2);
					else
					{
						Scanner sc2 = new Scanner(b_Url.openStream());
						while(sc2.hasNext())
						{
							bDetails+=sc2.nextLine();
						}
						sc2.close();
						
						JSONObject b_Details = new JSONObject(bDetails);
						
						if (b_Details.has("logos")) {
							JSONObject logos = (JSONObject) b_Details.get("logos");
							String en_logo = (String) logos.get("EN");
							System.out.println(en_logo);
						}else {
							System.out.println("NO Logo");
						}
//						
						
						JSONObject products = (JSONObject) b_Details.get("products");
						JSONArray webUrl = (JSONArray) products.get("webUrl");
						
						if (webUrl.length() > 0) {
							for(int k = 0; k < webUrl.length(); k++){
								String uString = (String) webUrl.get(k);
								System.out.println(uString);
							}
						}
//						
					}
					
					
					System.out.println("");
					
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
