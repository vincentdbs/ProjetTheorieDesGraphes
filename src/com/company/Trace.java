package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Trace {
    private String nameFile;

    public Trace(int _num_file){
        nameFile = "Textfile/Log/Log-TG-PRJ-A4-" + _num_file + ".txt";
    }

    public void write(String str){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile, true));
            writer.append(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(str);
    }
}
