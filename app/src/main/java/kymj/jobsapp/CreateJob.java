package kymj.jobsapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.Date;


public class CreateJob extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;
    Location mLastLocation;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateJob.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateJob newInstance(String param1, String param2) {
        CreateJob fragment = new CreateJob();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateJob() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_job, container, false);

        Button createButton = (Button) rootView.findViewById(R.id.CreateFragmentButton);
        createButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                createJob(v);
            }
        });
       // ImageView img = (ImageView) rootView.findViewById(R.id.target_image);
       // img.setImageResource(R.drawable.dot);
        //FragmentManager.findFragmentById(R.id.map);
        return rootView;
    }

    public void onStart(){
        super.onStart();


        MapFragment fm = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        rootView.invalidate();
    }

    public void createJob(View v) {
        String title = ((EditText) rootView.findViewById(R.id.TitleEdit)).getText().toString();
        String description = ((EditText) rootView.findViewById(R.id.DescriptionEdit)).getText().toString();
        int money = 0;
        try {
            money = Integer.parseInt(((EditText) rootView.findViewById(R.id.MoneyEdit)).getText().toString());
        }
        catch(Exception e) {
            signUpMsg("Money is not int");
            return;
        }
        if (title.length() <= 0 || description.length() <= 0 || money < 0) {
            signUpMsg("Fields empty");
            return;
        }
        ParseObject job = new ParseObject("Job");
        job.put("title", title);
        job.put("description", description);
        job.put("money", money);
        job.put("user", ParseUser.getCurrentUser());
        LatLng requestLoc = googleMap.getCameraPosition().target;
        ParseGeoPoint geoPoint = new ParseGeoPoint(requestLoc.latitude, requestLoc.longitude);
        job.put("location", geoPoint);
        job.saveInBackground();

        getActivity().finish();


    }

    protected void signUpMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        rootView.invalidate();
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
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        rootView.invalidate();
    }
}
