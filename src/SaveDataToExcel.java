import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaveDataToExcel {

	Database database = new Database();
	ResultSet rs = null;
	public SaveDataToExcel() throws SQLException, IOException{
		String sql = "SELECT name, category, phone, photo, mainaddress FROM yelp";
		rs = database.getData(sql);
		
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("yelp");
        XSSFRow row = spreadsheet.createRow(1);
        XSSFCell cell;
        
        cell = row.createCell(0);
        cell.setCellValue("Name");
        
        cell = row.createCell(1);
        cell.setCellValue("Phone Number");
        
        cell = row.createCell(2);
        cell.setCellValue("Category");
        
        cell = row.createCell(3);
        cell.setCellValue("Image");
        
        cell = row.createCell(4);
        cell.setCellValue("Address");
        
        cell = row.createCell(5);
        cell.setCellValue("State");
        
        cell = row.createCell(6);
        cell.setCellValue("Zipcode");
        
        int i = 2;
        
        while (rs.next()) {
            row = spreadsheet.createRow(i);
            
            cell = row.createCell(0);
            cell.setCellValue(rs.getString("name"));
            
            cell = row.createCell(1);
            cell.setCellValue(rs.getString("phone"));
            
            cell = row.createCell(2);
            cell.setCellValue(rs.getString("category"));
            
            cell = row.createCell(3);
            cell.setCellValue(rs.getString("photo"));
            
            
            String address = rs.getString("mainaddress");
            if (!address.isEmpty() && !address.contains(",,")) {
				if (address.contains(",")) {
					String[] _ad = address.split(",");
					String _address = _ad[1].trim();
					String _cityAddress = _ad[0].trim();
					String[] stateZip = _address.split(" ");
					
					String __addrerss = _cityAddress;
					cell = row.createCell(4);
		            cell.setCellValue(__addrerss);
					
					if (stateZip.length == 2) {
						
						String state = stateZip[0];
						String zip = stateZip[1];
						cell = row.createCell(5);
			            cell.setCellValue(state);
			            cell = row.createCell(6);
			            cell.setCellValue(zip);
					}
				}			
			}      
            i++;
        }
        FileOutputStream out = new FileOutputStream(new File(
                "yelp.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("File Successfully created");
	}
}
