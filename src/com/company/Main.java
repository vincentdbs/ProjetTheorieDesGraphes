package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graphe a = new Graphe(1);
        a.detectCircuit();
        a.affichageMatrice();

        //todo demander si les fichiers sont forecement bien rempli
    }


}
