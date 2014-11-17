package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class MainActivity extends Activity {

	EditText userNameET, phoneNumberET;
	String mobileNumber;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_screen);

			userNameET = (EditText) findViewById(R.id.userNameET);
			phoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
			
            Parse.initialize(this, "eS4klh1woY6Qw2CnmhMilJZx6CPn18m9TkmZTztB", "2KbPt3cj7kmsYTDRJImiaPsAgXb8l83YRkewH3lv");
            
            ParseQuery<ParseObject> query = ParseQuery.getQuery("PostingJob");
            query.whereEqualTo("jobId", "8");
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject jobObj, ParseException e) {
                  if (e == null) {
                      jobObj.add("Applicants", "Hello");
                      jobObj.saveInBackground();
                  }
                }
              });
            
            
            
            //calculateDistanceBetweenCurrentLocationAndCoordinates();
            
            if (SaveUserDetails.getSavedUserName(this) != null)
            {
            	// already have their username
            	finish();
				Intent JobOptionsIntent = new Intent(MainActivity.this, JobOptions.class);
				startActivity(JobOptionsIntent);
            }
            
            Button getStartedBTN = (Button) findViewById(R.id.getStartedBTN);
            
            getStartedBTN.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				if (userNameET.getText().toString() != null)
    				{
    					Log.i("important",userNameET.getText().toString());
    					SaveUserDetails.saveUserName(userNameET.getText().toString(), MainActivity.this);
    					mobileNumber = phoneNumberET.getText().toString();
    					SaveUserMobile.saveUserMobile(mobileNumber, MainActivity.this);
    					Log.i("important","usermobile: "+phoneNumberET.getText().toString());
    					
    					// Upload to Parse
    		          //  userObj.put("User_ID", value)
    		            //userObj.saveInBackground();
    		            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
    		            query.countInBackground(new CountCallback() {
    		              public void done(int numberOfRowsInPostingJob, ParseException e) {
    		                if (e == null) {
    		                  // The count request succeeded. Log the count
    		                  Log.i("IMPORTANT","cOUNT: "+numberOfRowsInPostingJob);
    		                  int newUserId = numberOfRowsInPostingJob + 1;
    		                  Log.i("IMPORTANT","userNameET: "+userNameET.getText().toString());
    		                  saveUserID.saveUserId(String.valueOf(newUserId), MainActivity.this);
    		                  uploadToParse(newUserId, userNameET.getText().toString(), mobileNumber);    
    		                } else {
    		                  // The request failed
    		                }
    		              }
    		            });
    					
    					finish();
    					Intent jobOptionsIntent = new Intent(MainActivity.this, JobOptions.class);
    					startActivity(jobOptionsIntent);
    				}
    			}
    		});
    		
            
    }

        public void uploadToParse(int userId, String userName, String mobileNumber)
        {
            ParseObject userObj = new ParseObject("User");
            userObj.put("User_ID", String.valueOf(userId));
            userObj.put("User_Name", userName.toString());
            userObj.put("Mobile_Number", mobileNumber.toString());
            
            userObj.saveInBackground(new SaveCallback() {
              public void done(ParseException e) {
            		    // Handle success or failure here ...
            	  
            	  	}
            });
        }
        
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
