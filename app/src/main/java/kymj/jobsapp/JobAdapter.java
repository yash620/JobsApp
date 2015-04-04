package kymj.jobsapp;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by marcof on 4/4/15.
 */
public class JobAdapter extends ParseQueryAdapter<ParseObject> {

    public JobAdapter(Context context, final int side) {
    // Use the QueryFactory to construct a PQA that will only show
    // Todos marked as high-pri

    //side = 1 means from the user perspective
    //side = 0 means from the acceptor perspective


        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Job");
                if(side == 0)
                {
                    query.whereEqualTo("user",ParseUser.getCurrentUser());
                }
                else if( side == 1)
                {
                    query.whereEqualTo("acceptor",ParseUser.getCurrentUser());
                }
                //query.setLimit(15);
                return query;
            }
        });
    }


    public JobAdapter(Context context, final Location loc) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri


        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseGeoPoint userLocation;
                if(loc != null){
                    userLocation = new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
                }
                else
                    userLocation = new ParseGeoPoint(34.0689,-118.4451);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Job");
                query.whereWithinMiles("location", userLocation, 10.0);
                query.whereNotEqualTo("user", ParseUser.getCurrentUser());
                query.setLimit(15);
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.job_list_layout, null);
        }
        System.err.print(object.getString("title"));
        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        titleTextView.setText(object.getString("title"));
        // Add a reminder of how long this item has been outstanding
        TextView descView = (TextView) v.findViewById(R.id.description);
        descView.setText(object.getString("description"));

        TextView cost = (TextView) v.findViewById(R.id.cost);
        cost.setText("$"+Integer.toString(object.getInt("money")));
        return v;
    }

}
