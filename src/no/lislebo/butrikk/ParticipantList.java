package no.lislebo.butrikk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class ParticipantList extends ExpandableListActivity {

    private int n_participants;
    private final String filename = "deltakere.csv";
    private final String path = "/storage/sdcard1/Documents";
    private HashMap<String,int[]> drinkMap = new HashMap<String,int[]>();
    private List groupList;

    public void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_list);
        groupList = createGroupList();
            SimpleExpandableListAdapter expListAdapter =
                new SimpleExpandableListAdapter(
                                                this,
                                                groupList,
                                                R.layout.group_row,
                                                new String[] { "participant" },
                                                new int[] { R.id.row_name },
                                                createChildList(),
                                                R.layout.child_row,
                                                new String[] {"drink"},
                                                new int[] { R.id.grp_child}
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
                int[] p_drinks = {Integer.parseInt(p[1]),Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5])};
                drinkMap.put(p[0], p_drinks);
                n_participants++;
                result.add(m);
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
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
                    secList.add( child );
                }
            }
            result.add( secList );
        }
        return result;
    }
    public void  onContentChanged  () {
        System.out.println("onContentChanged");
        super.onContentChanged();
    }

    public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,int childPosition,long id) {
        System.out.println("Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
        return true;
    }

    public void  onGroupExpand  (int groupPosition) {
        try{
            System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
        }catch(Exception e){
            System.out.println(" groupPosition Errrr +++ " + e.getMessage());
        }
    }
}
