package no.lislebo.butrikk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

public class ParticipantList extends ExpandableListActivity {

    private int n_participants;
    private final String filename = "deltakere.csv";
    private final String path = "/storage/sdcard1/Documents";
    private HashMap<String,int[]> drinkMap = new HashMap<String,int[]>();
    private List groupList;
    private SimpleExpandableListAdapter expListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.participant_list);
            groupList = createGroupList();
            expListAdapter =
                new SimpleExpandableListAdapter(
                                                this,
                                                groupList,
                                                R.layout.group_row,
                                                new String[] { "participant" },
                                                new int[] { R.id.row_name },
                                                createChildList(),
                                                R.layout.child_row,
                                                new String[] {"drink", "additional_drinks", "sub_drink", "add_drink"},
                                                new int[] { R.id.grp_child, R.id.additional_drinks, R.id.sub_dr_btn, R.id.add_dr_btn}
                                                );
            setListAdapter( expListAdapter );

        }catch(Exception e){

            System.out.println("Errrr +++ " + e.getMessage());
        }
    }

    private List createGroupList() {
        ArrayList result = new ArrayList();
        try {
            BufferedReader reader = new BufferedReader( new FileReader( new File(path+"/"+filename) ) );
            n_participants = 0;
            String line = "";
            while (!line.equals("-1")) {
                line = reader.readLine();
                String[] p = line.split(",");
                HashMap m = new HashMap();
                m.put("participant", p[0]);
                int[] p_drinks = {Integer.parseInt(p[1]),Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]), Integer.parseInt(p[6])};
                drinkMap.put(p[0], p_drinks);
                n_participants++;
                result.add(m);
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            Collections.sort((List)result, new CustomComparator());
            return (List)result;
        }
    }

    private List createChildList() {
        ArrayList result = new ArrayList();
        String[] drinkNames = {"Pils", "Alkoholfri øl", "Vin", "Alkoholfri vin", "Mineralvann"};
        for( int i = 0 ; i < n_participants ; ++i ) {
            ArrayList secList = new ArrayList();
            HashMap p = (HashMap)groupList.get(i);
            int[] paidDrinks = drinkMap.get(p.get("participant").toString());
            for( int j = 0; j < 5; j++ ) {
                HashMap child = new HashMap();
                if (paidDrinks[j] > 0) {
                    child.put("drink", drinkNames[j] + ": " + paidDrinks[j]);
                    child.put("additional_drinks", "");
                    child.put("add_drink", "+");
                    child.put("sub_drink", "-");
                    secList.add( child );
                }
            }
            result.add( secList );
        }
        return result;
    }
    public void  onContentChanged  () {
        super.onContentChanged();
    }

    public void addDrink(View view) {
        Button b = (Button)view;
        ViewGroup row = (ViewGroup)view.getParent();
        for (int i=0; i<row.getChildCount(); i++) {
            View v = row.getChildAt(i);
            if (v instanceof TextView && v.getId() == 2131034113) {
                TextView tv = (TextView)v;
                String text = ""+tv.getText();
                if ("".equals(text)) {
                    tv.setText("+1");
                } else {
                    tv.setText( "+" + ( Integer.parseInt(text.substring(1)) + 1 ) );
                }
            }
        }
    }

    public void subtractDrink(View view) {
        Button b = (Button)view;
        ViewGroup row = (ViewGroup)view.getParent();
        for (int i=0; i<row.getChildCount(); i++) {
            View v = row.getChildAt(i);
            if (v instanceof TextView && v.getId() == 2131034113) {
                TextView tv = (TextView)v;
                String text = ""+tv.getText();
                if ("+1".equals(text)) {
                    tv.setText("");
                } else if (!"".equals(text)){
                    tv.setText( "+" + ( Integer.parseInt(text.substring(1)) - 1 ) );
                }
            }
        }
    }

}

class CustomComparator implements Comparator<HashMap> {
    @Override
    public int compare(HashMap m1, HashMap m2) {
        return m1.get("participant").toString().compareToIgnoreCase(m2.get("participant").toString());
    }
}
