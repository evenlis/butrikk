package no.lislebo.butrikk;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    private final String filename = "deltakere.csv";
    private final String path = "/storage/sdcard1/Documents";
    private ListView listView;
    private List<String> participantList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        participantList = new ArrayList<String>();
        listView = (ListView)findViewById(R.id.listview);
        createListView();
    }

    private void createListView() {
        participantList.add("Holmestrand fjordhotell");
        participantList.add("Bǽdelænd");
        participantList.add("Tyholt Apenes");
        participantList.add("Kvinneforbud");
        participantList.add("Skummamelk");
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
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
