package formulairepav.plectre.com.formulairepav.model;

import java.util.Calendar;

/**
 * Created by Plectre on 22/09/2017.
 */

public class GetDate {

    private String day;
    private String date;

    public String getDate() {

        getDay();
        return date;
    }


    private void getDay() {
        final Calendar calendar = Calendar.getInstance();
        final int int_day = calendar.get(Calendar.DAY_OF_WEEK); // Dimanche = 1
        final int int_day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        final int int_month = calendar.get(Calendar.MONTH) + 1; // janvier = 0
        final int int_year = calendar.get(Calendar.YEAR);

        switch (int_day) {
            case 1:
                day = "Dimanche";
                break;
            case 2:
                day = "Lundi";
                break;
            case 3:
                day = "Mardi";
                break;
            case 4:
                day = "Mercredi";
                break;
            case 5:
                day = "Jeudi";
                break;
            case 6:
                day = "Vendredi";
                break;
            case 7:
                day = "Samedi";
                break;
        }
        date = day + "_"
                + String.valueOf(int_day_of_month) + "_"
                + String.valueOf(int_month) + "_"
                + String.valueOf(int_year) + "_";
    }
}
