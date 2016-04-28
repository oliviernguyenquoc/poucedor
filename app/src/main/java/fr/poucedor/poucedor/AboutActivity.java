package fr.poucedor.poucedor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class AboutActivity extends BaseActivity {


    //UI Reference
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //toolbarSetUpCase();

    }

/*    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }*/

}