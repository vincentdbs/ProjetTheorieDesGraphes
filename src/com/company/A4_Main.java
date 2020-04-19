package com.company;

import java.io.File;
import java.io.FileFilter;
import java.util.Scanner;

public class A4_Main {

    public static void main(String[] args) {
        //choix du graphe
        //affichage des matrices
        //detection du circuit
            //calcul du rang
            //test graphe d'ordonnancement
                //calcul des calendrier
//        do {
//            int numGraphe = choiceNumGraphe();
//            A4_Graphe graphe = new A4_Graphe(numGraphe);
//            if (graphe.getisGraphOk()){
//                graphe.affichageMatrice();
//                if (!graphe.detectionCircuit()){
//                    graphe.rang();
//                    if (graphe.isGrapheOrdonnancement()){
//                        graphe.ordonnancement();
//                    }
//                }
//            }
//        }while (newGraphe());

        for (int i = 1; i <= 26; i++) {
            A4_Graphe graphe = new A4_Graphe(i);
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
                if (file.getName().startsWith("A4-graphe")) {
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


}
