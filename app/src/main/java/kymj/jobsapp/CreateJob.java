package kymj.jobsapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;


public class CreateJob extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        View rootView = inflater.inflate(R.layout.fragment_create_job, container, false);

        Button createButton = (Button) rootView.findViewById(R.id.CreateFragmentButton);
        createButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                createJob(v);
            }
        });

        //FragmentManager.findFragmentById(R.id.map);
        return rootView;
    }

    public void createJob(View v) {
        String title = ((EditText) v.findViewById(R.id.TitleEdit)).getText().toString();
        String description = ((EditText) v.findViewById(R.id.DescriptionEdit)).getText().toString();
        int money = Integer.parseInt(((EditText) v.findViewById(R.id.MoneyEdit)).getText().toString());
        if (title.length() <= 0 || description.length() <= 0 || money < 0) {
            signUpMsg("Fields empty");
            return;
        }
        ParseObject job = new ParseObject("Job");
        job.put("title", title);
        job.put("description", description);
        job.put("money", money);
        job.put("user", ParseUser.getCurrentUser());
   //     job.put("location", location);

    }

    protected void signUpMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
