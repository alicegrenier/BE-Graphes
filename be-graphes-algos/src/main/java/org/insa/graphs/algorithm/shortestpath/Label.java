package org.insa.graphs.algorithm.shortestpath;
//import java.util.* ;
//import org.insa.graphs.model.Arc;
//import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
//import org.insa.graphs.model.Path;

public class Label {

    // attributs :

    private Node sommet_courant ; // sommet associé à ce label

    private boolean marque ; // si le sommet est marqué, vrai quand le coût min de ce sommet est définitivement connu par l'algorithme

    private int cout_realise ; // valeur courante du plus court chemin depuis l'origine vers le sommet

    private Node pere ; // sommet précédent sur le chemin correspondant au plus court chemin courant
    // plutôt stocker l'arc ????????

    

    // constructeur :

    public Label(Node init_sommet_courant, boolean init_marque, int init_cout, Node init_pere) {
        this.sommet_courant = init_sommet_courant ;
        this.marque = init_marque ;
        this.cout_realise = init_cout ;
        this.pere = init_pere ;
        
    }

    // méthodes :

    public Node getSommetCourant() {
        return this.sommet_courant ;
    }

    public boolean getMarque() {
        return this.marque ;
    }

    public int getCost() {
        return this.cout_realise ;
    }

    public Node getPere() {
        return this.pere ; 
    }

    public void setSommetCourant(Node x) {
        this.sommet_courant = x ;
    }

    public void setMarque(boolean x) {
        this.marque = x ;
    }

    public void setCost(int x) {
        this.cout_realise = x ;
    }

    public void setPere(Node x) {
        this.pere = x ;
    }
}