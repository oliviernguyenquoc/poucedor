/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSetUpCase();

        localisationCheckbox = (CheckBox) findViewById(R.id.checkBox);
        //localisationCheckbox.setText(R.string.localisationCheckBoxText);
        //localisationCheckbox.setChecked(true);

        addListenerOnLocalisationCheckbox();

        addTextAndSpinnerTimeSelection();

    }

    public void addListenerOnLocalisationCheckbox() {

        localisationCheckbox = (CheckBox) findViewById(R.id.checkBox);

        localisationCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(SettingsActivity.this,
                            "Bro, try Android :)", Toast.LENGTH_LONG).show();
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
        List<String> list = new ArrayList<String>();
        list.add("5");
        list.add("15");
        list.add("30");
        list.add("60");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View view, int time, long id) {
                    showToast(getResources().getString(R.string.localisationRecord) + " " + Integer.toString(time) + " " + getResources().getString(R.string.everyTime));
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
