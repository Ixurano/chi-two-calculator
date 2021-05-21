package fi.arcada.sos21proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    TextView textViewCol1;
    TextView textViewCol2;
    TextView textViewRow1;
    TextView textViewRow2;

    TextView textViewBarn;
    TextView textViewVuxna;
    TextView textViewChiResult;
    TextView textViewSigni;
    TextView textViewSignifikans;
    TextView textViewPvalue;
    TextView textViewResult;
    // Deklarera 4 Button-objekt
    Button btn1, btn2, btn3, btn4, btnReset;
    // Deklarera 4 heltalsvariabler för knapparnas värden
    int val1, val2, val3, val4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        textViewCol1 = findViewById(R.id.textViewCol1);
        textViewCol1.setText(prefs.getString("textViewCol1", "Barn"));
        textViewCol2 = findViewById(R.id.textViewCol2);
        textViewCol2.setText(prefs.getString("textViewCol2", "Vuxna"));
        textViewRow1 = findViewById(R.id.textViewRow1);
        textViewRow1.setText(prefs.getString("textViewRow1", "Använder mask"));
        textViewRow2 = findViewById(R.id.textViewRow2);
        textViewRow2.setText(prefs.getString("textViewRow2", "Använder inte mask"));
        textViewBarn = findViewById(R.id.textViewBarn);
        textViewBarn.setText(prefs.getString("textViewCol1", "Barn"));
        textViewVuxna = findViewById(R.id.textViewVuxna);
        textViewVuxna.setText(prefs.getString("textViewCol2", "Vuxna"));
        textViewChiResult = findViewById(R.id.textViewChiResult);
        textViewSigni = findViewById(R.id.textViewSigni);
        textViewSignifikans = findViewById(R.id.textViewSignifikans);
        textViewPvalue = findViewById(R.id.textViewPvalue);
        textViewResult = findViewById(R.id.textViewResult);

        // Koppla samman Button-objekten med knapparna i layouten
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btnReset = findViewById(R.id.buttonReset);

        val1 = prefs.getInt("counter", 0);
        val2 = prefs.getInt("counter2", 0);
        val3 = prefs.getInt("counter3", 0);
        val4 = prefs.getInt("counter4", 0);
        calculate();
        textViewBarn.setText("");
        textViewVuxna.setText("");
        textViewChiResult.setText("");
        textViewSigni.setText("");
        textViewSignifikans.setText("");
        textViewPvalue.setText("");

    }

    /**
     * Klickhanterare för knapparna
     */
    public void buttonClick(View view) {

        if (view.getId() == R.id.buttonSettings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else {

            if (view.getId() == R.id.button1) val1++;
            if (view.getId() == R.id.button2) val2++;
            if (view.getId() == R.id.button3) val3++;
            if (view.getId() == R.id.button4) val4++;


            // Slutligen, kör metoden som ska räkna ut allt!
            calculate();
        }
    }

    //Reset data
    @SuppressLint("SetTextI18n")
    public void buttonReset(View view) {
        if (view.getId() == R.id.buttonReset) {
            val1 = 0;
            val2 = 0;
            val3 = 0;
            val4 = 0;
           /* prefsEditor.putInt("counter", 0);
            prefsEditor.putInt("counter2", 0);
            prefsEditor.putInt("counter3", 0);
            prefsEditor.putInt("counter4", 0);*/

            btn1.setText(String.valueOf(val1));
            btn2.setText(String.valueOf(val2));
            btn3.setText(String.valueOf(val3));
            btn4.setText(String.valueOf(val4));
            calculate();
            textViewBarn.setText("");
            textViewVuxna.setText("");
            textViewChiResult.setText("");
            textViewSigni.setText("");
            textViewSignifikans.setText("");
            textViewPvalue.setText("");

        }
    }

    /**
     * Metod som uppdaterar layouten och räknar ut själva analysen.
     */
    @SuppressLint("DefaultLocale")
    public void calculate() {


        // Uppdatera knapparna med de nuvarande värdena
        prefsEditor = prefs.edit();
        prefsEditor.putInt("counter", val1);
        prefsEditor.putInt("counter2", val2);
        prefsEditor.putInt("counter3", val3);
        prefsEditor.putInt("counter4", val4);
        prefsEditor.apply();


        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));

        // Mata in värdena i Chi-2-uträkningen och ta emot resultatet
        // i en Double-variabel
        double chi2 = Significance.chiSquared(val1, val2, val3, val4);

        // Mata in chi2-resultatet i getP() och ta emot p-värdet
        double pValue = Significance.getP(chi2);

        // Signifikant nivå

        double signiA = 0.05;
        // skriver ut chi-2 samt P-värdet.

        textViewChiResult.setText(String.format("%s: %.2f", "Chi-2 resultat: ", chi2));
        textViewSignifikans.setText(String.format("%s", "Signifikansnivå: "));
        textViewSigni.setText(prefs.getString("textViewSigni", String.valueOf(signiA)));
        textViewPvalue.setText(String.format("%s: %.3f", "P-värde: ", pValue));
        // % för jakande svar
        textViewBarn.setText(String.format("%s: %.2f:%s", "Barn ", Significance.getProcentage(val1, val3), "%"));
        textViewVuxna.setText(String.format("%s: %.2f:%s", "Vuxna ", Significance.getProcentage(val2, val4), "%"));

    }


}