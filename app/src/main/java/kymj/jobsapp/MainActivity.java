package kymj.jobsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXSDKConfig;
import com.moxtra.sdk.MXSDKException;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    private MXAccountManager mAccountMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        Parse.initialize(this, "qCOUDggMQqCb743STldjmYzLwFqaWNwYg62okRK8", "fgm7HsfsqGpsXxYzbAua7u77oRoslTJGoTIdJ6Qg");
        final ParseUser currentUser = ParseUser.getCurrentUser();

        mAccountMgr = ((MoxtraApplication) super.getApplication()).getAccountMgr();
//        mAccountMgr.unlinkAccount(new MXAccountManager.MXAccountUnlinkListener() {
//            @Override
//            public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {
//
//            }
//        });
        if (mAccountMgr.isLinked()) {
            Log.e("user logged in","asas");
            if (currentUser != null) {
                Intent in = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(in);
                finish();
            }
        }
        else{
            if(currentUser != null) {
                MXSDKConfig.MXUserInfo userInfo = new MXSDKConfig.MXUserInfo(currentUser.get("username").toString(), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                Bitmap bmpAvatar = BitmapFactory.decodeFile("../../res/mipmap-mpdpi/ic_launcher.png");
                MXSDKConfig.MXProfileInfo profile = new MXSDKConfig.MXProfileInfo(currentUser.get("name").toString(), null, bmpAvatar);
                mAccountMgr.setupUser(userInfo, profile, null, new MXAccountManager.MXAccountLinkListener() {
                    @Override
                    public void onLinkAccountDone(boolean bSuccess) {
                        // Do something in the callback.
                        Log.e("Hello", "fsdfsdf");
                        if (currentUser != null) {
                            Intent in = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(in);
                            finish();
                        }
                    }
                });
            }
        }

    }
   // }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /*
     Called when the user clicks login button

     */
    public void login(View view){
        String username = ((EditText)findViewById(R.id.editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();

        System.err.print("username");
        System.err.print("password");


        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    MXSDKConfig.MXUserInfo userInfo = new MXSDKConfig.MXUserInfo(user.get("username").toString(), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                    Bitmap bmpAvatar = BitmapFactory.decodeFile("../../res/mipmap-mpdpi/ic_launcher.png");
                    MXSDKConfig.MXProfileInfo profile = new MXSDKConfig.MXProfileInfo(user.get("name").toString(), null, bmpAvatar);
                    mAccountMgr.setupUser(userInfo, profile, null, new MXAccountManager.MXAccountLinkListener() {
                                @Override
                                public void onLinkAccountDone(boolean b) {

                                }
                    });
                    Intent in = new Intent(getApplicationContext(), LandingActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    System.err.print("Error");
                    signUpMsg("Login Error.");
                }
            }
        });

    }

    public void go_signup(View view){
        Intent in = new Intent(this, SignupActivity.class);
        startActivity(in);
    }
    /*public void signup(View view){
        user.logInInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    signUpMsg("Account Created Successfully");
                    Intent in = new Intent(getApplicationContext(), LandingActivity.class);
                    startActivity(in);
                } else {
                    signUpMsg("Account Error.");
                }
            }
        });
    }*/

    protected void signUpMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
