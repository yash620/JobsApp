package kymj.jobsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXSDKException;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.Date;

import static com.moxtra.sdk.MXSDKConfig.MXProfileInfo;
import static com.moxtra.sdk.MXSDKConfig.MXUserIdentityType;
import static com.moxtra.sdk.MXSDKConfig.MXUserInfo;


public class AcceptedJobActivity extends ActionBarSignOutActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //TODO set fields by getting job with its id.
    GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;
    Location mLastLocation;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = false;
    Location jobLocation;
    public MXAccountManager mAcctMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_job);

        Intent intent = getIntent();
        String jobId = intent.getStringExtra(GetActivity.GetActivityJobId);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Job");
        query.whereEqualTo("objectId", jobId);

        try {
            mAcctMgr = MXAccountManager.createInstance(this);
        } catch (MXSDKException.InvalidParameter invalidParameter) {
            invalidParameter.printStackTrace();
        }
        Log.d("SDFfsddfs","sdffdssdf");
        ParseUser user = ParseUser.getCurrentUser();
        MXUserInfo userInfo = new MXUserInfo(user.getUsername() , MXUserIdentityType.IdentityUniqueId);
        Bitmap bmpAvatar = BitmapFactory.decodeFile("../../res/mipmap-mpdpi/ic_launcher.png");
        Log.d("sdffdsfds","dsffdsdsf");
        MXProfileInfo profile = new MXProfileInfo("John", "Doe",bmpAvatar);
        mAcctMgr.setupUser(userInfo, profile ,null, new MXAccountManager.MXAccountLinkListener(){
            @Override
            public void onLinkAccountDone(boolean bSuccess){
                // Do something in the callback.
                Log.d("Hello","fsdfsdf");
            }
        });

        MXChatManager conversationMgr = MXChatManager.getInstance();
        Log.d("Hello","Hello");
        try {
            MXChatManager.getInstance().createChat(new MXChatManager.OnCreateChatListener() {
                @Override
                public void onCreateChatSuccess(String binderID) {

                    Log.d("Testerinosss", "onCreateChatSuccess(), binderID = " + binderID);
                }
                @Override
                public void onCreateChatFailed(int errorCode, String message) {
                    Log.d("Testerinos", "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
                }
            });
        } catch (MXException.AccountManagerIsNotValid e) {
            e.printStackTrace();
        }




        MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // Enabling MyLocation Layer of Google Map


        googleMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    ((TextView)findViewById(R.id.TitleText)).setText(object.get("title").toString());
                    ((TextView)findViewById(R.id.DescriptionText)).setText(object.get("description").toString());
                    ((TextView)findViewById(R.id.MoneyText)).setText(object.get("money").toString());
                    ParseGeoPoint jobLoc = ((ParseGeoPoint)object.get("location"));
                    jobLocation = new Location("jobLocation");
                    jobLocation.setLatitude(jobLoc.getLatitude());
                    jobLocation.setLongitude(jobLoc.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(jobLocation.getLatitude(), jobLocation.getLongitude()))
                            .title("Job Is Here"));
                }
            }
        });
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

    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(jobLocation.getLatitude(), jobLocation.getLongitude()))
                .title("Job Is Here"));
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
}
