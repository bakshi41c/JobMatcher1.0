package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ApplicantDetailsActivity extends Activity {

    public final String applicationKEY = "eS4klh1woY6Qw2CnmhMilJZx6CPn18m9TkmZTztB";
    public final String clientKEY = "2KbPt3cj7kmsYTDRJImiaPsAgXb8l83YRkewH3lv";

    TextView job_title_tv ;
    TextView job_category_tv;

    ImageView job_image_iv;

    Button map_btn;
    Button apply_btn;

    String applicantID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile_layout);

        setTitle("Applicant Details");

        Intent intent = getIntent();
        applicantID = intent.getStringExtra("Id");

        job_title_tv = (TextView)findViewById(R.id.offer_job_title_tv);
        job_category_tv = (TextView)findViewById(R.id.info_tv);


        job_image_iv = (ImageView)findViewById(R.id.job_image_iv);

        map_btn = (Button)findViewById(R.id.maps_btn);
        apply_btn = (Button)findViewById(R.id.apply_btn);


        new DownloadJobDataTask().execute();


    }

    public void onClick(View view) throws InterruptedException {
        switch(view.getId()){


            case R.id.offer_btn:
                Thread.sleep(500);
                Toast.makeText(this, "Application Offered Successfully", Toast.LENGTH_LONG).show();
                break;

        }
    }

    public class DownloadJobDataTask extends AsyncTask<String, Void, Applicant> {

        private final int NO_CONNECTION = 2;
        private final int SUCCESSFULL = 1;
        private final int UNSUCCESSFUL = 0;

        @Override
        protected void onPreExecute() {

            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Applicant doInBackground(String... params) {

            Log.v("AsyncTask", "Loading Data");

            if(checkNetworkConnection() && isOnline()) {

                List<ParseObject> applicantDetailsObject = fetchApplicantDetails(applicantID);

                    ParseObject applicant = applicantDetailsObject.get(0);
                    System.out.println();

                    String applicantName = applicant.get("User_Name").toString();
                    String applicantStatement = " ";
                    String applicantID = applicant.get("User_ID").toString();
                    String applicantPhone = applicant.get("Mobile_Number").toString();



                return new Applicant(applicantID,applicantName,applicantStatement,applicantPhone);






            }else {
                return null;
            }
        }



        @Override
        protected void onPostExecute(Applicant applicant) {
            setProgressBarIndeterminateVisibility(false);
            /* Download complete. Lets update UI */


            Log.v("AsyncTask", "Done");

            job_title_tv.setText(applicant.getName());
            job_category_tv.setText(applicant.getPhone());


        }
    }



    private List<ParseObject> fetchApplicantDetails(String applicantID) {

        //fetch applicants for a job

        List<ParseObject> parseList = null;



        Log.v("ApplicantListActivity", "Fetching Applicant Details");

        Parse.initialize(this, applicationKEY, clientKEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        //TODO: Change the query
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("User");
        query.whereEqualTo("User_ID", applicantID);

        try {
            parseList = query.find();
            //Log.v("Applicants", parseList.get(0).toString());
            return parseList;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean checkNetworkConnection() {
        // TODO Auto-generated method stub
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isOnline() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
