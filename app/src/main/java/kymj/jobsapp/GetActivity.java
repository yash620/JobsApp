package kymj.jobsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;


public class GetActivity extends Activity{

    private JobAdapter jobAdapter;
    private ListView listView;
    public static final String GetActivityJobId = "kymj.jobsapp.job_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        System.err.print("go");
        jobAdapter = new JobAdapter(this);

        listView = (ListView) findViewById(R.id.list_view_id);
        listView.setAdapter(jobAdapter);

        //adding in click recognition for list view items

        jobAdapter.loadObjects();
        System.err.print("load");
        final Activity me = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Printing onItem clic", parent.toString());
                Log.e("Printing view", view.toString());
                Log.e("Printng position", new Integer(position).toString());
                Log.e("Printing id", new Long(id).toString());
                ParseObject job = ((JobAdapter)parent.getAdapter()).getItem(position);
                String jobId = job.getObjectId();
                Intent jobIntent = new Intent(me, UnacceptedJobActivity.class);
                jobIntent.putExtra(GetActivityJobId, jobId);
                startActivity(jobIntent);



            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get, menu);
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

    protected void signUpMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
