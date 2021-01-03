import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetDYPData {
	int page = 1;
	int idn = 3;
	Database database = new Database();
	
	public GetDYPData() throws IOException{
		
		getData();
	}

	public void getData() throws IOException {
		Document document = Jsoup.connect("http://www.dhakayellowpages.com/view1.php?page="+page+"&idn="+idn+"").get();
		Elements tables = document.select("table");
		if (tables.size() > 8) {
			for(int i = 4; i < tables.size()- 4; i++){
				DhakaYP dYp = new DhakaYP();
				Elements tr = tables.get(i).select("tr");
				for (int j = 0; j < tr.size(); j++) {
					
					if (j == 0) {
						Elements td = tr.get(j).select("td");
						dYp.setName(td.get(1).text());
					}
					if (j == 1) {
						Elements td = tr.get(j).select("td");
						dYp.setAddress(td.get(1).text());
					}
					if (j == 2) {
						Elements td = tr.get(j).select("td");
						dYp.setCity(td.get(1).text());
					}
					if (j == 3) {
						Elements td = tr.get(j).select("td");
						dYp.setCountry(td.get(1).text());
					}
					if (j == 4) {
						Elements td = tr.get(j).select("td");
						dYp.setPhone(td.get(1).text());
					}
					if (j == 5) {
						Elements td = tr.get(j).select("td");
						dYp.setFax(td.get(1).text());
					}
					if (j == 6) {
						Elements td = tr.get(j).select("td");
						dYp.setEmail(td.get(1).text());
					}
					if (j == 7) {
						Elements td = tr.get(j).select("td");
						dYp.setWeb(td.get(1).text());
					}
					if (j == 8) {
						Elements td = tr.get(j).select("td");
						dYp.setProduct(td.get(1).text());
					}
				}
				
				String sql = "INSERT INTO dhakayp(name, address, city, country, phone, fax, email, web, product) "
						+ "VALUES('"+dYp.getName()+"', '"+dYp.getAddress()+"', '"+dYp.getCity()+"', '"+dYp.getCountry()+"', '"+dYp.getPhone()+"', '"+dYp.getFax()+"', '"+dYp.getEmail()+"', '"+dYp.getWeb()+"', '"+dYp.getProduct()+"')";
				database.insertData(sql);
				System.out.println(dYp.getName());
			}
			page++;
			System.out.println(page);
			getData();
		}else {
			System.out.println("End of page");
			idn++;
			page = 1;
			System.out.println(idn);
			if (idn < 466) {
				getData();
			}else {
				System.out.println("End");
			}
		}
	}
}
