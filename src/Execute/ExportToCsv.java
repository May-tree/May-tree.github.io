package Execute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ExportToCsv {

    public static void export(ArrayList<HashSet<Integer>> clusterList) {
        try {
            File csv = new File("D:\\File\\Dropbox\\Project\\minHashedAuthors.csv");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true))) {
                bw.newLine();
                bw.write("aid" + "," + "clusterid");
                for (int i = 0; i < clusterList.size(); i++) {
                    for (int j : clusterList.get(i)) {
                        bw.newLine();
                        bw.write((i + 1) + "," + j);
                    }
                }
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

}
