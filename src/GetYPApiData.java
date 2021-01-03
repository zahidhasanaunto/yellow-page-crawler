import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetYPApiData {

	Database database;
	public GetYPApiData(){
		database = new Database();
		String inline = "";
		try{
			URL url = new URL("http://api.yellowapi.com/FindBusiness/?what=plumbers&where=NEWFOUNDLAND+AND+LABRADOR&fmt=JSON&pgLen=30&apikey=a1s2d3f4g5h6j7k8l9k6j5j4");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
	
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " +responsecode);

			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else {
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
					System.out.println("");
					
					String sql = "INSERT INTO yellowpage(bid, name, street, city, prov, pcode, phn, category, url) "
							+ "VALUES('"+id+"', '"+name+"', '"+street+"', '"+city+"', '"+prov+"', '"+pcode+"', '"+dispPhone+"', '"+category+"', '"+mUrl+"')";
					database.insertData(sql);
					System.out.println("Inserted");
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
