package kymj.jobsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by marcof on 4/4/15.
 */
public class SignupActivity extends Activity {

    // Random change
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "qCOUDggMQqCb743STldjmYzLwFqaWNwYg62okRK8", "fgm7HsfsqGpsXxYzbAua7u77oRoslTJGoTIdJ6Qg");
    }


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
    public void signup(View view){
        String username = ((EditText)findViewById(R.id.editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();

        System.err.print("username");
        System.err.print("password");
        if(password.length() <= 0)
        {
            signUpMsg("Use a password");
            return;
        }
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
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

    }
    public void back_to_login(View view){
        Intent in = new Intent(this, MainActivity.class);
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