import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AbstractWallpaper {

	private ArrayList<String> catalog = new ArrayList<>();
	
	public AbstractWallpaper() throws IOException{
		
		catalog.add("abstract");
		catalog.add("animals");
		catalog.add("anime");
		catalog.add("cars");
		catalog.add("city");
		catalog.add("flowers");
		catalog.add("food");
		catalog.add("girls");
		catalog.add("hi-tech");
		catalog.add("macro");
		catalog.add("men");
		catalog.add("nature");
		catalog.add("other");
		catalog.add("sport");	
		
		for (int m = 0; m < catalog.size(); m++) {
			for(int j = 2; j <= 10; j++){
				Document document = Jsoup.connect("https://wallpaperscraft.com/catalog/"+catalog.get(m)+"/1440x2560/page"+j+"").get();
				
				Elements element = document.select("div.wallpaper_pre");
				Elements elements = element.select("div.preview_size a");
				for(int i = 0; i < elements.size(); i++){
					String url = elements.get(i).attr("href");
					Document document2 = Jsoup.connect("https:"+url).get();
					Elements ele = document2.select("a.wd_zoom img");
					String img ="https:" + ele.attr("src");
					System.out.println(img);

					Response resultImageResponse = Jsoup.connect(img)
					        .ignoreContentType(true).execute();
					FileOutputStream out = (new FileOutputStream(new java.io.File("C:/Users/zahid/Desktop/Wallpapers/4k/"+catalog.get(m)+"/image"+j+"_"+i+".jpg")));
					out.write(resultImageResponse.bodyAsBytes());
					out.close();
					
				}

			}
		}
		
	}

}
