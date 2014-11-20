package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class JobOptions extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_match_options);
        
        Button findJobBTN = (Button) findViewById(R.id.findJobBTN);
        Button postJobBTN = (Button) findViewById(R.id.postJobBTN);
        
        findJobBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent jobListIntent = new Intent(JobOptions.this, JobListActivity.class);
				startActivity(jobListIntent);

			}
        });
        
        postJobBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent jobPostingIntent = new Intent(JobOptions.this, JobPosting.class);
				startActivity(jobPostingIntent);

			}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jobs_confirmed_applicants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.applicants) {
            Intent applicantListActivity = new Intent(JobOptions.this, ApplicantsListActivity.class);
            startActivity(applicantListActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}
