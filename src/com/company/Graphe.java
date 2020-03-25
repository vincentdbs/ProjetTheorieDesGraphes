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
    private int[] rang;

    public Graphe(int _num_file){
        this.listTransition = new ArrayList<Transition>(); //initialisation de la liste de transition
        this.num_file = _num_file; //numéro du fichier
        this.name_file = "TG-PRJ-A4-" + num_file + ".txt"; //nom du fichier
        readFile(); //récupération de la liste de transition

        //initialisation des matrices
        this.matriceValeur = new String[nb_sommet][nb_sommet];
        this.matriceAdjacence = new int[nb_sommet][nb_sommet];
        this.rang = new int[nb_sommet];
        for (int i = 0; i < nb_sommet; i++) {
            rang[i] = -1;
            for (int j = 0; j < nb_sommet; j++) {
                matriceAdjacence[i][j] = 0;
                matriceValeur[i][j] = "*";
            }
        }
        fillMatrice(); //remplissage des matrices

        detectCircuit();
    }

    /**
     * Lit et creer la liste de transition à partir du fichier
     */
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

    /**
     * creer une transition
     */
    private Transition createTransition(String line){
        String[] array = line.split(" ");
        Transition transition = new Transition(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
        return transition;
    }

    /**
     * remplis les matrices d'ajacence et valeurs à partir de la liste de transition
     */
    private void fillMatrice(){
        for (int i = 0; i < listTransition.size(); i++) {
            matriceAdjacence[listTransition.get(i).getInit()][listTransition.get(i).getFin()] = 1;
            matriceValeur[listTransition.get(i).getInit()][listTransition.get(i).getFin()] = String.valueOf(listTransition.get(i).getArc());
        }
    }

    private void detectCircuit() {
        //initialisation
        int[][] matDetection = matriceAdjacence;
        ArrayList<Integer> done = new ArrayList<Integer>();
        ArrayList<Integer> todo = new ArrayList<Integer>();
        for (int i = 0; i < nb_sommet; i++) {
            todo.add(i);
        }

        //rang
        int rg = 0;
        do{
            //un tour
            int tempo = 0;
            //detection predecesseur ou non
            for (int i = 0; i < todo.size(); i++) {
                for (int j = 0; j < nb_sommet; j++) {
                    if (matDetection[j][todo.get(i)] == 1) { // [j][i] car parcourt en colonne
                        tempo++;
                    }
                }
                if (tempo == 0) {
                    done.add(todo.get(i));
                    rang[todo.get(i)] = rg;
                }
                tempo = 0;
            }
            //passage à -1 des elements
            for (int i = 0; i < done.size(); i++) {
                for (int j = 0; j < nb_sommet; j++) {
                    matDetection[j][done.get(i)] = -1; //passage à -1 en colonne
                    matDetection[done.get(i)][j] = -1; //passage à -1 en ligne
                }
            }
            //suppression dans tab_todo
            for (int i = 0; i < done.size() ; i++) {
                todo.remove(done.get(i));
            }


            System.out.println("k= " + rg + "\n" + todo);
            System.out.println(done);
            rg++;
        }while ((rg < nb_sommet) && (!todo.isEmpty()));


        printMatrice(matDetection);
    }

    private void printMatrice(int[][] arrayList){
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                System.out.print(arrayList[i][j] + " ");
            }
            System.out.println("");
        }
    }
    /*Getter setter*/

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
