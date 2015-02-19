package no.lislebo.butrikk;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    private final String FILENAME = "deltakere.csv";
    private final String PATH = "/storage/sdcard1/Documents/";
    private final String TAG = "TRIKK";
    private ListView listView;
    private CustomList participantList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        participantList = new CustomList();
        listView = (ListView)findViewById(R.id.listview);
        createListView();
    }

    private void createListView() {
        try {
            BufferedReader reader = new BufferedReader( new FileReader( new File(PATH + FILENAME) ) );
            Log.d(TAG, "Successfully opened participants file");
            while (reader.ready()) {
                String[] participant = reader.readLine().split(",");
                String name = participant[0];
                int vouchers = Integer.parseInt(participant[1]);
                participantList.add(name + " - " + vouchers);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Collections.sort(participantList);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,participantList));
        listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                    //args2 is the listViews Selected index
                }
            });
    }

    public void scanQrCode(View view) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                if (participantList.contains(contents)) {
                    listView.smoothScrollToPosition(13);
                    Toast.makeText(this, "Position in list: " + participantList.indexOf(contents), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Fant ikke deltaker", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class CustomList extends ArrayList<String> {

        @Override
        public boolean contains(Object o) {
            if (o == null)
                return false;

            String comp;
            if (o instanceof String)
                comp = (String)o;
            else
                return false;

            for (String s : this) {
                if (s.split(" - ")[0].equalsIgnoreCase(comp))
                    return true;
            }
            return false;
        }

        @Override
        public int indexOf(Object o) {
            int index = 0;
            if (o == null) {
                for (String s : this) {
                    if (s == null)
                        return index;
                    index++;
                }
                return -1;
            } else {
                String comp = (String)o;
                for (String s : this) {
                    if ( s.split(" - ")[0].equalsIgnoreCase(comp) )
                        return index;
                    index++;
                }
                return -1;
            }
        }
    }

}
