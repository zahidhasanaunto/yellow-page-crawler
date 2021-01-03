import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {

	public static void main(String[] args) throws IOException, SQLException {
		
		new YelpData();
//		new GetYelpData();	
//		new BankSwiftCode();
//		new AbstractWallpaper();
		
//		String sql = "SELECT * FROM swiftcode";
//		
//		Database database = new Database();
//		ResultSet resultSet = database.getData(sql);
//		
//		FileWriter writer = new FileWriter("C:/Users/zahid/Desktop/Android Projects/BankSwiftCode/swiftcode.json");
//		JSONArray array = new JSONArray();
//		while(resultSet.next()){
//			int id = resultSet.getInt("id");
//			String name = resultSet.getString("name");
//			String city = resultSet.getString("city");
//			String branch = resultSet.getString("branch");
//			String code = resultSet.getString("code");
//			
//			JSONObject object = new JSONObject();
//			object.put("id", id);
//			object.put("name", name);
//			object.put("city", city);
//			object.put("branch", branch);
//			object.put("code", code);
//			array.put(object);
//		}
//		System.out.println(array.toString());
//		writer.write(array.toString());
	}

}
