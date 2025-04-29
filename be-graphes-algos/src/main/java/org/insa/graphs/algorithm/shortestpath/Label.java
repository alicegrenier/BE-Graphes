//import java.lang.* ;
import java.util.* ;
//import java.object.* ;

public class Label {

    // attributs :

    private Node sommet_courant ; // sommet associé à ce label

    private boolean marque ; // si le sommet est marqué

    private int cout_realise ; // valeur courante du plus court chemin depuis l'origine vers le sommet

    private Node pere ; // sommet précédent sur le chemin correpsondant au plus court chemin courant
    // plutôt stocker l'arc ????????

    // constructeur :

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
}