package com.company;

public class Transition {
    private int init, arc, fin;
    //TODO refactor nom de la classe en Arc

    public Transition(int i_init, int i_fin, int i_arc){
        init = i_init;
        arc = i_arc;
        fin = i_fin;
    }

    public void print(){
        System.out.print(init + " " + arc  + " " + fin);
    }

    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

}
