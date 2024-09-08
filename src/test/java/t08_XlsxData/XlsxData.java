package t08_XlsxData;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XlsxData {
    /* Need Apache POI and Apache POI-OOXML */

    @DataProvider
    public Object[][] getData() throws IOException {
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

        // Create 2D array for containing the data;
        String[][] data = new String[numOfRow - 1][numOfColumn];


//        System.out.println(numOfRow);
        for (int r = 0; r < numOfRow - 1; r++) {
            for (int c = 0; c < numOfColumn; c++) {
                DataFormatter df = new DataFormatter();
                // Format value to String and assign values to the 2D array
                data[r][c] = df.formatCellValue(sheet.getRow(r + 1).getCell(c));
//                System.out.println(df.formatCellValue(sheet.getRow(r).getCell(c)));
            }
        }

//        for (String[] dataArr : data) {
//            System.out.println(Arrays.toString(dataArr));
//        }

        // Always close Workbook and FileInputStream to avoid memory leak
        workbook.close();
        fis.close();

        return data;
    }
}
