package kymj.jobsapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class UserAcceptedJobActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accepted_job);

        Intent intent = getIntent();
        String jobId = intent.getStringExtra(MyJobsCreate.MyJobsCreateJobId);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Job");
        query.whereEqualTo("objectId", jobId);
        final View layout = findViewById(R.id.activity_user_accepted_job_layout);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    Log.e("Stuff happening", "this shit is actually happening");
                    ((TextView)findViewById(R.id.TitleText)).setText(object.get("title").toString());
                    ((TextView)findViewById(R.id.TitleText)).invalidate();
                    ((TextView)findViewById(R.id.DescriptionText)).setText(object.get("description").toString());
                    ((TextView)findViewById(R.id.DescriptionText)).invalidate();
                    ((TextView)findViewById(R.id.MoneyText)).setText(object.get("money").toString());
                    ((TextView)findViewById(R.id.MoneyText)).invalidate();

                    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                    query2.whereEqualTo("objectId", object.getParseObject("acceptor").getObjectId());

                    query2.getFirstInBackground(new GetCallback<ParseUser>() {

                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null){
                                ((TextView)findViewById(R.id.name_text)).setText(parseUser.getString("name"));
                                ((TextView)findViewById(R.id.name_text)).invalidate();
                                ((TextView)findViewById(R.id.user_name_text)).setText(parseUser.get("username").toString());
                                ((TextView)findViewById(R.id.user_name_text)).invalidate();
                            }
                            else
                            {
                                Log.e("CAN'T FIND ACCEPTOR", "TT_TT");
                            }
                        }
                    });

////                    ((TextView)findViewById(R.id.name_text)).setText((object.getParseUser("acceptor")).getString("name"));
////                    ((TextView)findViewById(R.id.name_text)).invalidate();
//                    ((TextView)findViewById(R.id.user_name_text)).setText((object.getParseUser("acceptor")).get("username").toString());
//                    ((TextView)findViewById(R.id.user_name_text)).invalidate();

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_accepter_job, menu);
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
}
