package formulairepav.plectre.com.formulairepav.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import formulairepav.plectre.com.formulairepav.R;
import formulairepav.plectre.com.formulairepav.controler.Display;
import formulairepav.plectre.com.formulairepav.model.FillSpinner;
import formulairepav.plectre.com.formulairepav.model.SaveForm;

public class MainActivity extends AppCompatActivity {

    // Element view
    RadioGroup rg_modele;
    public static EditText et_pav;
    public static EditText et_code;
    public static EditText et_lib;
    public static Spinner sp_matiere;
    public static Spinner sp_jours;
    public static EditText et_ville;
    public static EditText et_adresse;

    Button btn_valider;

    // Datas
    private final String CLIENT = "sivom";
    public final String INIT_SPINNER = "__________________";

    ArrayList<String> fields;
    private String itemMatiere;
    private String itemJour;
    double lat;
    double lng;
    LocationManager lm;
    LocationListener ll;
    boolean inbound = false;
    double PRECISION = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesToastsLong(getString(R.string.asterisque_obligatoire));

        rg_modele = (RadioGroup) findViewById(R.id.rg_modele);

        sp_matiere = (Spinner) findViewById(R.id.sp_matiere);
        sp_jours = (Spinner) findViewById(R.id.sp_jours);

        et_pav = (EditText) findViewById(R.id.et_pav);
        et_code = (EditText) findViewById(R.id.et_code);
        et_lib = (EditText) findViewById(R.id.et_libcolone);
        et_ville = (EditText) findViewById(R.id.et_ville);
        et_adresse = (EditText) findViewById(R.id.et_adresse);

        btn_valider = (Button) findViewById(R.id.btn_valider);

        // Recuperer la liste matiere dans FillSpinner
        FillSpinner fillSpinner = new FillSpinner();
        List<String> matieres = fillSpinner.fillMatiere();
        List<String> jours = fillSpinner.jours();

        // Créer adapter pour les spinners
        ArrayAdapter<String> dataAdapterMatieres = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, matieres);
        ArrayAdapter<String> dataAdapterJours = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jours);
        dataAdapterMatieres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterJours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_matiere.setAdapter(dataAdapterMatieres);
        sp_jours.setAdapter(dataAdapterJours);
        /**sp_matiere.setVisibility(View.INVISIBLE);*/

        // On rend le boutton innactif

        setButton(getString(R.string.coorNotOk), false);

        /** Appel methode localisation qui demarre le gps */
        localisation();

        Display display = new Display();
        display.clearDisplay();

        sp_matiere.setOnItemSelectedListener(new MatiereSpinnerClass());
        sp_jours.setOnItemSelectedListener(new JourSpinnerClass());
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Test si les champs sont rempli
                fields = getFields();

                Boolean fieldsIsOk = gestionDesChamps();

                if (!fieldsIsOk) {
                    mesToasts(getString(R.string.asterisque_obligatoire));
                    return;
                }
                // envoi du tableau vers la classe "SaveForm()" qui vas se charger de l'enregistrer
                new SaveForm(fields);

                Display display = new Display();
                display.clearDisplay();
                display.initDisplay();

            }
        });

    }

    /**
     * Methode qui recupére et rempli le tableau les données
     * récupéré dans les champs
     */
    public ArrayList<String> getFields() {

        fields = new ArrayList<>();

        // ici on recupére le radioButton selectionné dans le radioGroup
        RadioButton selectRadio = (RadioButton) findViewById(rg_modele.getCheckedRadioButtonId());

        // Si un des deux est checker on l' ajoute à l' Array() fields
        // Si non on affiche un toast !
//        if (selectRadio != null) {
//            fields.add((String) selectRadio.getText());
//        } else {
//            mesToasts("Seléctionner un volume svp !");
//        }

        /**
         * La colonne PAV/POI et remplis avec l'adresse
         * */
        //fields.add((String) selectRadio.getText());
        //fields.add(String.valueOf(et_pav.getText()));
        fields.add(String.valueOf(et_adresse.getText())); // Champs 1: PAV/POI
        fields.add(String.valueOf(et_code.getText())); // Champs 2: CODE
        fields.add(String.valueOf(et_lib.getText())); // Champs 3 : LIB/COL
        //fields.add(String.valueOf(et_matiere.getText()));
        fields.add(itemMatiere); // Champs 4 : MATIERE
        fields.add(CLIENT); // Champs 5 : CLIENT
        //fields.add(String.valueOf(et_client.getText()));

        fields.add(String.valueOf(et_ville.getText())); // Champs 6 : VILLE
        fields.add(String.valueOf(et_adresse.getText())); // Champs 7 : ADRESSE
        //fields.add(String.valueOf(et_cp.getText()));
        fields.add(String.valueOf(lat)); // Champs 8 : LATITUDE
        fields.add(String.valueOf(lng)); // Champs 9 : LONGITUDE

        //fields.add(String.valueOf(et_volume.getText()));
        fields.add((String) selectRadio.getText()); // Champs  10 : VOLUME : Texte du radioButon selectionné
        //fields.add(String.valueOf(et_jdecollecte.getText()));
        fields.add(itemJour); // Champs 11 : JOUR DE COLLECTTE

        return fields;

    }


    public void mesToasts(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void mesToastsLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    public void localisation() {


        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        ll = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                double accuracy = location.getAccuracy();

                /** Si la precision du gps < 20m et les données gps sont
                 * differentes de zero
                 * alors on change le texte du bouton
                 * et on le rend actif
                 **/

                if (accuracy < PRECISION && lat != 0.0 || lng != 0.0) {
                    inbound = true;
                    setButton(getString(R.string.coorOk), true);
                } else {
                    setButton(getString(R.string.coorNotOk), false);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates("gps", 1000, 0, ll);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desabonement du gps
        lm.removeUpdates(ll);
    }

    // Gestion du changement d'etat du bouton de validation
    public void setButton(String msg, boolean isEnable) {
        btn_valider.setText(msg);
        btn_valider.setEnabled(isEnable);
    }


    // Fonction qui géres le champs vides pour interdir la validation
    public boolean gestionDesChamps() {


        if (itemMatiere == INIT_SPINNER ||
                et_ville.getText().toString().equals("") ||
                //et_cp.getText().toString().equals("") ||
                et_adresse.getText().toString().equals("") ||
                itemJour == INIT_SPINNER
                ) {
            return false;
        }
        mesToasts("Enregistrement effectué");
        return true;
    }


    class JourSpinnerClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> list, View view, int position, long l) {
            itemJour = list.getItemAtPosition(position).toString();
            Log.i("TAG", itemJour);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class MatiereSpinnerClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> list, View view, int position, long l) {
            itemMatiere = list.getItemAtPosition(position).toString();
            Log.i("TAG", itemMatiere);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
