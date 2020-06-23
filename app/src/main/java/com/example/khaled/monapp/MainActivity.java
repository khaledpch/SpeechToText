package com.example.khaled.monapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private Button button;
    private Button button2;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Dictation.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMainSortByTimestamp:
               // sort(item, SORT_ORDER_TIMESTAMP);
                return true;
            case R.id.menuMainSortByTranslation:
                //sort(item, SORT_ORDER_TRANSLATION);
                return true;
            case R.id.menuMainSortByEvaluation:
                //sort(item, SORT_ORDER_EVALUATION);
                return true;
            case R.id.menuMainExamples:
                //startActivity(new Intent(this, ExamplesActivity.class));
                return true;
            case R.id.menuMainSettings:
                //startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  //  private void sort(MenuItem item, String sortOrder) {

    //    item.setChecked(true);
        // Save the ID of the selected item.
        // TODO: ideally this should be done in onDestory
      //  SharedPreferences.Editor editor = mPrefs.edit();
        //editor.putInt(getString(R.string.prefCurrentSortOrderMenu), item.getItemId());
        //editor.apply();
//}
}
