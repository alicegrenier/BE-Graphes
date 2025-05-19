package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double cout_estime ;

    public LabelStar(Node init_sommet_courant, boolean init_marque, double init_cout, Node init_pere, double init_cout_estime) {
        super(init_sommet_courant, init_marque, init_cout, init_pere) ;
        this.cout_estime =  init_cout_estime;
    }

    public double getCoutEstime() {
        return this.cout_estime ;
    }

    public void setCoutEstime(double x) {
        this.cout_estime = x ;
    }

    public int compareTo(LabelStar label) {
        return (int)(this.getCost() + this.cout_estime - (label.getCost() + label.getCoutEstime())) ;
    }

}