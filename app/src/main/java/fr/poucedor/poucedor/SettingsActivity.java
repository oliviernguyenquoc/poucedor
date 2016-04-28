/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {


    //UI Reference
    private CheckBox localisationCheckbox;
    private TextView giveLocalisation;
    private Spinner spinner;
    //Variable to avoid toast to appear when setting the spinner
    private boolean mIsSpinnerFirstCall;

    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean locationEnablePref = settings.getBoolean("locationEnable", false);

        localisationCheckbox = (CheckBox) findViewById(R.id.checkBox);
        //localisationCheckbox.setText(R.string.localisationCheckBoxText);

        if(locationEnablePref){
            localisationCheckbox.setChecked(true);
        }

        giveLocalisation = (TextView) findViewById(R.id.giveLocalisation);
        spinner = (Spinner) findViewById(R.id.spinner);

        if (!localisationCheckbox.isChecked()) {
            giveLocalisation.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }


        addListenerOnLocalisationCheckbox();

        //Variable to avoid toast to appear when setting the spinner
        mIsSpinnerFirstCall=true;
        addTextAndSpinnerTimeSelection();

    }

    /**
     * Function to add a listener to the checkbox (Enable/disable automatic location)
     */
    public void addListenerOnLocalisationCheckbox() {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        localisationCheckbox = (CheckBox) findViewById(R.id.checkBox);

        localisationCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    giveLocalisation.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    editor.putBoolean("locationEnable", true);

                    // Commit the edits!
                    editor.commit();
                }
                else{
                    giveLocalisation.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    editor.putBoolean("locationEnable", true);

                    // Commit the edits!
                    editor.commit();
                }
            }
        });
    }

    /**
     * Show a toast
     * @param msg The message to show
     */
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * All to setup the spinner to select the interval between 2 automatic locations
     */
    public void addTextAndSpinnerTimeSelection() {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor2 = settings.edit();

        int locationTimePref = settings.getInt("locationTime", 60);

        giveLocalisation = (TextView) findViewById(R.id.giveLocalisation);
        giveLocalisation.setText(R.string.giveLocalisation);

        //To refere to choice just after. Do not forget to change this list if you change the spinnerList
        final List<Integer> equivalentList = new ArrayList<>();
        equivalentList.add(20);
        equivalentList.add(40);
        equivalentList.add(60);
        equivalentList.add(120);

        spinner = (Spinner) findViewById(R.id.spinner);
        final List<String> spinnerList = new ArrayList<>();
        spinnerList.add("20 " + getResources().getString(R.string.minutes));
        spinnerList.add("40 "  + getResources().getString(R.string.minutes));
        spinnerList.add("1 " + getResources().getString(R.string.hour));
        spinnerList.add("2 "  + getResources().getString(R.string.hours));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        //Select automatically the choice saved in the preference
        spinner.setSelection(equivalentList.indexOf(locationTimePref));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View view, int time, long id) {

                    editor2.putInt("locationTime", equivalentList.get((int) id));

                    // Commit the edits!
                    editor2.commit();

                    //To avoid toast to appear when setting the spinner
                    if(!mIsSpinnerFirstCall){
                        showToast(getResources().getString(R.string.localisationRecord) + " " + spinnerList.get((int) id) );

                    }else
                    {
                        mIsSpinnerFirstCall=false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //Just do nothing
                }
            });

    }

}
