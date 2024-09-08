package t07_CsvData;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ReadCSV {

    @Test
    public static void readDataLineByLine() {
        try {
            FileReader fileReader = new FileReader("src/test/java/t07_CsvData/cvs_sample_data.csv");

            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public static void readAllDataAtOnce() {
        try {
            FileReader fileReader = new FileReader("src/test/java/t07_CsvData/cvs_sample_data.csv");

            CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();

            List<String[]> allData = csvReader.readAll();

            for(String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
