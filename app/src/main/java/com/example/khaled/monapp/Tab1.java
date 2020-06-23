package com.example.khaled.monapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import android.app.ListActivity;

import static android.R.attr.name;

public class Tab1 extends Activity
{
    // Variables des objets que l'on manipule
    private ToggleButton toggleButton1;
    private ImageView imageViewOn;
    private TextView textReceive;
    private Button refresh;
    EditText txtData;
    int data_block =100;
    private long lastTime = 0;
    static String temp = "";
    private List<String> myList;
    String tt;
    private File file;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg)
        {
            String data = msg.getData().getString("receivedData");
            // Affichage de data
            long t = System.currentTimeMillis();
            if(t-lastTime > 100)
            {
                // Pour éviter que les messages soit coupés
                textReceive.append("\n");
                lastTime = System.currentTimeMillis();
            }
            textReceive.append(data);
        }
    };

    final Handler handlerStatus = new Handler() {
        public void handleMessage(Message msg)
        {
            int co = msg.arg1;
            if(co == 1) {
            } else if(co == 2) {
            }
        }
    };

    /** Appelée quand l'activitée est créée la 1ère fois. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Méthode qui affiche le contenu de onglet1 */
        setContentView(R.layout.activity_tab1);

        //refresh = (Button) findViewById(R.id.button2);

        final ListView listView = (ListView) findViewById(R.id.listView1);
        //ListView listView2 = (ListView) findViewById(R.id.listView2);
        myList = new ArrayList<String>();

        File directory = Environment.getExternalStorageDirectory();
        ///file = new File( directory + "/Test" );
        file = new File(directory + "/Notes");
        final File list[] = file.listFiles();

        for (int i = 0; i < list.length; i++) {
            myList.add(list[i].getName());

        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, myList);
        listView.setAdapter(adapter); //Set all the file in the list.

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {




                String file_name = myList.get(position);
                File uri = new File(file, file_name);
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(uri));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');

                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

//Find the view by its id

tt=text.toString();
               // Toast.makeText(getBaseContext(),"Message:"+text,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Tab1.this,Dictation.class);
                intent.putExtra("Message", tt);
                startActivity(intent);


            }





            });// onClick
        } // btnReadSDFile
            }





