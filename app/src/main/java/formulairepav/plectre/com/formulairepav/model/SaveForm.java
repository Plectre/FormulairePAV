package formulairepav.plectre.com.formulairepav.model;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.ArrayList;

import formulairepav.plectre.com.formulairepav.R;
import formulairepav.plectre.com.formulairepav.view.MainActivity;

/**
 * Created by Plectre on 18/09/2017.
 *
 * Classe chrgée de checker la presence stokage externe
 * et de sauvgarder le tableau dans le fichier formaté
 * jj_mm_aa_pav.txt
 **/

public class SaveForm {

    private ArrayList<String> fields;
    private File mFichier;
    private String path;
    private String PAV = "Pav.txt";

    MainActivity mainActivity = new MainActivity();

    public SaveForm(ArrayList<String> fields) {
        this.fields = fields;

        checkExternalStorage();
        createFile();
        savePav();
    }


    public void checkExternalStorage() {
        // Check de la presence de la carte

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            path = Environment.getExternalStorageDirectory().getPath();
            path = path + "/FORM_PAV/";
            Log.i("saveClass", path);

        } else {
            Log.i("saveClass", "Pas de carte en vue");
            mainActivity.mesToasts(mainActivity.getString(R.string.no_sd_card));
            
        }
    }

    public void createFile() {
        // récupére le chemin et crée le dossier
        mFichier = new File(path);
        mFichier.mkdir();
    }


    // Methode de sauvgarde des champs
    public void savePav() {

        GetDate gDate = new GetDate();
        String date = gDate.getDate();
        //path = path + date;
        path = path + PAV;
        Log.i("DATE", date);
        BufferedWriter fOut;

        try {
            fOut = new BufferedWriter(new FileWriter(path, true)); // Le true sert à ne pas ecraser les données existantes
            // On parcours le tableau en parametre
            for (String field : fields) {
                // Convertion en UTF-8
                field.getBytes( Charset.forName("UTF-8"));
                fOut.write(field.toLowerCase() + ";"); // Enregistrement du champs dans le fichier

            }
            fOut.newLine();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mainActivity.mesToasts(mainActivity.getString(R.string.save_failed));
        } catch (IOException e) {
            mainActivity.mesToasts(mainActivity.getString(R.string.save_failed));
            e.printStackTrace();
        }

    }

}