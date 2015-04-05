package kymj.jobsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public MXAccountManager acctMgr;
    String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_job);

        Intent intent = getIntent();
        jobId = intent.getStringExtra(GetActivity.GetActivityJobId);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Job");
        query.whereEqualTo("objectId", jobId);

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

    public void startChat(View view){

        final MXChatManager conversationMgr = MXChatManager.getInstance();

        final List<String> uniqueIds = new ArrayList();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Job");
        query.whereEqualTo("objectId", jobId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                    query2.whereEqualTo("objectId", object.getParseObject("user").getObjectId());

                    query2.getFirstInBackground(new GetCallback<ParseUser>() {

                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                Log.e("inviting is happening", "invites");
                                 //list of ids to invite
                                uniqueIds.add(parseUser.get("username").toString());
                                try {
                                    conversationMgr.createChat(((TextView)findViewById(R.id.TitleText)).getText().toString(), uniqueIds, new MXChatManager.OnCreateChatListener() {
                                        @Override
                                        public void onCreateChatSuccess(final String binderID) {
                                            Log.d("YAAAAY created", "onCreateChatSuccess(), binderID = " + binderID);
                                            final MXChatManager.OnInviteListener callback = new MXChatManager.OnInviteListener() {
                                                @Override
                                                public void onInviteSuccess() {
                                                    Toast.makeText(AcceptedJobActivity.this, "Invite Successfully", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onInviteFailed(int errorCode, String message) {
                                                    Toast.makeText(AcceptedJobActivity.this, "Invite Failed. Error: " + message + " " + errorCode, Toast.LENGTH_LONG).show();
                                                }
                                            };
                                        }

                                        @Override
                                        public void onCreateChatFailed(int errorCode, String message) {
                                            Log.d("Noooo failure", "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
                                        }
                                    });
                                } catch (MXException.AccountManagerIsNotValid t) {
                                    t.printStackTrace();
                                }
                                //MXChatManager.getInstance().inviteByUniqueIds(binderID, uniqueIds, callback);
                            } else {
                                Log.e("CAN'T FIND owner", "TT_TT");
                            }


                        }
                    });
                }
            }
        });

       // uniqueIds.add("yashwanth");
//        List<String> emptyList = new ArrayList<>();
//        try {
//            conversationMgr.createChat(((TextView)findViewById(R.id.TitleText)).getText().toString(), uniqueIds, new MXChatManager.OnCreateChatListener() {
//                @Override
//                public void onCreateChatSuccess(final String binderID) {
//                    Log.d("YAAAAY created", "onCreateChatSuccess(), binderID = " + binderID);
//                    final MXChatManager.OnInviteListener callback = new MXChatManager.OnInviteListener() {
//                        @Override
//                        public void onInviteSuccess() {
//                            Toast.makeText(AcceptedJobActivity.this, "Invite Successfully", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onInviteFailed(int errorCode, String message) {
//                            Toast.makeText(AcceptedJobActivity.this, "Invite Failed. Error: " + message + " " + errorCode, Toast.LENGTH_LONG).show();
//                        }
//                    };
//                }
//
//                @Override
//                public void onCreateChatFailed(int errorCode, String message) {
//                    Log.d("Noooo failure", "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
//                }
//            });
//        } catch (MXException.AccountManagerIsNotValid e) {
//            e.printStackTrace();
//        }

    }
}
