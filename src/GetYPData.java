import java.io.IOException;

import java.util.List;

import javax.print.Doc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetYPData {

	private Elements listings;
	
	public GetYPData() throws IOException{

		Document pageDoc = Jsoup.connect("https://www.yellowpages.ca/search/si/50/plumber/Toronto%2C%20ON").get();
		
		listings = pageDoc.getElementsByClass("listing listing--bottomcta placement placementText");
		
		if (!listings.isEmpty()) {
			getData();
		}else {
			listings = pageDoc.getElementsByClass("listing listing--bottomcta ");
			getData();
		}
	}

	public void getData() throws IOException {
		if (!listings.isEmpty()) {
			for(int j = 0; j < listings.size(); j++){
				Elements mainElements = listings.get(j).getElementsByClass("listing__right hasIcon");

				Elements h3Elements = mainElements.select("div.listing__title--wrap h3");
				Elements h3 = h3Elements.select("a");
				
				String title = h3.get(0).text();
				String profile = h3.get(0).attr("href");
				System.out.println(title);
				System.out.println(profile);
				
				Elements addressElements = mainElements.select("div.listing__address");
				Elements allAddress = addressElements.select("span.listing__address--full span");
				for(int i = 0; i < allAddress.size(); i++){
					String add = allAddress.get(i).text();
					System.out.println(add);
				}
				
				Elements secElements = listings.get(j).select("div.listing__mlr__root");

				Elements webElements = secElements.get(0).getElementsByClass("mlr__item mlr__item--website");
				String web = webElements.select("a").attr("href").toString();
				System.out.println(web);
				
				Elements phnElements = secElements.get(0).getElementsByClass("mlr__item mlr__item--more mlr__item--phone jsMapBubblePhone");
				Elements phnNumbers = phnElements.select("ul li");
				String phn = phnNumbers.get(0).text();
				
				System.out.println(phn);
				
				//Image
				Document profileDoc = Jsoup.connect("https://www.yellowpages.ca"+profile).get();
				Elements multimedia = profileDoc.select("ul.multimedia li a img");
				if (!multimedia.isEmpty()) {
					String image = multimedia.get(0).attr("src");
					System.out.println(image);
				}else {
					System.out.println("No Image");
				}
				System.out.println("");	
			}
		}else {
			System.out.println("End of Pages");
		}
	}
}
