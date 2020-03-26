package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        int choice = 0;
//        do {
//            System.out.println("Choisissez un graph");
//
//        }while (choice == 0);

        Graphe a = new Graphe(2);

        a.affichageMatrice();
        a.detectCircuit();

        a.uniqueStart();
        a.uniqueEnd();
        a.noNegative();
        System.out.print(a.sameValueOnLine());


        //todo demander si les fichiers sont forecement bien rempli
        //todo demander si graphe avec un état et une boucle sur lui même considéré comme un circuit
        //todo securisation de l'entrée du numéro de fichier
    }


}
