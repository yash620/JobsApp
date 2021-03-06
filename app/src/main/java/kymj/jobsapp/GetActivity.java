package kymj.jobsapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.util.Date;


public class GetActivity extends ActionBarSignOutActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private JobLocationAdapter jobAdapter;
    private ListView listView;

    private JobAcceptorAdapter jobAcceptedAdapter;
    private ListView listAcceptedView;
    public static final String GetActivityJobId = "kymj.jobsapp.job_id";

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        System.err.print("go");

        GoogleApiClient mGoogleApiClient;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }

    public void onStart(){
        super.onStart();
        jobAcceptedAdapter = new JobAcceptorAdapter(this);
        jobAdapter = new JobLocationAdapter(this, mLastLocation);

        listAcceptedView = (ListView) findViewById(R.id.list_accepted_id);
        listAcceptedView.setAdapter(jobAcceptedAdapter);

        listView = (ListView) findViewById(R.id.list_view_id);
        listView.setAdapter(jobAdapter);

        //adding in click recognition for list view items
        jobAcceptedAdapter.loadObjects();
        jobAdapter.loadObjects();
        Log.d("load", "hello");
        final Activity me = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject job = ((JobLocationAdapter)parent.getAdapter()).getItem(position);
                String jobId = job.getObjectId();
                Intent jobIntent = new Intent(me, UnacceptedJobActivity.class);
                jobIntent.putExtra(GetActivityJobId, jobId);
                startActivity(jobIntent);

            }
        });

        listAcceptedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject job = ((JobAcceptorAdapter)parent.getAdapter()).getItem(position);
                String jobId = job.getObjectId();
                Intent jobIntent = new Intent(me, AcceptedJobActivity.class);
                jobIntent.putExtra(GetActivityJobId, jobId);
                startActivity(jobIntent);

            }
        });
    }

    protected void signUpMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mRequestingLocationUpdates = true;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        onStart();
    }

}
