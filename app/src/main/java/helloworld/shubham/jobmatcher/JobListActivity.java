package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class JobListActivity extends Activity {


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

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

               Intent intent = new Intent(JobListActivity.this, JobDetailsActivity.class);
               intent.putExtra("Id", jobList.get(position).getJobId());
               startActivity(intent);
            }
        });

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(adapter.getItem(position));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

        /*
        recyclerView = (RecyclerView)findViewById(R.id.job_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        */

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

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JobListActivity.this);
                    String category_preference = preferences.getString("category","");
                    int distance_preference = preferences.getInt("distance",0);

                    Double distance;
                    try {
                        distance = GPSCoordinates.calculateDistanceBetweenCurrentLocationAndCoordinates(
                                JobListActivity.this, Double.valueOf(locationLat),
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
                        Toast.makeText(JobListActivity.this, "Problem connecting to Server",Toast.LENGTH_SHORT).show();
                        break;

                    case NO_CONNECTION:
                        Toast.makeText(JobListActivity.this, "No Internet Connection",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }
    }

    private List<ParseObject> fetchFeed() {

        //fetch items from parse

        List<ParseObject> parseList;

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /*Default Location for UK parliament*/
        double longitude = -0.12464760000000297;
        double latitude = 51.4996367;

        if (location!= null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        String[] someplace = {String.valueOf(latitude),String.valueOf(longitude)};

        Bounds bounds = new Bounds(someplace, 200);
        Parse.initialize(this, applicationKEY, clientKEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("PostingJob");
        query.orderByDescending("_created_at");
        try {
            parseList = query.find();
            return parseList;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.v("EXCEPTION!!!!!!!!!!!", e.toString());
            return null;
        }

    }

    private ArrayList<Double> fetchBounds(String lat,String lng, int radius){

        /*
        Log.v("FETCH BOUND", "------------------------------------") ;
        Parse.initialize(this, applicationKEY, clientKEY);
        HashMap<String,Object> params = new HashMap<String, Object>();
        HashMap<String, Object> object = new HashMap<String, Object>();
        object.put("lat",lat);
        object.put("lng",lng);
        params.put("location", object);
        params.put("radius", params.toString()) ;
        ParseCloud.callFunction("getBounds", params,)
        ParseCloud.callFunctionInBackground("getBounds", params, new FunctionCallback<ArrayList<Double>>(){
            public void done(ArrayList<Double> result, ParseException e){
                if (e == null) {
                    setBounds((ArrayList<Double>)result.clone());
                }

            }
        });




    */
        return getBounds();

    }

    public void setBounds(ArrayList<Double> params){
        this.bounds = params;
    }

    public ArrayList<Double> getBounds(){
        return this.bounds;
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
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Function Not working
    public ArrayList<Double> boundingCoordinates(double distance, double radLat, double radLon) {

        final double radius = 6371.01;

        final double MIN_LAT = Math.toRadians(-90d);  // -PI/2
        final double MAX_LAT = Math.toRadians(90d);   //  PI/2
        final double MIN_LON = Math.toRadians(-180d); // -PI
        final double MAX_LON = Math.toRadians(180d);  //  PI

        radLat = Math.toRadians(radLat);
        radLon = Math.toRadians(radLon);

        if (radius < 0d || distance < 0d)
            throw new IllegalArgumentException();

        // angular distance in radians on a great circle
        double radDist = distance / radius;

        double minLat = radLat - radDist;
        double maxLat = radLat + radDist;

        double minLon, maxLon;
        if (minLat > MIN_LAT && maxLat < MAX_LAT) {
            double deltaLon = Math.asin(Math.sin(radDist) /
                    Math.cos(radLat));
            minLon = radLon - deltaLon;
            if (minLon < MIN_LON) minLon += 2d * Math.PI;
            maxLon = radLon + deltaLon;
            if (maxLon > MAX_LON) maxLon -= 2d * Math.PI;
        } else {
            // a pole is within the distance
            minLat = Math.max(minLat, MIN_LAT);
            maxLat = Math.min(maxLat, MAX_LAT);
            minLon = MIN_LON;
            maxLon = MAX_LON;
        }

        ArrayList<Double> bounds = new ArrayList<Double>();
        bounds.add(minLat);
        bounds.add(minLon);
        bounds.add(maxLat);
        bounds.add(maxLon);

        Log.v("--------------BOUNDS_LOCAL-------", String.valueOf(minLat)+ " "
                                                    +  String.valueOf(minLon) + " "
                                                    +  String.valueOf(maxLat) + " "
                                                    +  String.valueOf(maxLat));

        return bounds;
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

            FilterResultsDialog filterView=new FilterResultsDialog(JobListActivity.this);
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
