package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double cout_estime ;

    public LabelStar(Node sommet_courant, boolean init_marque, double init_cout, Node init_pere,ShortestPathData data) {
        super(sommet_courant, init_marque, init_cout, init_pere) ;
        this.cout_estime = heuristique(sommet_courant,data.getDestination(),data.getMaximumSpeed(),data.getMode());
    }

    //j'ai mis final pour éviter le warning car potentiellement, cette méthode pourrait être override alors qu'elle est appelée dans un constructeur 
    public final double heuristique(Node origine, Node destination, int vitesse_max, Mode mode) {
        double cout;
        //calcul de la distance et on a besoin de la distance pour calculer le temps 
        cout=origine.getPoint().distanceTo(destination.getPoint()); 
        if (mode.equals(Mode.TIME)){ //tps=dist/vitesse, vitesse actuellement km/h, distance en m 
            cout=origine.getPoint().distanceTo(destination.getPoint())*(vitesse_max*3600/1000);
        }
        return cout;
    }

    public double getCoutEstime() {
        return this.cout_estime ;
    }

    public void setCoutEstime(double x) {
        this.cout_estime = x ;
    }

    @Override
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