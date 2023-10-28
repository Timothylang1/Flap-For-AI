package ConstantFinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class CsvMaker {
    private FileWriter fileWriter;
    private BufferedWriter buffWriter;
    private StringBuilder builder = new StringBuilder();




    public CsvMaker() {
        File csvFile = new File("./src/ConstantFinder/Data/GenerationsData_1.csv");
        try {
            fileWriter = new FileWriter(csvFile, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        buffWriter = new BufferedWriter(fileWriter);
    }

    public void add_to_csv(String text) {
        builder.append(text.replace("\t", ","));
        try {
            buffWriter.append(builder.subSequence(0, builder.length()));
            buffWriter.newLine();

            builder.delete(0, builder.length());
            buffWriter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads through all the GenerationsDataN.csv files
     * 
     * @return a list of a list of row data
     */
    public static ArrayList<ArrayList<HashMap<String, Double>>> read_csv_files() {
        String pathName = "src/ConstantFinder/Data (2000 trials, Max Gen 30, Num Inputs 3)/";
        String fileName = "GenerationsData0.csv";
        ArrayList<ArrayList<HashMap<String, Double>>> simulations = new ArrayList<>();

        try {   
            FileReader reader = new FileReader(pathName + fileName);
            int counter = 0;

            while (reader != null) {
                simulations.add(read_csv(pathName + fileName));
                counter += 1;
                fileName = "GenerationsData" + counter + ".csv";
                reader.close();
                reader = new FileReader(pathName + fileName);
            }
        }
        catch (Exception e) {
            System.out.println("Path name could not be found!");
            return simulations;
        }
        return simulations;
    }

    
    /**
     * Reads all the lines of a single csv file
     * 
     * @return A list of row data in the csv file
     */
    public static ArrayList<HashMap<String, Double>> read_csv(String pathName) {
        ArrayList<HashMap<String, Double>> simData = new ArrayList<HashMap<String, Double>>();
        String line;
        List<String> keys = new ArrayList<String>();

        try {
            FileReader reader = new FileReader(pathName);
            BufferedReader buffReader = new BufferedReader(reader);
            boolean isFirst = true;

            while ((line = buffReader.readLine()) != null) {
                if (isFirst) {
                    String[] lines = line.split(",");
                    keys = Arrays.asList(lines);
                    isFirst = false;
                } else {
                    String[] lines = line.split(",");
                    List<String> values = Arrays.asList(lines);
                    HashMap<String, Double> rowData = new HashMap<String, Double>();
                    for (int i=0; i < values.size(); i++) {
                        rowData.put(keys.get(i), Double.parseDouble(values.get(i)));
                    }
                    simData.add(rowData);   
                }
            }
            reader.close();
            buffReader.close();
        } catch (Exception e) {
            System.out.println("This is not a valid file path!");
        }

        return simData;
    }

    public static void main(String[] args) throws IOException {
        // String numbers = "0.0\t1.0\t2.0\t3.0\t4.0\t5.0\t6.0";

        String text = "Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations";
        

        // test.add_to_csv(text);
        // test.add_to_csv(text);

        HashMap<String, Double> map = new HashMap<String, Double>();
        String pathName = "src/ConstantFinder/Data (2000 trials, Max Gen 30, Num Inputs 3)/";
        String fileName = "GenerationsData20.csv";
        read_csv(pathName + fileName);
    



        


    }
}
