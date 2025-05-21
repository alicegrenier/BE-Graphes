package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double cout_estime ;

    public LabelStar(Node init_sommet_courant, boolean init_marque, double init_cout, Node init_pere, int vitesse) {
        super(init_sommet_courant, init_marque, init_cout, init_pere) ;
        this.cout_estime = heuristique();
    }

    public double heuristique(Node origine, Node destination, int vitesse_max, int mode) {

    }

    public double getCoutEstime() {
        return this.cout_estime ;
    }

    public void setCoutEstime(double x) {
        this.cout_estime = x ;
    }

    public double getTotalCost() {
        return this.getCost() + this.cout_estime ;
    }

    /* version de compareTo n'utilisant pas getTotalCost
    public int compareTo(LabelStar label) {
        int res ;
        res = (int)(this.getCost() + this.cout_estime - (label.getCost() + label.getCoutEstime())) ;
        if (res == 0) {
            return (int)(this.cout_estime - label.getCoutEstime()) ;
        } else {
            return res ;
        }
    }*/


    // version de compareTo avec getTotalCost
    public int compareTo(LabelStar label) {
        int res ;
        res = (int)(this.getTotalCost() - label.getTotalCost()) ;
        if (res == 0) {
            return (int)(this.cout_estime - label.getCoutEstime()) ;
        } else {
            return res ;
        }
    }

}