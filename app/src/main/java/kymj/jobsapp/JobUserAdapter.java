package kymj.jobsapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by marcof on 4/4/15.
 */
public class JobUserAdapter extends ParseQueryAdapter<ParseObject> {

    public JobUserAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri


        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Job");
                query.whereEqualTo("user", ParseUser.getCurrentUser());

                //query.setLimit(15);
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
        //titleTextView.setTextColor(Color.GREEN);

        TextView descView = (TextView) v.findViewById(R.id.description);
        descView.setText(object.getString("description"));

        TextView cost = (TextView) v.findViewById(R.id.cost);
        cost.setText("$"+Integer.toString(object.getInt("money")));
        //return v;*/
        return v;
    }
}
