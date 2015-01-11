/*
 *
 * Created by MOLLET-PADIER Loïc and NGUYEN QUOC Olivier
 *
 */

package fr.poucedor.poucedor;


import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;


public class RankingActivity extends BaseActivity {

    //UI Reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        toolbarSetUpCase();

        addAndCompleteRankingTable();

    }

    public void addAndCompleteRankingTable(){
        try {
            TableLayout tv = (TableLayout) findViewById(R.id.table);
            tv.removeAllViewsInLayout();
            int flag = 1;

            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()

            /*
            for (int i = -1; i < jArray.length(); i++) {

                TableRow tr = new TableRow(Yourclassname.this);

                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));

                // this will be executed once
                if (flag == 1) {

                    TextView b3 = new TextView(Yourclassname.this);
                    b3.setText("column heading 1");
                    b3.setTextColor(Color.BLUE);
                    b3.setTextSize(15);
                    tr.addView(b3);

                    TextView b4 = new TextView(Yourclassname.this);
                    b4.setPadding(10, 0, 0, 0);
                    b4.setTextSize(15);
                    b4.setText("column heading 2");
                    b4.setTextColor(Color.BLUE);
                    tr.addView(b4);
                }
            }
        */
        }
        catch(Exception e){
            Toast.makeText(RankingActivity.this, "No data to print", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ranking;
    }


}

