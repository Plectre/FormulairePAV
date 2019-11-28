package formulairepav.plectre.com.formulairepav.controler;

import android.widget.EditText;

import formulairepav.plectre.com.formulairepav.view.MainActivity;

/**
 * Created by Plectre on 15/09/2017.
 */

public class Display extends MainActivity {


    public void initDisplay() {}


    public void clearDisplay() {


        sp_jours.setSelection(0,false);
        sp_matiere.setSelection(0,false);
        et_pav.setText("");
        et_pav.setFocusable(true);
        et_code.setText("");
        et_lib.setText("");
        //et_matiere.setText("");
        //et_client.setText("");
        et_ville.setText("");
        //et_cp.setText("");
        et_adresse.setText("");
        //et_volume.setText("");
        //et_jdecollecte.setText("");

    }
}
