/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;

import android.os.Bundle;


public class SettingsActivity extends BaseActivity {


    //UI Reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSetUpCase();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

}
