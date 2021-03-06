package com.company;

import java.io.File;
import java.io.FileFilter;
import java.util.Scanner;

public class L3_A4_Main {

    public static void main(String[] args) {
        //choix du graphe
        //affichage des matrices
        //detection du circuit
            //calcul du rang
            //test graphe d'ordonnancement
                //calcul des calendrier
        do {
            int numGraphe = choiceNumGraphe();
            L3_A4_Graphe graphe = new L3_A4_Graphe(numGraphe);
            if(choixAlgo().equals("f")){
                graphe.floyd();
            }else{
                if (graphe.getisGraphOk()){
                    graphe.affichageMatrice();
                    if (!graphe.detectionCircuit()){
                        graphe.rang();
                        if (graphe.isGrapheOrdonnancement()){
                            graphe.ordonnancement();
                        }
                    }
                }
            }
        }while (newGraphe());


    }

    /**
     * Choix utilisateur du graphe à traiter
     */
    private static int choiceNumGraphe(){
        String grapheChoice = "0";
        int nbGraphe = countNbFile(); //récuperation du nombre de graphe dans le projet
        int again = 0;
        do{
            System.out.print("Choisissez un numéro de graphe entre 1 et " + nbGraphe + " : ");
            Scanner scanner = new Scanner(System.in);
            grapheChoice = scanner.nextLine();
            try {
                if (!((Integer.parseInt(grapheChoice) <= nbGraphe) && (Integer.parseInt(grapheChoice) > 0))){
                    again = 0;
                }
                else{
                    again = 1;
                }
            }
            catch (NumberFormatException e){
                again = 0; //si la saisie n'est pas le bon nombre => recommencer
            }
        }while (again == 0);
        return Integer.parseInt(grapheChoice);
    }

    /**
     * Choix utilisateur de tester un nouveau graphe
     */
    private static boolean newGraphe(){
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        do{
            System.out.print("\n\nVoulez-vous tester un autre graphe ? [oui : o/non : n] : ");
            choice = scanner.nextLine();
        }while (!((choice.equals("o")) || (choice.equals("n"))));

        System.out.print("\n");
        if (choice.equals("o")){
            return true;
        }
        else {
            return false;
        }
    }

    private static int countNbFile(){
        File dir = new File(".");
        FileFilter grapheFileFilter = new FileFilter() {
            public boolean accept(File file) {
                //si le fichier est un graphe de test
                if (file.getName().startsWith("L3-A4-graphe")) {
                    return true;
                }
                return false;
            }
        };
        File[] contentDir = dir.listFiles(grapheFileFilter); //recupération des fichiers qui commencent par le patern A4-graphe
        int nbFile = contentDir.length;
        if(nbFile == 0){
            return 13;
        }else{
            return nbFile;
        }
    }

    private static String choixAlgo(){
        String choix= "";
        int again = 0;
        do{
            System.out.print("Choisissez entre ordonnancement[o] et floyd[f] : ");
            Scanner scanner = new Scanner(System.in);
            choix = scanner.nextLine();
            if ((choix.equals("f")) || (choix.equals("o"))){
                again = 1;
            }
            else{
                again = 0;
            }
        }while (again == 0);
        return choix;
    }

}
