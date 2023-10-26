package ConstantFinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvMaker {
    private FileWriter fileWriter;
    private BufferedWriter buffWriter;
    private StringBuilder builder;


    CsvMaker(File csvFile, StringBuilder builder) throws IOException{
        fileWriter = new FileWriter(csvFile, true);
        buffWriter = new BufferedWriter(fileWriter);
        this.builder = builder;
    }

    public void add_to_csv(String text) throws IOException{
        builder.append(text.replace("\t", ","));

        buffWriter.append(builder.subSequence(0, builder.length()));
        buffWriter.newLine();

        builder.delete(0, builder.length());
        buffWriter.flush();
        // System.out.println("Added to CSV!");
    }

    public static void main(String[] args) throws IOException {
        String numbers = "0.0\t1.0\t2.0\t3.0\t4.0\t5.0\t6.0";


        File csvFile = new File("./src/ConstantFinder/GenerationsData.csv");
        // FileWriter fileWriter = new FileWriter(csvFile);
        StringBuilder builder = new StringBuilder();


        String text = "Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations";
        
        // builder.append(text.replace("\t", ","));

        // fileWriter.append(builder.subSequence(0, builder.length()));
        // fileWriter.append("\n");

        // builder.delete(0, builder.length());
        CsvMaker test = new CsvMaker(csvFile, builder);
        test.add_to_csv(text);
        test.add_to_csv(text);
        

        // test.finish_csv();
        // fileWriter.close();
        


    }
}
