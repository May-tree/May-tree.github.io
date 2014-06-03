package Import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportFromCsv {

    public static ArrayList<String> fileImport() throws IOException {
        File csv = new File("D:\\File\\Dropbox\\Project\\Authors.csv");
        ArrayList<String> recordList;
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            recordList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                recordList.add(line);
            }
        }
        return recordList;
    }

    public static void main(String args[]) throws IOException {
        fileImport();
        System.out.println("success");
    }
}
