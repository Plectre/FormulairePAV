package formulairepav.plectre.com.formulairepav.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Plectre on 21/09/2017.
 */

public class FillSpinner {

    public List<String> fillMatiere() {

        List<String> matieres = new ArrayList<>();

        matieres.add("__________________");
        matieres.add("Verre");
        matieres.add("OM");
        matieres.add("Selectif");

        return matieres;
    }

    public List<String> jours() {

        List<String> jours = new ArrayList<>();
        jours.add("__________________");
        jours.add("Lundi");
        jours.add("Mardi");
        jours.add("Mercredi");
        jours.add("Jeudi");
        jours.add("Vendredi");
        jours.add("Samedi");

        return jours;
    }
}
