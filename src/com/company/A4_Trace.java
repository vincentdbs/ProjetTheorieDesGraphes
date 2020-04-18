package com.company;

import java.io.*;

public class A4_Trace {
    private String nameFile;

    public A4_Trace(int _num_file){
        nameFile = "A4-trace" + _num_file + ".txt";
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
