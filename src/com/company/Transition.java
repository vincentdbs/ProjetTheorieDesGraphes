package com.company;

public class Transition {
    int init, arc, fin;

    private Transition(int i_init, int i_arc, int i_fin){
        init = i_init;
        arc = i_arc;
        fin = i_fin;
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
