import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BankSwiftCode {

	public BankSwiftCode() throws IOException{
		
		Database database = new Database();
		
		Document document = Jsoup.connect("https://www.theswiftcodes.com/bangladesh/page/19/").get();
		
		Elements tr = document.select("table.swift tr");
		for(int i = 1; i <= 5; i++){
			Elements td = tr.get(i).select("td");
			
			String id = td.get(0).text();
			String name = td.get(1).text();
			String city = td.get(2).text();
			String branch = td.get(3).text();
			String code = td.get(4).text();
			System.out.println("ID: " + id);
			System.out.println("Name: " + name);
			System.out.println("City: "+ city);
			System.out.println("Branch: " + branch);
			System.out.println("Code: " + code);
			
			String sql = "INSERT INTO swiftcode(name, city, branch, code) "
					+ "VALUES('"+name+"', '"+city+"', '"+branch+"', '"+code+"')";

			database.insertData(sql);
			System.out.println("");
		}
		for(int i = 7; i <= 19; i++){
			Elements td = tr.get(i).select("td");
			
			String id = td.get(0).text();
			String name = td.get(1).text();
			String city = td.get(2).text();
			String branch = td.get(3).text();
			String code = td.get(4).text();
			System.out.println("ID: " + id);
			System.out.println("Name: " + name);
			System.out.println("City: "+ city);
			System.out.println("Branch: " + branch);
			System.out.println("Code: " + code);
			String sql = "INSERT INTO swiftcode(name, city, branch, code) "
					+ "VALUES('"+name+"', '"+city+"', '"+branch+"', '"+code+"')";

			database.insertData(sql);

			System.out.println("");
		}
		for(int i = 21; i <= 33; i++){
			Elements td = tr.get(i).select("td");
			
			String id = td.get(0).text();
			String name = td.get(1).text();
			String city = td.get(2).text();
			String branch = td.get(3).text();
			String code = td.get(4).text();
			System.out.println("ID: " + id);
			System.out.println("Name: " + name);
			System.out.println("City: "+ city);
			System.out.println("Branch: " + branch);
			System.out.println("Code: " + code);
			String sql = "INSERT INTO swiftcode(name, city, branch, code) "
					+ "VALUES('"+name+"', '"+city+"', '"+branch+"', '"+code+"')";

			database.insertData(sql);

			System.out.println("");
		}
		for(int i = 35; i <= 47; i++){
			Elements td = tr.get(i).select("td");
			
			String id = td.get(0).text();
			String name = td.get(1).text();
			String city = td.get(2).text();
			String branch = td.get(3).text();
			String code = td.get(4).text();
			System.out.println("ID: " + id);
			System.out.println("Name: " + name);
			System.out.println("City: "+ city);
			System.out.println("Branch: " + branch);
			System.out.println("Code: " + code);
			String sql = "INSERT INTO swiftcode(name, city, branch, code) "
					+ "VALUES('"+name+"', '"+city+"', '"+branch+"', '"+code+"')";

			database.insertData(sql);

			System.out.println("");
		}
		for(int i = 49; i <= 54; i++){
			Elements td = tr.get(i).select("td");
			
			String id = td.get(0).text();
			String name = td.get(1).text();
			String city = td.get(2).text();
			String branch = td.get(3).text();
			String code = td.get(4).text();
			System.out.println("ID: " + id);
			System.out.println("Name: " + name);
			System.out.println("City: "+ city);
			System.out.println("Branch: " + branch);
			System.out.println("Code: " + code);
			String sql = "INSERT INTO swiftcode(name, city, branch, code) "
					+ "VALUES('"+name+"', '"+city+"', '"+branch+"', '"+code+"')";

			database.insertData(sql);

			System.out.println("");
		}

	}
}
