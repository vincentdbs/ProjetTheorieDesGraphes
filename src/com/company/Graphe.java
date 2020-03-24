package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graphe {
    private String name_file;
    private int nb_arc, nb_sommet, num_file;
    private ArrayList<Transition> listTransition;
    private int[][] matriceAdjacence;
    private String[][] matriceValeur;

    public Graphe(int _num_file){
        this.listTransition = new ArrayList<Transition>(); //initialisation de la liste de transition
        this.num_file = _num_file; //numéro du fichier
        this.name_file = "TG-PRJ-A4-" + num_file + ".txt"; //nom du fichier
        readFile(); //récupération de la liste de transition

        //initialisation des matrices
        this.matriceValeur = new String[nb_sommet][nb_sommet];
        this.matriceAdjacence = new int[nb_sommet][nb_sommet];
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                matriceAdjacence[i][j] = 0;
                matriceValeur[i][j] = "X";
            }
        }
        createMatrice(); //remplissage des matrices
    }

    private void readFile(){
        try {
            Scanner scan = new Scanner(new File("Textfile/" + name_file));
            setNb_sommet(Integer.parseInt(scan.nextLine()));
            setNb_arc(Integer.parseInt(scan.nextLine()));
            while (scan.hasNextLine()){
                listTransition.add(createTransition(scan.nextLine()));
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Transition createTransition(String line){
        String[] array = line.split(" ");
        Transition transition = new Transition(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
        return transition;
    }

    private void createMatrice(){
        for (int i = 0; i < listTransition.size(); i++) {
            matriceAdjacence[listTransition.get(i).getInit()][listTransition.get(i).getFin()] = 1;
            matriceValeur[listTransition.get(i).getInit()][listTransition.get(i).getFin()] = String.valueOf(listTransition.get(i).getArc());
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
