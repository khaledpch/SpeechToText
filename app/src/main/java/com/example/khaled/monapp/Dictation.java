package com.example.khaled.monapp;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.camera2.params.OutputConfiguration;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import jp.naist.ahclab.speechkit.Recognizer;
import jp.naist.ahclab.speechkit.ServerInfo;
import jp.naist.ahclab.speechkit.SpeechKit;
import jp.naist.ahclab.speechkit.view.ListeningDialog;

public class Dictation extends SimpleActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListeningDialog lst_dialog;
    String Message;
    int data_block = 100;
    EditText ed_result;
    FloatingActionButton fab;

    protected ServerInfo serverInfo = new ServerInfo();
    Recognizer _currentRecognizer;

    void init_speechkit(ServerInfo serverInfo) {
        SpeechKit _speechKit = SpeechKit.initialize(getApplication().getApplicationContext(), "", "", serverInfo);
        _currentRecognizer = _speechKit.createRecognizer(Dictation.this);
        _currentRecognizer.connect();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictation);
        Intent intent = getIntent();
        String mm =  intent.getStringExtra("Message");
        lst_dialog = new ListeningDialog(Dictation.this);

        ed_result = (EditText)findViewById(R.id.ed_result);
        ed_result.setText(mm);
        serverInfo.setAddr(this.getResources().getString(R.string.default_server_addr));
        serverInfo.setPort(Integer.parseInt(this.getResources().getString(R.string.default_server_port)));
        serverInfo.setAppSpeech(this.getResources().getString(R.string.default_server_app_speech));
        serverInfo.setAppStatus(this.getResources().getString(R.string.default_server_app_status));

        init_speechkit(serverInfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentRecognizer.start();
                lst_dialog.show();

            }
        });

        Button.OnClickListener stop_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentRecognizer.stopRecording();
            }
        };
        lst_dialog.prepare(stop_listener);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dictation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {


           startActivity(new Intent(Dictation.this, Tab1.class));







        } else if (id == R.id.nav_new) {

            ed_result.setText("");

        }else if (id == R.id.nav_share) {

        }

     else if (id == R.id.nav_Delete) {

    }


        else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_save) {
            Message = ed_result.getText().toString();
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Notes");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Note-"+ n +".txt";
            File file = new File (myDir, fname);
            if (file.exists ()) file.delete ();

            try {

                FileOutputStream Fou = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(Fou);
                try {
                    osw.write(Message);
                    osw.flush();
                    osw.close();
                    Toast.makeText(getBaseContext(), "Data Saved", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onPartialResult(String result) {
        ed_result.setText(result);
    }

    @Override
    public void onFinalResult(String result) {
        ed_result.setText(result);
    }

    @Override
    public void onFinish(String reason) {
        if (lst_dialog.isShowing())
            lst_dialog.dismiss();
    }

    @Override
    public void onReady(String reason) {
        fab.setEnabled(true);
    }

    @Override
    public void onNotReady(String reason) {
        fab.setEnabled(false);
        Toast.makeText(getApplicationContext(),"Server connected, but not ready, reason: "+reason,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateStatus(SpeechKit.Status status) {

    }

    @Override
    public void onRecordingBegin() {
        lst_dialog.setText("Listening");
    }

    @Override
    public void onRecordingDone() {
        lst_dialog.setText("Please wait!");
    }

    @Override
    public void onError(Exception error) {

    }

}



