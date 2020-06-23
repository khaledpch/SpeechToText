package com.example.khaled.monapp;

/**
 * Created by khaled on 02/06/2017.
 */

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.*;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AndroidFileBrowser extends ListActivity {

    private enum DISPLAYMODE {ABSOLUTE, RELATIVE;}

    private String uri_path = "file:///mnt/sdcard/Notes";

    private final DISPLAYMODE displayMode = DISPLAYMODE.ABSOLUTE;
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/Notes");

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.file_row);
        // setContentView() gets called within the next line,
        // so we do not need it here.
        browseToRoot();
    }

    /**
     * This function browses to the
     * root-directory of the file-system.
     */
    private void browseToRoot() {
        browseTo(new File("/Notes"));
    }

    /**
     * This function browses up one level
     * according to the field: currentDirectory
     */
    private void upOneLevel() {
        if (this.currentDirectory.getParent() != null)
            this.browseTo(this.currentDirectory.getParentFile());
    }

    private void browseTo(final File aDirectory) {
        if (aDirectory.isDirectory()) {
            this.currentDirectory = aDirectory;
            fill(aDirectory.listFiles());
        } else {
            OnClickListener okButtonListener = new OnClickListener() {
                // @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    // Lets start an intent to View the file, that was clicked...
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("file://" + aDirectory.getAbsolutePath()));
                    startActivity(i);

                }
            };
            OnClickListener cancelButtonListener = new OnClickListener() {
                // @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // Do nothing
                }
            };
//AlertDialog alert=new AlertDialog();

       //     alert.show( this,"Question", "Do you want to open that file?n"
         //           + aDirectory.getName(),"OK",  okButtonListener,
           //         "Cancel",  cancelButtonListener, "false, null);

        }
    }

    private void fill(File[] files) {
        this.directoryEntries.clear();

        // Add the "." and the ".." == 'Up one level'
        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        this.directoryEntries.add(".");

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        switch (this.displayMode) {
            case ABSOLUTE:
                for (File file : files) {
                    this.directoryEntries.add(file.getPath());
                }
                break;
            case RELATIVE: // On relative Mode, we have to add the current-path to the beginning
                int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
                for (File file : files) {
                    this.directoryEntries.add(file.getAbsolutePath().substring(currentPathStringLenght));
                }
                break;
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                R.layout.file_row, this.directoryEntries);

        this.setListAdapter(directoryList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int selectionRowID = (int) this.getSelectedItemId();
        String selectedFileString = this.directoryEntries.get(selectionRowID);
        if (selectedFileString.equals(".")) {
            // Refresh
            this.browseTo(this.currentDirectory);
        } else if (selectedFileString.equals("..")) {
            this.upOneLevel();
        } else {
            File clickedFile = null;
            switch (this.displayMode) {
                case RELATIVE:
                    clickedFile = new File(this.currentDirectory.getAbsolutePath()
                            + this.directoryEntries.get(selectionRowID));
                    break;
                case ABSOLUTE:
                    clickedFile = new File(this.directoryEntries.get(selectionRowID));
                    break;
            }
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }
}
