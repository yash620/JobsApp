package kymj.jobsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;


public class MyJobsCreate extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyJobsCreateJobId = "kymj.jobsapp.myjobscreatejobid";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyJobs.
     */
    // TODO: Rename and change types and number of parameters
    public static MyJobsCreate newInstance(String param1, String param2) {
        MyJobsCreate fragment = new MyJobsCreate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyJobsCreate() {
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
        // Inflate the layout for this fragmentlistView = (ListView) findViewById(R.id.list_view_id);

        View rootView =  inflater.inflate(R.layout.fragment_my_jobs, container, false);
        JobUserAdapter jobAdapter = new JobUserAdapter(getActivity());
        ListView listView = (ListView) rootView.findViewById(R.id.my_jobs_create_list_view);
        listView.setAdapter(jobAdapter);

        jobAdapter.loadObjects();
        System.err.print("load");

        Activity me = getActivity();
        //adding in click recognition for list view items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject job = ((JobUserAdapter)parent.getAdapter()).getItem(position);
                String jobId = job.getObjectId();
                Intent jobIntent = new Intent(getActivity(), UserAcceptedJobActivity.class);
                jobIntent.putExtra(MyJobsCreateJobId, jobId);
                startActivity(jobIntent);

            }
        });

        return rootView;

    }


}
