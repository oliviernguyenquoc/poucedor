/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends BaseActivity {


    //UI Reference
    private CheckBox localisationCheckbox;
    private TextView giveLocalisation, everyTime;
    private Spinner spinner;
    //Variable to avoid toast to appear when setting the spinner
    private boolean mIsSpinnerFirstCall;

    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSetUpCase();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean locationEnable = settings.getBoolean("locationEnable", false);

        localisationCheckbox = (CheckBox) findViewById(R.id.checkBox);
        //localisationCheckbox.setText(R.string.localisationCheckBoxText);

        if(locationEnable){
            localisationCheckbox.setChecked(true);
        }

        giveLocalisation = (TextView) findViewById(R.id.giveLocalisation);
        everyTime = (TextView) findViewById(R.id.everyTime);
        spinner = (Spinner) findViewById(R.id.spinner);

        if (!localisationCheckbox .isChecked()) {
            giveLocalisation.setVisibility(View.INVISIBLE);
            everyTime.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }


        addListenerOnLocalisationCheckbox();

        //Variable to avoid toast to appear when setting the spinner
        mIsSpinnerFirstCall=true;
        addTextAndSpinnerTimeSelection();

    }

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
                    everyTime.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    editor.putBoolean("locationEnable", true);

                    // Commit the edits!
                    editor.commit();
                }
                else{
                    giveLocalisation.setVisibility(View.INVISIBLE);
                    everyTime.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    editor.putBoolean("locationEnable", true);

                    // Commit the edits!
                    editor.commit();
                }
            }
        });
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void addTextAndSpinnerTimeSelection() {
        giveLocalisation = (TextView) findViewById(R.id.giveLocalisation);
        giveLocalisation.setText(R.string.giveLocalisation);

        spinner = (Spinner) findViewById(R.id.spinner);
        final List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("5");
        spinnerList.add("15");
        spinnerList.add("30");
        spinnerList.add("60");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View view, int time, long id) {
                    //To avoid toast to appear when setting the spinner
                    if(!mIsSpinnerFirstCall){
                        showToast(getResources().getString(R.string.localisationRecord) + " " + spinnerList.get((int) id) + " " + getResources().getString(R.string.everyTime));

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

        everyTime = new TextView(this);
        everyTime.setText(R.string.everyTime);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

}
