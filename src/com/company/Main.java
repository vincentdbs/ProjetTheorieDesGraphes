package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String again = "1";
        do {
            int numGraphe = choiceNumGraphe();
            Graphe a = new Graphe(numGraphe);
            a.affichageMatrice();
            a.detectCircuit();

            a.uniqueStart();
            a.uniqueEnd();
            a.noNegative();
            System.out.print(a.sameValueOnLine());


        }while (again == "0");





        //todo demander si les fichiers sont forecement bien rempli
        //todo demander si graphe avec un état et une boucle sur lui même considéré comme un circuit
        //todo securisation de l'entrée du numéro de fichier
    }

    private static int choiceNumGraphe(){
        //todo check si ok
        String grapheChoice = "0";
        int nbGraphe = new File("Textfile/Graphe").listFiles().length;
        int again = 0;
        do{
            System.out.println("Choisissez un numéro de graphe entre 1 et " + nbGraphe + " : ");
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



}
