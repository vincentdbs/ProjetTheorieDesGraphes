package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Graphe {
    private String name_file;
    private int nb_arc, nb_sommet, num_file;
    private ArrayList<Transition> listTransition;
    private int[][] matriceAdjacence;
    private String[][] matriceValeur;
    private int[] rang;
    private Trace trace;
    private boolean isGrapheOrdonnancement;

    public Graphe(int _num_file){
        this.isGrapheOrdonnancement = false;
        this.trace = new Trace(_num_file);
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

    }

    public void affichageMatrice(){
        trace.write("----- Matrice d'ajacence -----\n");
        printMatrice(matriceAdjacence);
        trace.write("----- Matrice des valeurs -----\n");
        printMatrice(matriceValeur);
    }

    /**
     * Lit et creer la liste de transition à partir du fichier
     */
    private void readFile(){
        try {
            Scanner scan = new Scanner(new File("Textfile/Graphe/" + name_file));
            setNb_sommet(Integer.parseInt(scan.nextLine()));
            setNb_arc(Integer.parseInt(scan.nextLine()));
            trace.write("----- Lecture du fichier n°" + getNum_file() + " -----\n" + getNb_sommet() + " sommet(s)\n" + getNb_arc() + " arc(s)\n");
            while (scan.hasNextLine()){
                listTransition.add(createTransition(scan.nextLine()));
            }
            trace.write("\n");
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
        trace.write(array[0] + " -> " + array[1] + " = " + array[2] + "\n");
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

    /**
     * Detection de la présence/absence d'un circuit dans le graphe + calcul du rang si possible
     */
    //todo changer pour que rang se calcule après circuit
    public boolean detectCircuit() {
        //todo corriger pour fichier 8
        //initialisation
        int[][] matDetection = new int[nb_sommet][nb_sommet];

        for (int i = 0; i < matriceAdjacence.length; i++) {
            matDetection[i] = matriceAdjacence[i].clone();
        }

        ArrayList<Integer> done = new ArrayList<Integer>();
        ArrayList<Integer> todo = new ArrayList<Integer>();
        for (int i = 0; i < nb_sommet; i++) {
            todo.add(i);
        }

        trace.write("\n----- Detection de circuit - methode d'elimination des points d'entrée -----\n");
        //rang
        int rg = 0;
        do{
            //un tour
            int tempo = 0;
            //detection predecesseur ou non
            trace.write("\nPoints d'entrée : ");
            for (int i = 0; i < todo.size(); i++) {
                for (int j = 0; j < nb_sommet; j++) {
                    if (matDetection[j][todo.get(i)] == 1) { // [j][i] car parcourt en colonne
                        tempo++;
                    }
                }
                if (tempo == 0) {
                    done.add(todo.get(i));
                    rang[todo.get(i)] = rg;
                    trace.write(String.valueOf(todo.get(i)) + " ");
                }
                tempo = 0;
            }
            trace.write("\nSuppression des points d'entrée\n");
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
            //affichage des sommets restants
            trace.write("Sommets restants : ");
            for (int i = 0; i < todo.size() ; i++) {
                trace.write(String.valueOf(todo.get(i)) + " ");
            }
            trace.write("\n");
            rg++;
        }while ((rg < nb_sommet) && (!todo.isEmpty()));

        if (Arrays.binarySearch(rang, -1) > 0){
            trace.write("\nLe graphe contient au moins un circuit\n");
            return true;
        }
        else{
            trace.write("\nLe graphe ne contient pas de circuit\n");
            trace.write("Sommets : ");
            for (int i = 0; i <  rang.length; i++) {
                trace.write("\t" + i);
            }
            trace.write("\nRang : \t");
            for (int i = 0; i <  rang.length; i++) {
                trace.write("\t" + rang[i]);
            }
            trace.write("\n");
            return false;
        }
    }

    public boolean isGrapheOrdonnancement(){
        isGrapheOrdonnancement = true;
        trace.write("\n----- Le graphe est-il un graphe d'ordonancement ? -----\n");
        trace.write("Un seul point d'entrée :\t\t" + uniqueStart() + "\n");
        trace.write("Un seul point de sortie :\t\t" + uniqueEnd() + "\n");
        trace.write("Pas de circuit :\t\t\t\ttrue\n");
        trace.write("Pas d’arcs à valeur négative :\t" + noNegative() + "\n");
        trace.write("Valeurs identiques pour tous les arcs incidents vers l’extérieur à un sommet :\t" + sameValueOnLine() + "\n");
        trace.write("Arcs incidents vers l’extérieur au point d’entrée de valeur nulle :\t\t\t\t" + "true"+ "\n"); //todo créer la fonction après réponse de Kassel + isGrapheOrdonnancement = false si faux

        if(isGrapheOrdonnancement == false){
            trace.write("Le graphe n'est pas un graphe d'ordonnancement !\n");
        }
        else{
            trace.write("Le graphe est un graphe d'ordonnancement !\n");
        }

        return isGrapheOrdonnancement;
    }

    private boolean uniqueStart(){
        int nbStart = 0;
        int tempo = 0;
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                if (matriceAdjacence[j][i] == 1){
                    tempo++;
                }
            }
            if (tempo == 0){
                nbStart++;
            }
            tempo = 0;
        }
        if (nbStart == 1){
            return true;
        }
        else{
            isGrapheOrdonnancement = false;
            return false;
        }
    }

    private boolean uniqueEnd(){
        int nbStart = 0;
        int tempo = 0;
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                if (matriceAdjacence[i][j] == 1){
                    tempo++;
                }
            }
            if (tempo == 0){
                nbStart++;
            }
            tempo = 0;
        }
        if (nbStart == 1){
            return true;
        }
        else{
            isGrapheOrdonnancement = false;
            return false;
        }
    }

    private boolean noNegative(){
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                try {
                    if(Integer.parseInt(matriceValeur[i][j]) < 0 ){
                        isGrapheOrdonnancement = false;
                        return false;
                    }
                }
                catch (NumberFormatException e){}
            }
        }
        return true;
    }

    private boolean sameValueOnLine(){
        ArrayList<String> value = new ArrayList<String>();
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                if (!(value.contains(matriceValeur[i][j]))){
                    value.add(matriceValeur[i][j]);
                }
            }
            if (value.size() > 2){
                isGrapheOrdonnancement = false;
                return false;
            }
            value.clear();
        }
        return true;
    }

    //Affichage matrice
    private void printMatrice(int[][] arrayList){
        trace.write("\t");
        for (int i = 0; i < nb_sommet ; i++) {
            trace.write(i + "\t");
        }
        trace.write("\n");
        for (int i = 0; i < nb_sommet; i++) {
            trace.write(i + " ");
            for (int j = 0; j < nb_sommet; j++) {
                trace.write(String.valueOf("\t" + arrayList[i][j]));
            }
            trace.write("\n");
        }
    }

    private void printMatrice(String[][] arrayList){
        trace.write("\t");
        for (int i = 0; i < nb_sommet ; i++) {
            trace.write(i + "\t");
        }
        trace.write("\n");
        for (int i = 0; i < nb_sommet; i++) {
            trace.write(i + " ");
            for (int j = 0; j < nb_sommet; j++) {
                trace.write(String.valueOf("\t" + arrayList[i][j]));
            }
            trace.write("\n");
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

    public int[][] getMatriceAdjacence() {
        return matriceAdjacence;
    }

}
