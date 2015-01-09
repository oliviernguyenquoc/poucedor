/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;


import android.os.Bundle;

import android.view.View;
import android.widget.GridView;


public class RankingActivity extends BaseActivity {

    //UI Reference
    private View mRankTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ranking);
        mRankTable = (GridView) findViewById(R.id.rank_grid);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ranking;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_RANKING;
    }

}
