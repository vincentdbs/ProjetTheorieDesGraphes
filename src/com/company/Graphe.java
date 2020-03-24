package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graphe {
    int num_file;
    String name_file;
    int nb_arc;
    int nb_sommet;

    public Graphe(int _num_file){
        this.num_file = _num_file; //numéro du fichier
        this.name_file = "TG-PRJ-A4-" + num_file + ".txt";
        readFile();
    }

    private void readFile(){
        try {
            Scanner scan = new Scanner(new File("Textfile/" + name_file));
            setNb_sommet(Integer.parseInt(scan.nextLine()));
            setNb_arc(Integer.parseInt(scan.nextLine()));
            System.out.println(getNb_sommet() + " " + getNb_arc());
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getNum_file() {
        return num_file;
    }

    public void setNum_file(int num_file) {
        this.num_file = num_file;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public int getNb_arc() {
        return nb_arc;
    }

    public void setNb_arc(int nb_arc) {
        this.nb_arc = nb_arc;
    }

    public int getNb_sommet() {
        return nb_sommet;
    }

    public void setNb_sommet(int nb_sommet) {
        this.nb_sommet = nb_sommet;
    }


}
