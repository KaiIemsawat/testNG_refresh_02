package t08_XlsxData;




import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XlsxData {
    /* Need Apache POI and Apache POI-OOXML */

    public static void main(String[] args) throws IOException {
        // Create file based on the existing xlsx file
        File excelFile = new File("src/test/java/t08_XlsxData/sample_data.xlsx");
//        System.out.println(excelFile.exists()); // check if file existed

        // Create raw data using 'FileInputStream'
        FileInputStream fis = new FileInputStream(excelFile);

        /* Workbook -> Sheet -> Row -> Column -> Cell */
        // Convert raw data to Workbook
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // Get Sheet from workbook
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        // Get Row (get number of row first)
        int numOfRow = sheet.getPhysicalNumberOfRows(); // This includes header row
        // Get Column
        int numOfColumn = sheet.getRow(0).getLastCellNum();
//        System.out.println(numOfRow);
        for (int r = 1; r < numOfRow; r++) {
            for (int c = 0; c < numOfColumn; c++) {
                DataFormatter df = new DataFormatter();
                df.formatCellValue(sheet.getRow(r).getCell(c)); // Format value to String
                System.out.println(df.formatCellValue(sheet.getRow(r).getCell(c)));
            }
            System.out.println();
        }


        // Always close Workbook and FileInputStream to avoid memory leak
        workbook.close();
        fis.close();
    }
}
