package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class JobsConfirmedListActivity extends Activity {


    public final String applicationKEY = "eS4klh1woY6Qw2CnmhMilJZx6CPn18m9TkmZTztB";
    public final String clientKEY = "2KbPt3cj7kmsYTDRJImiaPsAgXb8l83YRkewH3lv";
    ListView listView;
    volatile ArrayList<Double> bounds;
    private JobListAdapter adapter;
    ArrayList<Job> jobList = new ArrayList<Job>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.job_list_layout);


        listView =  (ListView) findViewById(R.id.job_list_lv);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

               /*
                    Display a Dialog with Phone number
                */

            }
        });



        adapter = new JobListAdapter(this,R.layout.job_card,jobList );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.v("onCreate", "Starting AsyncTask");


        new DownloadDataTask().execute();
    }

    public class DownloadDataTask extends AsyncTask<String, Void, Integer> {

        private final int NO_CONNECTION = 2;
        private final int SUCCESSFULL = 1;
        private final int UNSUCCESSFUL = 0;

        @Override
        protected void onPreExecute() {
            adapter.clear();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Log.v("AsyncTask", "Loading Data");

            //fetchBounds(String.valueOf(51.5248), String.valueOf(0.1336),2);

            if(checkNetworkConnection() && isOnline()) {
                List<ParseObject> parseObjects = fetchFeed();

                if (parseObjects == null){
                    return UNSUCCESSFUL; //"Failed to fetch data!";
                }

                //Converting Parse objects into Jobs Object
                for (int i = 0; i < parseObjects.size(); i++){

                    ParseObject parseObject = parseObjects.get(i);

                    String jobTitle = "";
                    String jobDescription = "";
                    String jobID ="";
                    String locationLat ="";
                    String locationLon ="";
                    String category ="";

                    if(parseObject.containsKey("Job_Title")) {
                        jobTitle = parseObject.get("Job_Title").toString();

                    }

                    if(parseObject.containsKey("category")) {
                        jobTitle = parseObject.get("category").toString();

                    }

                    if(parseObject.containsKey("Job_Description")) {
                        jobDescription = parseObject.get("Job_Description").toString();

                    }

                    if(parseObject.containsKey("jobId")) {
                        jobID = parseObject.get("jobId").toString();

                    }

                    if(parseObject.containsKey("latitude")) {
                        locationLat = parseObject.get("latitude").toString();

                    }

                    if(parseObject.containsKey("longitude")) {
                        locationLon = parseObject.get("longitude").toString();

                    }

                    if (parseObject.containsKey("category")) {
                        category = parseObject.get("category").toString();

                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JobsConfirmedListActivity.this);
                    String category_preference = preferences.getString("category","");
                    int distance_preference = preferences.getInt("distance",0);

                    Double distance;
                    try {
                        distance = GPSCoordinates.calculateDistanceBetweenCurrentLocationAndCoordinates(
                                JobsConfirmedListActivity.this, Double.valueOf(locationLat),
                                Double.valueOf(locationLon));
                    }catch (NumberFormatException e){
                        distance = 0.0;
                    }
                    if (category_preference == "" && distance_preference == 0){
                        jobList.add(new Job(jobID,jobTitle,jobDescription,category,locationLat,locationLon));
                    }else{
                        if (category_preference == category && distance < distance_preference){
                            jobList.add(new Job(jobID,jobTitle,jobDescription,category,locationLat,locationLon));
                        }
                    }

                }

            }else {
                return NO_CONNECTION;
            }



            return SUCCESSFULL;
        }



        @Override
        protected void onPostExecute(Integer result) {
            setProgressBarIndeterminateVisibility(false);
            /* Download complete. Lets update UI */

            adapter.notifyDataSetChanged();

            Log.v("AsyncTask", "Done");

            if (result == SUCCESSFULL) {

                adapter.notifyDataSetChanged();

            } else {
                switch (result){
                    case UNSUCCESSFUL:
                        Toast.makeText(JobsConfirmedListActivity.this, "Problem connecting to Server",Toast.LENGTH_SHORT).show();
                        break;

                    case NO_CONNECTION:
                        Toast.makeText(JobsConfirmedListActivity.this, "No Internet Connection",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }
    }

    private List<ParseObject> fetchFeed() {

        //fetch items from parse

        List<ParseObject> parseList;
        return null;

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
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_filter) {

            FilterResultsDialog filterView=new FilterResultsDialog(JobsConfirmedListActivity.this);
            filterView.setOnDismissListener( new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    new DownloadDataTask().execute();
                }
            });
            filterView.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
