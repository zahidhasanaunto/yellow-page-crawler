import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("Main");
//		new GetYelpDataUsingVPN();
		new SaveDataToExcel();
//		Database database = new Database();
//		ResultSet resultSet = null;
//		String sql = "SELECT * FROM yelp";
//		resultSet = database.getData(sql);
//		int i = 0;
//		while(resultSet.next()){
//			String address = resultSet.getString("mainaddress");
//			int id = resultSet.getInt("id");
//			if (!address.isEmpty() && !address.contains(",,")) {
//				if (address.contains(",")) {
//					String[] _ad = address.split(",");
//					String _address = _ad[1].trim();
//					String _cityAddress = _ad[0].trim();
//					String[] stateZip = _address.split(" ");
//					
//					if (stateZip.length == 2) {
//						System.out.println(i);
//						System.out.println(_address);
//						System.out.println("State: " + stateZip[0]);
//						System.out.println("Zip: " + stateZip[1]);
//						i++;
//					}
//					
//					System.out.println(_cityAddress);
//				}			
//			}
//		}
	}

}
