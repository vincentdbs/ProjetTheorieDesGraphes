package com.company;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //todo graphe 12 => arc point d'entrée de valeur nul faux alors que vrai

        //choix du graphe
        //affichage des matrices
        //detection du circuit
            //calcul du rang
            //test graphe d'ordonnancement
                //calcul des calendrier
        do {
            int numGraphe = choiceNumGraphe();
            Graphe graphe = new Graphe(numGraphe);
            if (graphe.getisGraphOk()){
                graphe.affichageMatrice();
                if (!graphe.detectionCircuit()){
                    graphe.rang();
                    if (graphe.isGrapheOrdonnancement()){
                        graphe.ordonnancement();
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
        int nbGraphe = new File("Textfile/Graphe").listFiles().length;
        int again = 0;
        do{
            System.out.print("Choisissez un numéro de graphe entre 1 et " + nbGraphe + " : ");
            Scanner scanner = new Scanner(System.in);
            grapheChoice = scanner.nextLine();
            try {
                if (!((Integer.parseInt(grapheChoice) <= nbGraphe ) && (Integer.parseInt(grapheChoice) > 0))){
                    again = 0;
                }
                else{
                    again = 1;
                }
            }
            catch (NumberFormatException e){
                again = 0;
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
            System.out.print("\n\nVoulez-vous tester un autre graphe ? [o/n] : ");
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



}
