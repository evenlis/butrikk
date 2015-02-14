package no.lislebo.butrikk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class ParticipantList extends ExpandableListActivity {

    private int n_participants;

    public void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_list);

            SimpleExpandableListAdapter expListAdapter =
                new SimpleExpandableListAdapter(
                                                this,
                                                createGroupList(),
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
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            BufferedReader reader = new BufferedReader( new FileReader( new File("deltakere.csv") ) );
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            n_participants = 0;
            String line = "";
            while (!line.equals("-1")) {
                line = reader.readLine();
                String[] participant = line.split(",");
                HashMap m = new HashMap();
                Toast.makeText(this, participant[0], Toast.LENGTH_SHORT).show();
                m.put("participant", participant[0]);
                n_participants++;
            }
            reader.close();
        } catch (IOException e) {

        }
        return (List)result;
    }

    private List createChildList() {
        ArrayList result = new ArrayList();
        for( int i = 0 ; i < n_participants ; ++i ) {
            ArrayList secList = new ArrayList();
            for( int n = 0 ; n < 3 ; n++ ) {
                HashMap child = new HashMap();
                child.put( "Sub Item", "Flesk " + n );
                secList.add( child );
            }
            result.add( secList );
        }
        return result;
    }
    public void  onContentChanged  () {
        System.out.println("onContentChanged");
        super.onContentChanged();
    }
    /* This function is called on each child click */
    public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,int childPosition,long id) {
        System.out.println("Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
        return true;
    }

    /* This function is called on expansion of the group */
    public void  onGroupExpand  (int groupPosition) {
        try{
            System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
        }catch(Exception e){
            System.out.println(" groupPosition Errrr +++ " + e.getMessage());
        }
    }
}
