package com.company;

import java.io.*;

public class Trace {
    private String nameFile;

    public Trace(int _num_file){
        nameFile = "Textfile/Log/Log-TG-PRJ-A4-" + _num_file + ".txt";
        PrintWriter writer = null;
        try { //supprime le contenu du fichier
            writer = new PrintWriter(nameFile);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
