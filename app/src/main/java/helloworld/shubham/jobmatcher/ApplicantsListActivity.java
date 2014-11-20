package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplicantsListActivity extends Activity {


    public final String applicationKEY = "eS4klh1woY6Qw2CnmhMilJZx6CPn18m9TkmZTztB";
    public final String clientKEY = "2KbPt3cj7kmsYTDRJImiaPsAgXb8l83YRkewH3lv";
    ListView listView;
    ImageButton fab_btn;
    private ApplicantsListAdapter adapter;
    ArrayList<Applicant> applicantList = new ArrayList<Applicant>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.applicant_list_layout);


        listView =  (ListView) findViewById(R.id.applicant_list_lv);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(ApplicantsListActivity.this, ApplicantDetailsActivity.class);
                intent.putExtra("Id", applicantList.get(position).getId());
                startActivity(intent);
            }
        });
        //fab_btn = (ImageButton) findViewById(R.id.fab_btn);


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
        listView.setOnScrollListener(touchListener.makeScrollListener());



        adapter = new ApplicantsListAdapter(this,R.layout.applicant_card,applicantList );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.v("onCreate", "Starting AsyncTask");

        /*Downloading data*/
        final String url = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";
        new DownloadDataTask().execute(url);
    }

    public class DownloadDataTask extends AsyncTask<String, Void, Integer> {

        private final int NO_CONNECTION = 2;
        private final int SUCCESSFULL = 1;
        private final int UNSUCCESSFUL = 0;

        @Override
        protected void onPreExecute() {

            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Log.v("AsyncTask", "Loading Data");

            if(checkNetworkConnection() && isOnline()) {
                List<ParseObject> parseObjects = fetchApplicants();

                if (parseObjects == null){
                    return UNSUCCESSFUL; //"Failed to fetch data!";
                }

                Log.v("AsyncTask", "Iterating through every parse object");
                //Converting Parse objects into Applicants Object

                ArrayList<Integer> allApplicantIds = new ArrayList<Integer>();

                for (int i = 0; i < parseObjects.size(); i++){

                    ArrayList<Integer> applicantIds = new ArrayList<Integer>();

                    ParseObject parseObject = parseObjects.get(i);

                    Log.v("AsyncTask", "Iterating through object " + Integer.toString(i));


                    if (parseObject.containsKey("Applicants")){
                        applicantIds = (ArrayList<Integer>) parseObject.get("Applicants");
                        allApplicantIds.addAll(applicantIds);
                    }

                }

                System.out.println(allApplicantIds);

                List<ParseObject> applicantDetailsObject = fetchApplicantDetails(allApplicantIds);

                for (ParseObject applicant : applicantDetailsObject){

                    System.out.println();

                    String applicantName = applicant.get("User_Name").toString();
                    String applicantStatement = " ";
                    String applicantID = applicant.get("User_ID").toString();


                    applicantList.add(new Applicant(applicantID,applicantName,applicantStatement));


                }




            }else {
                return NO_CONNECTION;
            }



            return SUCCESSFULL;
        }


            //applicantList.add(new Applicant("1","Mark Hannity","Not much experience but enthusiastic about cars."));
            //applicantList.add(new Applicant("2","John Smith","I repair cars."));
            //applicantList.add(new Applicant("3","Chris Sean","Hi, I am looking for work experience."));


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
                        Toast.makeText(ApplicantsListActivity.this, "Problem connecting to Server",Toast.LENGTH_SHORT).show();
                        break;

                    case NO_CONNECTION:
                        Toast.makeText(ApplicantsListActivity.this, "No Internet Connection",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }
    }


    private List<ParseObject> fetchApplicants() {

        //fetch applicants for a job

        List<ParseObject> parseList = null;

        //getBounds()

        Log.v("ApplicantListActivity", "Fetching Feed");

        Parse.initialize(this, applicationKEY, clientKEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        //TODO: Change the query
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("PostingJob");
        query.whereEqualTo("Creator_Id", "25");

        try {
            parseList = query.find();
            Log.v("Applicants", parseList.get(0).toString());
            return parseList;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    private List<ParseObject> fetchApplicantDetails(ArrayList<Integer> applicantIDs) {

        //fetch applicants for a job

        List<ParseObject> parseList = null;

        //getBounds()

        ArrayList<String> applicantIDString = new ArrayList<String>();

        for(Integer applicantId : applicantIDs){

            applicantIDString.add(applicantId.toString());

        }

        Log.v("ApplicantListActivity", "Fetching Applicant Details");

        Parse.initialize(this, applicationKEY, clientKEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        //TODO: Change the query
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("User");
        query.whereContainedIn("User_ID", applicantIDString);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
