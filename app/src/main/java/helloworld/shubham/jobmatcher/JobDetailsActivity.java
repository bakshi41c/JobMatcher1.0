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


public class JobDetailsActivity extends Activity {

    public final String applicationKEY = "eS4klh1woY6Qw2CnmhMilJZx6CPn18m9TkmZTztB";
    public final String clientKEY = "2KbPt3cj7kmsYTDRJImiaPsAgXb8l83YRkewH3lv";

    TextView job_title_tv ;
    TextView job_category_tv;
    TextView distance_tv;
    TextView description_tv;
    TextView description_label_tv;
    TextView distance_label_tv;

    ImageView job_image_iv;

    Job mJob;

    Button map_btn;
    Button apply_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details_layout);

        setTitle("Job Details");

        Intent intent = getIntent();
        String id = intent.getStringExtra("Id");

        job_title_tv = (TextView)findViewById(R.id.job_title_tv);
        job_category_tv = (TextView)findViewById(R.id.job_category_tv);
        distance_tv = (TextView)findViewById(R.id.distance_tv);
        description_tv = (TextView)findViewById(R.id.description_tv);
        distance_label_tv = (TextView)findViewById(R.id.distance_label_tv);
        description_label_tv = (TextView)findViewById(R.id.description_label_tv);

        job_image_iv = (ImageView)findViewById(R.id.job_image_iv);

        map_btn = (Button)findViewById(R.id.maps_btn);
        apply_btn = (Button)findViewById(R.id.apply_btn);


        new DownloadJobDataTask().execute(id);


    }

    public void btnOnClick(View view){
        switch(view.getId()){

            case R.id.maps_btn:

                if (mJob != null){
                    String geoUri = "http://maps.google.com/maps?q=loc:" + mJob.getLocationLat() + ","
                            +  mJob.getLocationLon() + " (" + "Job" + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    startActivity(intent);
                }

                break;

            case R.id.apply_btn:
                try {
                    Thread.sleep(500);
                    Toast.makeText(getApplicationContext(),"Application submitted",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public class DownloadJobDataTask extends AsyncTask<String, Void, Job> {

        private final int NO_CONNECTION = 2;
        private final int SUCCESSFULL = 1;
        private final int UNSUCCESSFUL = 0;

        @Override
        protected void onPreExecute() {

            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Job doInBackground(String... params) {

            Log.v("AsyncTask", "Loading Data");

            Job job;

            String jobTitle = "";
            String jobDescription = "";
            String jobID = "";
            String locationLan = "";
            String locationLon = "";
            String category = "";

            if(checkNetworkConnection() && isOnline()) {
                List<ParseObject> parseObjects = fetchJobFeedUsingID(params[0]);

                if (parseObjects == null){
                    return null; //"Failed to fetch data!";
                }



                ParseObject parseObject = parseObjects.get(0);



                if (parseObject.containsKey("Job_Title")) {
                    jobTitle = parseObject.get("Job_Title").toString();

                }

                if (parseObject.containsKey("Job_Description")) {
                    jobDescription = parseObject.get("Job_Description").toString();

                }

                if (parseObject.containsKey("jobId")) {
                    jobID = parseObject.get("jobId").toString();

                }

                if (parseObject.containsKey("latitude")) {
                    locationLan = parseObject.get("latitude").toString();

                }

                if (parseObject.containsKey("longitude")) {
                    locationLon = parseObject.get("longitude").toString();

                }

                if (parseObject.containsKey("category")) {
                    category = parseObject.get("category").toString();

                }


            }else {
                return null;
            }



            return new Job(jobID,jobTitle,jobDescription,category,locationLan,locationLon);
        }



        @Override
        protected void onPostExecute(Job job) {
            setProgressBarIndeterminateVisibility(false);
            /* Download complete. Lets update UI */


            Log.v("AsyncTask", "Done");

            if (job != null) {

                mJob = job;

                job_title_tv.setText(job.getTitle());
                job_category_tv.setText(job.getCategory());
                description_tv.setText(job.getDescription());

                Double distance = GPSCoordinates.calculateDistanceBetweenCurrentLocationAndCoordinates(
                        JobDetailsActivity.this, Double.valueOf(job.getLocationLat()),
                        Double.valueOf(job.getLocationLon()));


                distance_tv.setText(String.valueOf(Math.round(distance)) + " km");
                distance_tv.setTextColor(Color.parseColor("#000000"));
                description_tv.setTextColor(Color.parseColor("#000000"));

                ArrayList<String> categories = new ArrayList( Arrays.asList(getResources()
                        .getStringArray(R.array.categories_array)) );
                ArrayList<String> colors = new ArrayList( Arrays.asList(getResources()
                        .getStringArray(R.array.categories_colors)) );



                int i = categories.indexOf(job.getCategory());

                if (i < 0){
                    i = 8;
                }

                job_image_iv.setBackgroundColor(Color.parseColor(colors.get(i)));
                description_label_tv.setTextColor(Color.parseColor(colors.get(i)));
                distance_label_tv.setTextColor(Color.parseColor(colors.get(i)));
                job_title_tv.setTextColor(Color.parseColor(colors.get(i)));
                map_btn.setTextColor(Color.parseColor(colors.get(i)));
                apply_btn.setTextColor(Color.parseColor(colors.get(i)));
                job_category_tv.setTextColor(Color.parseColor(colors.get(i)));

                switch(i){
                    case 0:
                        job_image_iv.setImageResource(R.drawable.icon_charity_volunteering);
                        break;

                    case 1:
                        job_image_iv.setImageResource(R.drawable.icon_education_teaching);
                        break;

                    case 2:
                        job_image_iv.setImageResource(R.drawable.icon_call_centre);
                        break;

                    case 3:
                        job_image_iv.setImageResource(R.drawable.icon_sales);
                        break;

                    case 4:
                        job_image_iv.setImageResource(R.drawable.icon_repairs);
                        break;

                    case 5:
                        job_image_iv.setImageResource(R.drawable.icon_personal_care);
                        break;

                    case 6:
                        job_image_iv.setImageResource(R.drawable.icon_gardening);
                        break;

                    case 7:
                        job_image_iv.setImageResource(R.drawable.icon_housekeeping);
                        break;

                    case 8:
                        job_image_iv.setImageResource(R.drawable.icon_other);
                        break;
                }

                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(colors.get(i))));

            } else {
                Toast.makeText(JobDetailsActivity.this, "Problem connecting to Server", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private List<ParseObject> fetchJobFeedUsingID(String Id) {

        //fetch items from parse

        List<ParseObject> parseList;

        //getBounds()

        Parse.initialize(this, applicationKEY, clientKEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("PostingJob");
        query.whereEqualTo("jobId",Id);
        query.orderByDescending("_created_at");
        try {
            parseList = query.find();
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
