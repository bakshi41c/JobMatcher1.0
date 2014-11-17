package helloworld.shubham.jobmatcher;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

	// THIS CLASS WORKS OUT WHETHER THE ADDRESS IS VALID AND UNIQUE. THEN FINDS OUT THE LONGITUDE AND LATITUDE. 
	// AFTER IT WORKS THIS OUT, IT UPLOADS THE JOB DETAILS INCLUDING LONGITUDE/ LATITUDE TO PARSE

    public class ThreadForGettingAddressStatus extends AsyncTask<String, Void, String> {
    	static String response_str; 
    	Context context;
    	ProgressDialog myDialog;
    	String category, contactNumber, jobDescription, jobTitle;
		
    	public ThreadForGettingAddressStatus(Context context, ProgressDialog myDialog, String category, String contactNumber, String jobDescription, String jobTitle)
    	{
    		this.context = context;
    		this.myDialog = myDialog;
    		this.category = category;
    		this.contactNumber = contactNumber;
    		this.jobDescription = jobDescription;
    		this.jobTitle = jobTitle;
    	}

    	@Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                try {
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet(url);
                        // Get the response
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        response_str = client.execute(request, responseHandler);
                        Log.i("RESPONSE",response_str);
                }
                catch (Exception e) {
                    System.out.println("Some error occured: "+e.toString());
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
        	JSONObject jsonObj;
            try {
				jsonObj = new JSONObject(response_str);
				Log.i("IMPORTANT",jsonObj.toString());
				String status = jsonObj.getString("status");
				
				// valid address
				if (status.equals("OK"))
				{
					Log.i("IMPORTANT", "VALID ADDRESS");
					myDialog.dismiss();
					// Check if there are more than one locations with that address
					JSONArray resultsArray = jsonObj.getJSONArray("results");
					if (resultsArray.length()>1)
					{
						Log.i("IMPORTANT", "NOT UNIQUE");
						myDialog.dismiss();
						Toast.makeText(context, "The location you entered is not unique, please enter a more specific address", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Log.i("IMPORTANT", "UNIQUE");
						myDialog.dismiss();
	    	    		myDialog = ProgressDialog.show(context, "Please wait", "Saving your job details", true);
						Toast.makeText(context, "Uploading to parse", Toast.LENGTH_SHORT).show();
						JSONObject addressObj = resultsArray.getJSONObject(0);
						JSONObject geometryObj = addressObj.getJSONObject("geometry");
						JSONObject locationObj = geometryObj.getJSONObject("location");
						final String latitude = locationObj.getString("lat");
						final String longitude = locationObj.getString("lng");
						Log.i("IMPORTANT","lat: "+latitude);
						Log.i("IMPORTANT","lng: "+longitude);
						
						// UPLOAD TO PARSE HERE
						
						// Count the number of jobs already there to know what to make the new job id
    		            ParseQuery<ParseObject> query = ParseQuery.getQuery("PostingJob");
    		            query.countInBackground(new CountCallback() {
    		              public void done(int numberOfRowsInPostingJob, ParseException e) {
    		                if (e == null) {
    		                  // The count request succeeded. Log the count
    		                  Log.i("IMPORTANT","cOUNT: "+numberOfRowsInPostingJob);
    		                  int newJobId = numberOfRowsInPostingJob + 1;
    		                  uploadToParse(newJobId, latitude, longitude, contactNumber, jobDescription, jobTitle);    		                  
    		                } else {
    		                  // The request failed
    		                }
    		              }
    		            });


					}
				}
				
				// invalid address
				else
				{
					Toast.makeText(context, "Please enter a valid address", Toast.LENGTH_SHORT).show();
					myDialog.dismiss();
				}
				
				
            } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //textView.setText(response_str);
        }
        
        public static String getResponseString()
        {
        	return response_str;
        }
        
        public void uploadToParse(int jobId, String latitude, String longitude, String contactNumber, String jobDescription, String jobTitle)
        {
        	  ParseObject jobPostObj = new ParseObject("PostingJob");
	          jobPostObj.put("jobId", String.valueOf(jobId));
	          jobPostObj.put("Category", category.toString());
	          jobPostObj.put("Creator_Id", saveUserID.getSavedUserId(context));
	          jobPostObj.put("Contact_Number", contactNumber);
	          jobPostObj.put("Job_Description", contactNumber);
	          jobPostObj.put("Job_Title", jobTitle);
	          jobPostObj.put("latitude", latitude);
	          jobPostObj.put("longitude", longitude);

	           jobPostObj.saveInBackground();
	           myDialog.dismiss();
	           
	           
        }
        
    }