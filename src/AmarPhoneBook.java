import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AmarPhoneBook {

	Database database;
	public AmarPhoneBook(){
	
		database = new Database();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter First: ");
		int a = Integer.parseInt(scanner.nextLine());
		System.out.println("Enter Second: ");
		int b = Integer.parseInt(scanner.nextLine());
		scanner.close();
		
		for(int i = a; i < b; i++){
			String url = "http://www.amarphonebook.com/entries/find/Dhaka/All/1/page:"+i+"";
			
			System.out.println(url);
			getData(url);	
		}
	}

	public void getData(String url) {
		try{
			Document document = Jsoup.connect(url).get();
			Elements list_common = document.select("div.list_common1");
			for(int i = 0; i < list_common.size(); i++){
				Elements list_division = list_common.get(i).select("div.list_division");
				
				Elements image_list = list_division.get(0).select("div.list_gallery_bar");
				Elements images = image_list.get(0).select("a img");
				String img = "www.amarphonebook.com" + images.get(0).attr("src");
				
				Elements details_list = list_division.get(1).select("div.detailinfo");
				String title = details_list.select("p.detailstitle a").text();
				String phone = details_list.select("p.detailsphone").text();
				String address = details_list.select("p.detailsaddress").text();
				String category = details_list.select("p.detailscategory i").text();
				
				database.insertData("INSERT INTO apb(name, phn, address, category, img) "
						+ "VALUES('"+title+"', '"+phone+"', '"+address+"', '"+category+"', '"+img+"')");
				
				
				System.out.println(title);
				System.out.println(phone);
				System.out.println("");
			}
		}catch (IOException e) {
			System.out.println("Internet Connection Error.. ");
			getData(url);
		}
	}			
}
