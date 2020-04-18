package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Graphe {
    private String name_file;
    private int nb_arc, nb_sommet, num_file;
    private ArrayList<A4_Arc> listArc;
    private int[][] matriceAdjacence;
    private String[][] matriceValeur;
    private int[] rang;
    private Trace trace;
    private boolean isGrapheOrdonnancement, isGrapheOk;
    private int start;

    public Graphe(int _num_file){
        this.isGrapheOrdonnancement = false;
        this.trace = new Trace(_num_file);
        this.listArc = new ArrayList<>(); //initialisation de la liste de transition
        this.num_file = _num_file; //numéro du fichier
        this.name_file = "A4-graphe" + num_file + ".txt"; //nom du fichier
        this.isGrapheOk = false; //le graphe est bien récuperer depuis le fichiers
        if (readFile()){
            isGrapheOk = true;
            if (nb_sommet == 1){
                this.start = 0;
            }else{
                this.start = -1;
            }

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
    }


    /**
     * Récupere ligne par ligne le nombre de sommets, d'arc et creer la liste de transition à partir du fichier
     */
    private boolean readFile(){
        try {
            Scanner scan = new Scanner(new File(name_file));
            setNb_sommet(Integer.parseInt(scan.nextLine()));
            setNb_arc(Integer.parseInt(scan.nextLine()));
            trace.write("----- Lecture du fichier n°" + getNum_file() + " -----\n" + getNb_sommet() + " sommet(s)\n" + getNb_arc() + " arc(s)\n\n");
            while (scan.hasNextLine()){
                listArc.add(createTransition(scan.nextLine())); //creation de la liste d'arc
            }
            trace.write("\n");
            scan.close();
            return true;
        } catch (FileNotFoundException e) {
            trace.write("Fichier non trouvé\n");
            return false;
        }
        catch (NumberFormatException e){
            trace.write("Erreur dans la structure du fichier - Un symbole non autorisé a été détecté\n");
            return false;
        }
        catch (NoSuchElementException e){
            trace.write("Erreur dans la structure du fichier - Une ligne est manquante\n");
            return false;
        }
    }

    /**
     * Creer une transition
     */
    private A4_Arc createTransition(String line){
        String[] array = line.split(" ");
        A4_Arc arc = new A4_Arc(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
        trace.write(array[0] + " -> " + array[1] + " = " + array[2] + "\n");
        return arc;
    }

    /**
     * Remplis les matrices d'ajacence et valeurs à partir de la liste de transition
     * Chaque transition se voit attribué un 1 dans la matrice d'ajacence et la valeur de l'arc dans la matrice des valeurs
     */
    private void fillMatrice(){
        for (int i = 0; i < listArc.size(); i++) {
            matriceAdjacence[listArc.get(i).getInit()][listArc.get(i).getFin()] = 1; //1 à la position en position (source,cible) dans la matrice
            matriceValeur[listArc.get(i).getInit()][listArc.get(i).getFin()] = String.valueOf(listArc.get(i).getArc()); //valeur de l'arc à la position en position (source,cible) dans la matrice
        }
    }

    /**
     * Affichage des matrices d'adjacence et des valeurs
     */
    public void affichageMatrice(){
        trace.write("----- Matrice d'ajacence -----\n\n");
        printMatrice(matriceAdjacence);
        trace.write("\n----- Matrice des valeurs -----\n\n");
        printMatrice(matriceValeur);
    }

    /**
     * Affichage d'une matrice 2D de type int
     */
    private void printMatrice(int[][] arrayList){
        trace.write("\t");
        for (int i = 0; i < nb_sommet ; i++) {
            trace.write(i + "\t");
        }
        trace.write("\n");
        for (int i = 0; i < nb_sommet; i++) {
            trace.write(i + " ");
            for (int j = 0; j < nb_sommet; j++) {
                trace.write("\t" + arrayList[i][j]);
            }
            trace.write("\n");
        }
    }

    /**
     * Affichage d'une matrice 2D de type String
     */
    private void printMatrice(String[][] arrayList){
        trace.write("\t");
        for (int i = 0; i < nb_sommet ; i++) {
            trace.write(i + "\t");
        }
        trace.write("\n");
        for (int i = 0; i < nb_sommet; i++) {
            trace.write(i + " ");
            for (int j = 0; j < nb_sommet; j++) {
                trace.write("\t" + arrayList[i][j]);
            }
            trace.write("\n");
        }
    }


    /**
     * Detection de la présence/absence d'un circuit dans le graphe + calcul du rang si possible
     */
    public boolean detectionCircuit(){
        int[][] matDetection = new int[nb_sommet][nb_sommet]; //matrice temporaire similaire à matrice d'adjacence
        ArrayList<Integer> done = new ArrayList<>(); //sommets traiter
        ArrayList<Integer> todo = new ArrayList<>(); //sommets à traiter
        ArrayList<Integer> todoPrec = new ArrayList<>(); //sommet à traiter à l'étape n-1

        trace.write("\n----- Detection de circuit - methode d'elimination des points d'entrée -----\n");
        for (int i = 0; i < matriceAdjacence.length; i++) {
            matDetection[i] = matriceAdjacence[i].clone();
        }
        for (int i = 0; i < nb_sommet; i++) {
            todo.add(i);
        }

        do {
            todoPrec.clear();
            todoPrec.addAll(todo);
            int tempo = 0;
            trace.write("\nPoints d'entrée : ");
            //detection des entrées = aucun predecesseur
            for (int i = 0; i < todo.size(); i++) {
                if(getNumberOfPredecesseur(todo.get(i), matDetection) == 0){
                    done.add(todo.get(i));
                    trace.write(todo.get(i) + " ");
                }
            }
            trace.write("\nSuppression des points d'entrée\n");
            //passage à -1 des entrées
            for (int i = 0; i < done.size(); i++) {
                for (int j = 0; j < nb_sommet; j++) {
                    matDetection[j][done.get(i)] = -1; //passage à -1 en colonne
                    matDetection[done.get(i)][j] = -1; //passage à -1 en ligne
                }
            }
            //suppression des sommets traités dans tab_todo
            for (int i = 0; i < done.size() ; i++) {
                todo.remove(done.get(i));
            }//affichage des sommets restants à traiter
            trace.write("Sommets restants : ");
            for (int i = 0; i < todo.size() ; i++) {
                trace.write(todo.get(i) + " ");
            }
            trace.write("\n");
        }while (!todoPrec.equals(todo));

        if (todo.size() > 0){
            trace.write("\nLe graphe contient au moins un circuit\n");
            return true;
        }
        else{
            trace.write("\nLe graphe ne contient pas de circuit\n");
            return false;
        }
    }

    /**
     * Calcul du rang par suppression des états
     */
    public void rang(){
        int[] degMoins = new int[nb_sommet];
        ArrayList<Integer> racines = new ArrayList<>();
        ArrayList<Integer> todo = new ArrayList<>();
        int rangCourant = 0;
        int[][] matDetection = new int[nb_sommet][nb_sommet];

        trace.write("\n----- Calcul du rang -----\n\n");
        //copie de la matrice
        for (int i = 0; i < matriceAdjacence.length; i++) {
            matDetection[i] = matriceAdjacence[i].clone();
        }

        //initialisation = calcul des d°- des sommets + determination des racines
        for (int i = 0; i < nb_sommet; i++) {
            todo.add(i);
            degMoins[i] = getNumberOfPredecesseur(i, matDetection);
            if (degMoins[i] == 0){
                racines.add(i);
            }
        }

        do{
            trace.write("Rang courant :\t" + rangCourant + "\n");
            //on attribue le rang courant aux racines
            trace.write("Racine(s) :\t");
            for (int i = 0; i <  racines.size() ; i++) {
                rang[racines.get(i)] = rangCourant;
                trace.write("\t" + racines.get(i));
            }
            trace.write("\n\n");
            //suppression des racines => passage à -1 dans la matrice
            for (int i = 0; i < racines.size(); i++) {
                for (int j = 0; j < nb_sommet; j++) {
                    matDetection[j][racines.get(i)] = -1; //passage à -1 en colonne
                    matDetection[racines.get(i)][j] = -1; //passage à -1 en ligne
                }
            }
            //suppression des racines de liste.tod'o
            for (int i = 0; i < racines.size(); i++) {
                todo.remove(racines.get(i));
            }
            //calcul des deg°- + ajout des racines
            racines.clear();
            for (int i = 0; i < todo.size(); i++) {
                int index = todo.get(i);
                degMoins[index] = getNumberOfPredecesseur(index, matDetection);
                if (degMoins[index] == 0){
                    racines.add(index);
                }
            }
            rangCourant++;
        }while (!todo.isEmpty());

        trace.write("Sommets : ");
        for (int i = 0; i <  rang.length; i++) {
            trace.write("\t" + i);
        }
        trace.write("\nRang : \t");
        for (int i = 0; i <  rang.length; i++) {
            trace.write("\t" + rang[i]);
        }
        trace.write("\n");
    }

    /**
     * Determine le nombre de predecesseur d'un sommet à partir de la matrice d'adjacence
     */
    private int getNumberOfPredecesseur(int sommet, int[][] mat){
        int num = 0;
        for (int i = 0; i < nb_sommet; i++) {
            if (mat[i][sommet] == 1){
                num++;
            }
        }
        return num;
    }

    /**
     * Determine le nombre de successeur d'un sommet à partir de la matrice d'adjacence
     */
    private int getNumberOfSuccesseur(int sommet, int[][] mat){
        int num = 0;
        for (int i = 0; i < nb_sommet; i++) {
            if (mat[sommet][i] == 1){
                num++;
            }
        }
        return num;
    }

    /**
     * Processus de contrôle pour savoir si le graphe est un graphe d'ordonnancement
     */
    public boolean isGrapheOrdonnancement(){
        isGrapheOrdonnancement = true;
        trace.write("\n----- Le graphe est-il un graphe d'ordonancement ? -----\n\n");
        trace.write("Un seul point d'entrée :\t\t" + uniqueStart() + "\n");
        trace.write("Un seul point de sortie :\t\t" + uniqueEnd() + "\n");
        boolean plsSortie = false;
        if(!isGrapheOrdonnancement){
            plsSortie = true;
        }
        trace.write("Pas de circuit :\t\t\t\ttrue\n");
        trace.write("Pas d’arcs à valeur négative :\t" + noNegative() + "\n");
        trace.write("Valeurs identiques pour tous les arcs incidents vers l’extérieur à un sommet :\t" + sameValueOnLine() + "\n");
        trace.write("Arcs incidents vers l’extérieur au point d’entrée de valeur nulle :\t\t\t\t");
        if (plsSortie){
            trace.write("non testé car il y a plusieurs sorties\n");
        }else{
            trace.write(valueZeroOnStart()+ "\n");
        }

        if(!isGrapheOrdonnancement){
            trace.write("Le graphe n'est pas un graphe d'ordonnancement !\n\n");
        }
        else{
            trace.write("Le graphe est un graphe d'ordonnancement !\n\n");
        }
        return isGrapheOrdonnancement;
    }

    /**
     * Determine si le graphe possède une unique entrée
     */
    private boolean uniqueStart(){
        int nbStart = 0;
        for (int i = 0; i < nb_sommet; i++) {
            if (getNumberOfPredecesseur(i, matriceAdjacence) == 0){ //si aucun prédecesseur pour le sommet i => nb entrée++
                start = i;
                nbStart++;
            }
        }
        if (nbStart == 1){
            return true;
        }
        else{
            isGrapheOrdonnancement = false;
            return false;
        }
    }

    /**
     * Determine si le graphe possède une unique sortie
     */
    private boolean uniqueEnd(){
        int nbEnd = 0;
        for (int i = 0; i < nb_sommet; i++) {
            if (getNumberOfSuccesseur(i, matriceAdjacence) == 0){ //si aucun successeur pour le sommet i => nb entrée++
                nbEnd++;
            }
        }
        if (nbEnd == 1){
            return true;
        }
        else{
            isGrapheOrdonnancement = false;
            return false;
        }
    }

    /**
     * Controle de la presence d'arc de valeur positif uniquement
     */
    private boolean noNegative(){
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                try {
                    if(Integer.parseInt(matriceValeur[i][j]) < 0 ){ //si la valeur de l'arc est négative
                        isGrapheOrdonnancement = false;
                        return false;
                    }
                }
                catch (NumberFormatException e){}
            }
        }
        return true;
    }

    /**
     * Controle de la presence de la même valeur des arcs sortants d'un sommet
     */
    private boolean sameValueOnLine(){
        ArrayList<String> value = new ArrayList<>();
        for (int i = 0; i < nb_sommet; i++) {
            for (int j = 0; j < nb_sommet; j++) {
                if (!(value.contains(matriceValeur[i][j]))){ //si la valeur de l'arc d'un sommet n'a pas déjà ajouter à la liste => ajout
                    value.add(matriceValeur[i][j]);
                }
            }
            if (value.size() > 2){ //si il y a plus de 2 valeurs (* et la valeur de l'arc) sur une ligne => faux
                isGrapheOrdonnancement = false;
                return false;
            }
            value.clear(); //reset de la liste des valeurs pour le sommet suivant
        }
        return true;
    }

    /**
     * Controle de la presence de la valeur 0 sur les arcs sortants du sommet d'entrée
     */
    private boolean valueZeroOnStart(){
        for (int i = 0; i < nb_sommet; i++) {
            if (!((matriceValeur[start][i].equals("*")) || (matriceValeur[start][i].equals("0")))){
                isGrapheOrdonnancement = false;
                return false;
            }
        }
        return true;
    }


    /**
     * Processus d'odonnancement
     */
    public void ordonnancement(){
        trace.write("----- Ordonnancement -----\n\n");
        int[][] tabOrdonnancement = new int[8][nb_sommet]; //calendrier total
            //i = 0 => sommets | i = 1 => longueur tache du sommet | i = 2 => prédecesseur ayant la plus grande date | i = 3 =>  date au plus tôt
            //i = 4 => successeur ayant la plus petite date | i = 5 => date au plus tard | i = 6 => marge totale | i = 7 => marge libre
        //initialisation
        for (int i = 0; i < 8 ; i++) {
            for (int j = 0; j < nb_sommet ; j++) {
                tabOrdonnancement[i][j] = -1;
            }
        }
        //classement des sommets par ordre croissant de rang
        tabOrdonnancement[0] = orderSommetsbyRang();
        //recupération des tâche de chaque sommet
        tabOrdonnancement[1] = retrieveSommetValue(tabOrdonnancement[0]);
        //Calendrier des dates au plus tôt
        int[][] result = retrieveBestPredecesseur(tabOrdonnancement[0]);
        tabOrdonnancement[2] = result[0]; //meilleur successeur
        tabOrdonnancement[3] = result[1]; //date au plus tôt
        //Calendrier date au plus tard
        result = retrieveBestSuccesseur(tabOrdonnancement[0], tabOrdonnancement[3][nb_sommet-1]);
        tabOrdonnancement[4] = result[0]; //meilleur successeur
        tabOrdonnancement[5] = result[1]; //date au plus tard
        //margeTotale
        tabOrdonnancement[6] = margeTotale(tabOrdonnancement[3], tabOrdonnancement[5]);
        //marge libre
        tabOrdonnancement[7] = margeLibre(tabOrdonnancement[0], tabOrdonnancement[3], tabOrdonnancement[1]);
        //affichage du tableau d'ordonnancement
        printOrdonnancement(tabOrdonnancement);
    }

    /**
     * Affichage du calendrier d'un graphe
     */
    private void printOrdonnancement(int[][] arrayOrdonnancement) {
        trace.write("\t----- Calendrier -----\n");
        for (int i = 0; i < arrayOrdonnancement.length; i++) {
            switch (i){
                case 0:
                    trace.write("Sommet :\t\t\t");
                    break;
                case 1:
                    trace.write("\nTache :\t\t\t\t");
                    break;
                case 2:
                    trace.write("\nPrédecesseur :\t\t");
                    break;
                case 3:
                    trace.write("\nDate au + tôt :\t\t");
                    break;
                case 4:
                    trace.write("\nSuccesseur :\t\t");
                    break;
                case 5:
                    trace.write("\nDate au + tard :\t");
                    break;
                case 6:
                    trace.write("\nMarge totale :\t\t");
                    break;
                case 7:
                    trace.write("\nMarge libre : \t\t");
                default:
                    break;
            }
            for (int j = 0; j < arrayOrdonnancement[i].length; j++) {
                if (arrayOrdonnancement[i][j] == -1){
                    trace.write("*\t");
                }else{
                    trace.write(arrayOrdonnancement[i][j] + "\t");
                }
            }
        }
    }

    /**
     * Ordonne les sommets par rang croissant
     * @return tableau des sommets classé par rang
     */
    private int[] orderSommetsbyRang(){
        int[] ordered = new int[nb_sommet];
        int index = 0;
        int rg = 0;
        do{
            for (int i = 0; i < rang.length; i++) {
                if(rang[i] == rg){ //si le rang du sommet i = rang courant
                    ordered[index] = i; //ajout du sommet i au tableau
                    index++;
                }
            }
            rg++; //rang suivant
        }while (rg<nb_sommet);
        return ordered;
    }

    /**
     * Récupere pour chaque sommet sa longueur
     * @param sommet, le tableau de sommet classé par ordre croissant de rang
     * @return la longeur de la tache de chaque sommet classé par rang
     */
    private int[] retrieveSommetValue(int[] sommet){
        int[] tache = new int[nb_sommet];
        for (int i = 0; i < nb_sommet; i++) {
            tache[i] = sommetValue(sommet[i]);
        }
        return tache;
    }

    /**
     * Recupere la tache du sommet i
     * @param i le sommet i à analyse
     * @return la longeur de la tache du sommet i
     */
    private int sommetValue(int i){
        int tache = -1;
        for (int j = 0; j < nb_sommet ; j++) {
            try {
                tache = Integer.parseInt(matriceValeur[i][j]);
            }catch (NumberFormatException e){}

        }
        return tache;
    }

    /**
     * Caclul du calendrier de date au plus tôt
     * @param sommetOrdonne, le tableau de sommet ordonné par rang croissant
     * @return tableau 2D des predecesseur et des dates au plus tôt ordonné par rang
     */
    private int[][] retrieveBestPredecesseur(int[] sommetOrdonne){
        int[][] array = new int[2][nb_sommet]; //tableau du meilleur predecesseur(i = 0) et sa date au plus tot(i=1)
        ArrayList<Integer> tempoPred = new ArrayList<Integer>(); //liste temporaire des prédecesseur d'un sommet
        ArrayList<Integer> tempoTache = new ArrayList<Integer>(); //liste temporaire de la durée du prédecesseur d'un sommet
        ArrayList<Integer> tempoDatePlusTot = new ArrayList<Integer>(); //liste temporaire de la date au plus tôt en fonction de chaque predecesseur

        array[0][0] = -1;
        array[1][0] = 0;
        trace.write("\t ----- Date au plus tôt -----\n\n");
        for (int i = 1; i < nb_sommet; i++) {
            trace.write("Prédecesseur de " + sommetOrdonne[i] + " : \t");
            //récupération des predecesseur du sommet à la position sommetOrdonne[i]
            for (int j = 0; j < nb_sommet ; j++) {
                if (matriceAdjacence[j][sommetOrdonne[i]] == 1){
                    tempoPred.add(j); //ajout du predecesseur
                    trace.write(j + "\t");
                }
            }
            trace.write("\n");

            //recupération de la tache de chaque prédecesseur récupéré
            for (int j = 0; j < tempoPred.size(); j++) {
                tempoTache.add(sommetValue(tempoPred.get(j))); //recuperation de la tâche
            }

            trace.write("Date au plus tôt : \t\t");
            //pour chaque predecesseur => calcul de la date au plus tôt
            for (int j = 0; j < tempoTache.size() ; j++) {
                int sommetTache = tempoPred.get(j); //recuperation du sommet correspondant
                int index = 0;
                for (int k = 0; k < sommetOrdonne.length ; k++) {
                    if (sommetOrdonne[k] == sommetTache){
                        index = k; //recuperation de l'index du sommet dans le tableau des sommets ordonnés
                    }
                }
                trace.write(array[1][index] + tempoTache.get(j) + "\t");
                tempoDatePlusTot.add(array[1][index] + tempoTache.get(j));
            }
            trace.write("\n");

            //calcul de la date au plus tôt optimal
            array[1][i] = Collections.max(tempoDatePlusTot);
            trace.write("Date au plus tôt max : \t" + array[1][i] + "\n\n");
            array[0][i] = tempoPred.get(tempoDatePlusTot.indexOf(array[1][i]));

            tempoDatePlusTot.clear();
            tempoPred.clear();
            tempoTache.clear();
        }
        return array;
    }

    /**
     * Calcul du calendrier des dates au plus tard
     * @param sommetOrdonne, le tableau de sommet ordonné par rang croissant
     * @param datePlusTot, la date au plus tôt du sommet max
     */
    private int[][] retrieveBestSuccesseur(int[] sommetOrdonne, int datePlusTot){
        int[][] array = new int[2][nb_sommet];
        ArrayList<Integer> tempoSucc = new ArrayList<Integer>(); //liste temporaire des succésseurs d'un sommet
        ArrayList<Integer> tempoDatePlusTard = new ArrayList<Integer>(); //liste temporaire de la date au plus tard en fonction de chaque successeur

        array[0][nb_sommet-1] = -1; //la sortie n'a pas de successeur
        array[1][nb_sommet-1] = datePlusTot; //la date au plus tard = la date au plus tôt

        trace.write("\t ----- Date au plus tard -----\n\n");
        for (int i = nb_sommet-2 ; i >= 0; i--) {
            trace.write("Successeur de " + sommetOrdonne[i] + " : \t\t");
            //récupération des successeur du sommet à la position sommetOrdonne[i]
            for (int j = 0; j < nb_sommet ; j++) { //récupération des successeurs pour le sommet à l'index i
                if (matriceAdjacence[sommetOrdonne[i]][j] == 1){
                    tempoSucc.add(j); //ajout du successeur
                    trace.write(j + "\t");
                }
            }
            trace.write("\n");
            int tache = sommetValue(sommetOrdonne[i]); //longueur de la tache du somme à l'index i

            //calcul de la date au plus tard de chaque successeur
            trace.write("Date au plus tard : \t");
            for (int j = 0; j < tempoSucc.size() ; j++) {
                int sommetTache = tempoSucc.get(j); //recuperation du sommet correspondant
                int index = 0;
                for (int k = 0; k < sommetOrdonne.length ; k++) {
                    if (sommetOrdonne[k] == sommetTache){
                        index = k; //recuperation de l'index du sommet dans le tableau des sommets ordonnés
                    }
                }
                trace.write(array[1][index] - tache + "\t");
                tempoDatePlusTard.add(array[1][index] - tache);
            }
            trace.write("\n");

            //date au plus tard optimale
            array[1][i] = Collections.min(tempoDatePlusTard);
            trace.write("Date au plus tard max : " + array[1][i] + "\n\n");
            array[0][i] = tempoSucc.get(tempoDatePlusTard.indexOf(array[1][i]));

            tempoDatePlusTard.clear();
            tempoSucc.clear();
        }
        return array;
    }

    /**
     * Calcule des marges totales
     */
    private int[] margeTotale(int[] datePlusTot, int[] datePlusTard){
        int[] margeTotale = new int[nb_sommet];
        for (int i = 0; i < nb_sommet; i++) {
            margeTotale[i] = datePlusTard[i] - datePlusTot[i];
        }
        return margeTotale;
    }

    private int[] margeLibre(int[] sommetOrdonne, int[] datePlusTot, int[]tache){
        int[] margeLibre = new int[nb_sommet];
        ArrayList<Integer> tempoSucc = new ArrayList<Integer>();
        ArrayList<Integer> tempoSuccIndex = new ArrayList<Integer>();

        ArrayList<Integer> tempoMarge = new ArrayList<Integer>();

        trace.write("\t ----- Marges libres -----\n");
        for (int i = 0; i < nb_sommet-1; i++) {
            tempoMarge.clear();
            tempoSucc.clear();
            tempoSuccIndex.clear();
            trace.write("\nSuccesseur(s) du sommet " + sommetOrdonne[i] + " :");
            for (int j = 0; j < nb_sommet ; j++) { //récupération des successeurs pour le sommet à l'index i
                if (matriceAdjacence[sommetOrdonne[i]][j] == 1){
                    tempoSucc.add(j); //ajout de l'index
                    trace.write("\t" + j);
                }
            }
            for (int j = 0; j < tempoSucc.size(); j++) {
                for (int k = 0; k < sommetOrdonne.length ; k++) {
                    if(sommetOrdonne[k] == tempoSucc.get(j)){
                        tempoSuccIndex.add(k);
                    }
                }
            }
            trace.write("\nDate au plus tôt : \t\t");
            for (int j = 0; j < tempoSuccIndex.size() ; j++) {
                tempoMarge.add(datePlusTot[tempoSuccIndex.get(j)]);
                trace.write("\t" + datePlusTot[tempoSuccIndex.get(j)]);
            }
            int marge = Collections.min(tempoMarge);
            trace.write("\nDate au plus tôt minimale : " + marge);
            trace.write("\nDate au plus tôt de " + sommetOrdonne[i] + " :\t\t" + tache[i]);
            trace.write("\nTâche du sommet : \t\t\t" + datePlusTot[i]);
            marge -= (datePlusTot[i] + tache[i]);
            margeLibre[i] = marge;
            trace.write("\nMarge libre : \t\t\t\t" + marge + "\n");
        }
        trace.write("\n");
        return margeLibre;
    }

    /*Getter setter*/
    public int getNum_file() {
        return num_file;
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

    public boolean getisGraphOk(){
        return isGrapheOk;
    }


}
