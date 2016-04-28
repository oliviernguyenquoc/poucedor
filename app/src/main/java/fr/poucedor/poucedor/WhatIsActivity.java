package fr.poucedor.poucedor;

import android.os.Bundle;
import android.widget.ImageView;


public class WhatIsActivity extends BaseActivity {


    //UI Reference
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is);

        //toolbarSetUpCase();

    }

/*    @Override
    protected int getLayoutResource() {
        return R.layout.activity_what_is;
    }*/

}
