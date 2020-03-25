package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Trace {
    private BufferedWriter writer;

    public Trace(int _num_file){
        String nameFile = "Log-TG-PRJ-A4-" + _num_file + ".txt";
        try {
            this.writer = new BufferedWriter(new FileWriter(nameFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String str){
        try {
            writer.append(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str);
    }
}
