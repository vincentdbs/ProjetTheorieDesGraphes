package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graphe a = new Graphe(2);

        a.affichageMatrice();
        a.detectCircuit();

        a.uniqueStart();
        a.uniqueEnd();



        //todo demander si les fichiers sont forecement bien rempli
        //todo demander si graphe avec un état et une boucle sur lui même considéré comme un circuit
        //todo securisation de l'entrée du numéro de fichier
    }


}
