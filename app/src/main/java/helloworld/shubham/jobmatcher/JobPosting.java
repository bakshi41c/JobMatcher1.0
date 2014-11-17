package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class JobPosting extends Activity {

	private Button submitJob;
	private EditText jobDescriptionET, contactNumberET, locationET, jobTitleET;
	private String[] categoryArray = {"Call Centre & Customer Service","Charity & Voluntary", "Education & Training", "Garden","House Keeping","Personal Care & Service","Repairs","Sales","Others"};
	private Spinner mySpinner;
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.posting_a_job);
            
            
           
            
            // initialise the button, edittexts and textviews
            initialiseLayouts();
            
            // onclicklistener for posting the job
    		submitJob.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    			//	if (new JobPosting().AreAnyTextFieldsEmpty())
    				//	Toast.makeText(JobPosting.this, "You must fill in all of the text fields!", Toast.LENGTH_SHORT);
    	    		ProgressDialog myDialog = ProgressDialog.show(JobPosting.this, "Please wait", "Checking if the location entered is valid and unique", true);
    		        String contactNumber = contactNumberET.getText().toString();
    		        String jobDescription = jobDescriptionET.getText().toString();
    		        String jobTitle = jobTitleET.getText().toString();
    		        ThreadForGettingAddressStatus task = new ThreadForGettingAddressStatus(JobPosting.this, myDialog, categoryArray[mySpinner.getSelectedItemPosition()], contactNumber, jobDescription, jobTitle);
    		        String locationFromUser = locationET.getText().toString();
    		        locationFromUser = locationFromUser.replaceAll(" ", "+");
    		        task.execute(new String[] {"https://maps.googleapis.com/maps/api/geocode/json?address="+locationFromUser });
    			}
    		});
    		
    			
    		
    		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String> (JobPosting.this, android.R.layout.simple_spinner_dropdown_item, categoryArray);
    		mySpinner = (Spinner) findViewById(R.id.spinnerCategory);
    		mySpinner.setAdapter(categoryAdapter);
        }
        
        
	    public void initialiseLayouts()
		{
    		submitJob= (Button) findViewById(R.id.postJobBTN);
    		jobDescriptionET= (EditText) findViewById(R.id.jobDescriptionET);
    		contactNumberET= (EditText) findViewById(R.id.contactNumberET);
    		locationET= (EditText) findViewById(R.id.locationET);
    		jobTitleET = (EditText) findViewById(R.id.jobTitleET);
		}
		
		
		public boolean AreAnyTextFieldsEmpty()
		{
			if ( (jobDescriptionET.getText().toString().matches("")) || (contactNumberET.getText().toString().matches("")) || (locationET.getText().toString().matches("")))
			{
				return true;
			}
			else
				return false;
		}

		
}
